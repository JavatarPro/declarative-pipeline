/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/declarative-pipeline/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.javatar.pipeline.builder


import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.model.CacheRequest
import pro.javatar.pipeline.builder.model.JenkinsTool
import pro.javatar.pipeline.builder.model.Python
import pro.javatar.pipeline.config.Config
import pro.javatar.pipeline.exception.*
import pro.javatar.pipeline.integration.docker.DockerOnlyBuildService
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.model.*
import pro.javatar.pipeline.release.CurrentVersionAware
import pro.javatar.pipeline.release.ReleaseType
import pro.javatar.pipeline.release.ReleaseUploadArtifactType
import pro.javatar.pipeline.release.SetupVersionAware
import pro.javatar.pipeline.service.*
import pro.javatar.pipeline.service.cache.CacheRequestHolder
import pro.javatar.pipeline.service.impl.*
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.s3.AwsS3DeploymentService
import pro.javatar.pipeline.service.test.AutoTestsService
import pro.javatar.pipeline.service.test.SonarQubeService
import pro.javatar.pipeline.service.test.UiAutoTestsService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.stage.*
import pro.javatar.pipeline.stage.deploy.DeployToDevEnvStage
import pro.javatar.pipeline.stage.deploy.DeployToProdEnvStage
import pro.javatar.pipeline.stage.deploy.DeployToQAEnvStage
import pro.javatar.pipeline.stage.deploy.DeployToStagingEnvStage
import pro.javatar.pipeline.stage.sign.DevOpsSignOffStage
import pro.javatar.pipeline.stage.sign.DeveloperSignOffStage
import pro.javatar.pipeline.stage.sign.QaSignOffStage
import pro.javatar.pipeline.util.Logger

/**
 * TODO class too big, need some refactoring
 * TODO move stage creation to stage builder inside stage package
 * @author Borys Zora
 * @since 2018-03-09
 */
class FlowBuilder implements Serializable {

    ReleaseInfo releaseInfo = new ReleaseInfo()
    BuildServiceType buildType
    boolean useBuildNumberForVersion = true
    RevisionControlBuilder revisionControlBuilder
    List<StageType> stageTypes = new ArrayList<>()
    Map<StageType, Stage> availableStages = new HashMap<>()
    Set<StageType> stageTypesToBeSkipped = new HashSet<>()
    SlackBuilder slackBuilder // TODO
    DockerBuilder dockerBuilder // TODO
    SonarQubeBuilder sonarQubeBuilder // TODO
    SwaggerBuilder swaggerBuilder // TODO
    BackEndAutoTestsServiceBuilder backEndAutoTestsServiceBuilder;

    // services
    Maven maven
    MavenBuildService mavenBuildService
    GradleBuildService gradleBuildService
    Python python
    PythonBuildService pythonBuildService
    JenkinsTool jenkinsTool
    DockerBuildService dockerBuildService
    Npm npm = new Npm()
    String moduleRepository = ""
    NpmBuildService npmBuildService
    DockerNpmBuildService dockerNpmBuildService
    SenchaService senchaService
    BuildService buildService
    CdnDeploymentService cdnDeploymentService
    AwsS3DeploymentService awsS3DeploymentService
    UiDeploymentType uiDeploymentType = UiDeploymentType.AWS_S3
    DeploymentService deploymentService
    AutoTestsService autoTestsService
    RevisionControlService revisionControlService
    ReleaseService releaseService
    DockerService dockerService
    SlackService slackService
    SonarQubeService sonarQubeService
    SwaggerService swaggerService
    PipelineStagesSuit suit

    SetupVersionAware setupVersionAware
    CurrentVersionAware currentVersionAware
    List<ReleaseType> releaseTypes
    List<ReleaseUploadArtifactType> releaseUploadArtifactTypes

    NexusUploadAware uploadAware;
    JenkinsDslService jenkinsDslService;
    Config config;

    FlowBuilder(JenkinsDslService jenkinsDslService) {
        Logger.debug("FlowBuilder: constructor")
        this.jenkinsDslService = jenkinsDslService;
    }

    Flow build() {
        Logger.info("build Flow started")

        // tmp
        backEndAutoTestsServiceBuilder = new BackEndAutoTestsServiceBuilder(config.autoTestConfig());

        createServices()
        createStages()

        Flow flow = new Flow(releaseInfo, jenkinsDslService);
        populateStages(flow, stageTypes)

        Logger.info("build Flow finished: " + toString())
        return flow
    }

    def populateStages(Flow flow, List<StageType> stageTypes) {
        for (StageType stageType : stageTypes) {
            Logger.info("populateStages for stageType: " + stageType.name())
            Stage stage = availableStages.get(stageType)
            if (stageTypesToBeSkipped.contains(stageType)) {
                stage.skipStage = true
            }
            Logger.info(stage.getName() + " is present: " + stageType.name())
            flow.addStage(stage)
        }
        Logger.info("Flow with stages: " + flow.getStageNames())
    }

    void createServices() {
        Logger.debug("FlowBuilder:createServices started")
        prepareRevisionControl()
        prepareDockerService()
        prepareSonarQube()
        prepareBuildService()
        prepareDeploymentService()
        autoTestsService = getAutoTestsService()
        releaseService = getReleaseService()
        populateServiceContextHolder()
        Logger.debug("FlowBuilder:createServices finished")
    }

    def prepareDockerService() {
        dockerService = dockerBuilder.build()
        Logger.debug("docker service preparation complete")
    }

    void prepareRevisionControl() {
        Logger.debug("FlowBuilder:prepareRevisionControl started")
        revisionControlService = revisionControlBuilder.build()
        Logger.debug("created revisionControlService: ${revisionControlService}")
        Logger.debug("FlowBuilder:prepareRevisionControl finished")
    }

    void prepareDeploymentService() {
        deploymentService = getAppropriateDeploymentService(buildType)
    }

    void prepareBuildService() {
        Logger.info("complete setup build service")
        setupBuildServiceType()
        if (buildType == BuildServiceType.MAVEN) {
            Logger.debug("FlowBuilder:prepareBuildService: current build type ${buildType}")
            // TODO refactor, previously it does not work because of CPS jenkins issue
            populate(maven)
            mavenBuildService = buildMavenBuildService(maven, jenkinsTool)
            mavenBuildService.setUp()
            uploadAware = mavenBuildService;
            dockerBuildService = new DockerBuildService(mavenBuildService, dockerService)
        } else if (buildType == BuildServiceType.GRADLE) {
            gradleBuildService = new GradleBuildService(jenkinsDslService, config.gradleConfig())
            gradleBuildService.setUp()
            uploadAware = gradleBuildService;
            dockerBuildService = new DockerBuildService(gradleBuildService, dockerService)
        }
        setupBuildService()
        Logger.info("FlowBuilder:prepareBuildService: created buildService: " + buildService.toString())
    }

    def prepareSonarQube() {
        if (sonarQubeBuilder != null) {
            Logger.debug("sonarQubeBuilder start build: " + sonarQubeBuilder.toString())
            sonarQubeService = sonarQubeBuilder.build()
            Logger.debug("sonarQubeBuilder finish build")
        } else {
            Logger.info("sonarQubeBuilder is not provided")
        }
    }

    DeploymentService getAppropriateDeploymentService(BuildServiceType buildServiceType) {
        if (buildType == BuildServiceType.MAVEN
                || buildType == BuildServiceType.DOCKER
                || buildType == BuildServiceType.GRADLE
                || buildType == BuildServiceType.PHP
                || buildType == BuildServiceType.PYTHON) {
            return new DockerDeploymentService(releaseInfo, dockerService)
        }
        if (buildType == BuildServiceType.NPM_YARN_DOCKER
                || buildType == BuildServiceType.NPM_DOCKER
                || buildType == BuildServiceType.NPM_JUST_DOCKER
                || uiDeploymentType == UiDeploymentType.DOCKER
                || buildType == BuildServiceType.TEST_DOCKER) {
            releaseInfo.setIsUi(true)
            releaseInfo.setOptimizeDockerContext(true)
            def deploymentService = new DockerDeploymentService(releaseInfo, dockerService)
            return deploymentService
        }
        if (uiDeploymentType == UiDeploymentType.AWS_S3) {
            return awsS3DeploymentService
        }
        if (buildType == BuildServiceType.NPM || buildType == BuildServiceType.SENCHA) {
            // TODO choose deployment type
            return new CdnDeploymentService(releaseInfo.getServiceName(), mavenBuildService, null)
        }
        throw new DeploymentServiceCreationException("Could not find this buildServiceType: ${buildServiceType}")
    }

    void createStages() {
        Logger.info("FlowBuilder:createStages: createStages started")
        availableStages.put(StageType.BUILD_AND_UNIT_TESTS,
                new BuildAndUnitTestStage(buildService, revisionControlService))
        availableStages.put(StageType.AUTO_TESTS,
                new AutoTestsStage(autoTestsService, jenkinsDslService, config.autoTestConfig()))
        availableStages.put(StageType.RELEASE, new ReleaseArtifactsStage(releaseService))
        availableStages.put(StageType.BACKWARD_COMPATIBILITY_TEST,
                new DatabaseBackwardCompatibilityStage(dockerService, deploymentService))
        availableStages.put(StageType.BACKWARD_COMPATIBILITY_AUTO_TESTS,
                new AutoTestsStage(autoTestsService, jenkinsDslService, config.autoTestConfig()))
        createSignOffStages()
        createDeployStages()
        Logger.info("FlowBuilder:createStages: createStages finished")
    }

    void createDeployStages() {
        Logger.info("FlowBuilder:createDeployStages started")
        availableStages.put(StageType.DEPLOY_ON_DEV_ENV, new DeployToDevEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_QA_ENV, new DeployToQAEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_STAGING_ENV, new DeployToStagingEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_PROD_ENV, new DeployToProdEnvStage(deploymentService))
        Logger.info("FlowBuilder:createDeployStages finished")
    }

    void createSignOffStages() {
        Logger.info("FlowBuilder:createSignOffStages started")
        availableStages.put(StageType.DEV_SIGN_OFF, new DeveloperSignOffStage())
        availableStages.put(StageType.QA_SIGN_OFF, new QaSignOffStage())
        availableStages.put(StageType.DEVOPS_SIGN_OFF, new DevOpsSignOffStage())
        Logger.info("FlowBuilder:createSignOffStages finished")
    }

    FlowBuilder skipStage(StageType stageType) {
        Logger.info("FlowBuilder:skipStage stageType: ${stageType.name()}")
        stageTypesToBeSkipped.add(stageType)
        return this
    }

    FlowBuilder skipStages(String commaSeparatedStageTypes) {
        List<String> stageTypes = commaSeparatedStageTypes.split(",")
        for (String stageType : stageTypes) {
            stageTypesToBeSkipped.add(StageType.valueOf(stageType))
        }
        return this
    }

    FlowBuilder addPipelineStages(List<String> stageTypeList) {
        stageTypeList.each { stageType -> stageTypes.add(StageType.valueOf(stageType)) }
        return this
    }

    FlowBuilder addPipelineStage(String stageType) {
        stageTypes.add(StageType.valueOf(stageType))
        return this
    }

    FlowBuilder addPipelineStage(StageType stageType) {
        stageTypes.add(stageType)
        return this
    }

    FlowBuilder setConfig(Config config) {
        this.config = config;
        return this;
    }

    def populate(Maven maven) { // TODO replace with more preferable
        Logger.debug("FlowBuilder:populate maven: ${maven} started")
        if (isUi(releaseInfo.getServiceName())) {
            maven.withPackaging("zip")
            maven.withArtifactId(releaseInfo.getServiceName().replace("-ui", ""))
        }
        Logger.debug("populate maven: ${maven} finished")
    }

    boolean isUi(String repo) {
        return (repo.endsWith("-ui")
                || buildType == BuildServiceType.NPM
                || buildType == BuildServiceType.SENCHA)
    }

    // TODO just validate BuildServiceType instead of guessing
    void setupBuildServiceType() {
        Logger.debug("setupBuildServiceType started")
        if (buildType != null) {
            Logger.debug("setupBuildServiceType manually provided, buildType: ${buildType}")
            return
        }
        // TODO it is better fail rather than guess
        if (revisionControlService.repo.endsWith("ui")) {
            buildType = BuildServiceType.NPM
        } else if (revisionControlService.repo.endsWith("service")) {
            buildType = BuildServiceType.MAVEN
        } else {
            if (buildType == null) {
                throw new UnrecognizedBuildServiceTypeException("type is null");
            }
        }
        Logger.debug("setupBuildServiceType finished, buildType: ${buildType}")
    }

    boolean isNpm() {
        if (buildType == BuildServiceType.NPM) {
            return true
        }
        return false
    }

    def setupBuildService() {
        Logger.debug("setupBuildService started buildType: " + buildType + ", maven: " + maven.toString())

        if (buildType == BuildServiceType.MAVEN && suit == PipelineStagesSuit.SERVICE) {
            // TODO refactor
            buildService = dockerBuildService
        } else if (buildType == BuildServiceType.MAVEN && suit == PipelineStagesSuit.SERVICE_WITH_DB) {
            buildService = dockerBuildService
        } else if (buildType == BuildServiceType.MAVEN && suit == PipelineStagesSuit.LIBRARY) {
            // TODO refactor
            buildService = mavenBuildService
        } else if (buildType == BuildServiceType.MAVEN) {
            buildService = dockerBuildService
        } else if (buildType == BuildServiceType.GRADLE && suit == PipelineStagesSuit.SERVICE) {
            buildService = dockerBuildService
        } else if (buildType == BuildServiceType.GRADLE && suit == PipelineStagesSuit.LIBRARY) {
            buildService = gradleBuildService
        } else if (buildType == BuildServiceType.NPM) {
            npmBuildService = npm.build()
            buildService = npmBuildService
        } else if (buildType == BuildServiceType.NPM_DOCKER) {
            DockerNpmBuildService service = new DockerNpmBuildService(dockerService, jenkinsDslService)
            service.setType(npm.getType())
            service.setNpmVersion(npm.npmVersion)
            dockerNpmBuildService = service
            buildService = dockerNpmBuildService
        } else if (buildType == BuildServiceType.NPM_YARN_DOCKER) {
            DockerNpmBuildService service = new DockerNpmYarnBuildService(dockerService, jenkinsDslService)
            service.setType(npm.getType())
            service.setNpmVersion(npm.npmVersion)
            dockerNpmBuildService = service
            buildService = dockerNpmBuildService
        } else if (buildType == BuildServiceType.NPM_JUST_DOCKER || buildType == BuildServiceType.TEST_DOCKER) {
            npmBuildService = npm.build()
            npmBuildService.withDslService(jenkinsDslService)
            buildService = new DockerOnlyBuildService(dockerService, npmBuildService, npmBuildService)
        } else if (buildType == BuildServiceType.SENCHA) {
            senchaService = new SenchaService()
            buildService = senchaService
        } else if (buildType == BuildServiceType.PHP) {
            buildService = new PhpBuildService(dockerService)
        } else if (buildType == BuildServiceType.PYTHON) {
            buildService = new PythonBuildService(dockerService, python.versionFile, python.versionParameter, python.projectDirectory)
        }

        // TODO
        setupVersionAware = buildService
        currentVersionAware = buildService

        if (buildType == BuildServiceType.DOCKER) {
            new DockerOnlyBuildService(dockerService, setupVersionAware, currentVersionAware)
        }
        buildService.useBuildNumberForVersion = useBuildNumberForVersion
        Logger.debug("buildService: " + buildService.toString())
        Logger.info("setupBuildService finished")
    }

    void validate() throws PipelineException {
        validateRevisionControl(revisionControlBuilder)
    }

    void validateRevisionControl(RevisionControlBuilder revisionControlBuilder) throws InvalidRepositoryNameException {

    }

    FlowBuilder addPipelineStages(String pipelineStagesSuit) {
        Logger.info("Current suit is $pipelineStagesSuit")
        suit = PipelineStagesSuit.fromString(pipelineStagesSuit)
        if (suit == PipelineStagesSuit.SERVICE) {
            return addDefaultPipelineStages()
        }
        if (suit == PipelineStagesSuit.LIBRARY) {
            return addReleaseCommonLibsPipelineStages()
        }
        if (suit == PipelineStagesSuit.SERVICE_WITH_DB) {
            return addServiceWithDBStages()
        }
        if (suit == PipelineStagesSuit.SERVICE_SIMPLE) {
            return addServiceSimpleStages()
        }
        throw new UnrecognizedPipelineStagesSuitException("can not find suit: ${pipelineStagesSuit}")
    }

    FlowBuilder addDefaultPipelineStages() {
        Logger.info("Load default stages")
        addPipelineStage(StageType.BUILD_AND_UNIT_TESTS)
        addPipelineStage(StageType.DEPLOY_ON_DEV_ENV)
        addPipelineStage(StageType.AUTO_TESTS)
        addPipelineStage(StageType.DEV_SIGN_OFF)
        addPipelineStage(StageType.RELEASE)
        addPipelineStage(StageType.DEPLOY_ON_QA_ENV)
        addPipelineStage(StageType.QA_SIGN_OFF)
//        addPipelineStage(StageType.DEPLOY_ON_STAGING_ENV)
        addPipelineStage(StageType.DEVOPS_SIGN_OFF)
        addPipelineStage(StageType.DEPLOY_ON_PROD_ENV)
        return this
    }

    FlowBuilder addServiceWithDBStages() {
        Logger.info("Load stages for service-with-db")
        addPipelineStage(StageType.BUILD_AND_UNIT_TESTS)
        addPipelineStage(StageType.DEPLOY_ON_DEV_ENV)
        addPipelineStage(StageType.AUTO_TESTS)
        addPipelineStage(StageType.BACKWARD_COMPATIBILITY_TEST)
        addPipelineStage(StageType.BACKWARD_COMPATIBILITY_AUTO_TESTS)
        addPipelineStage(StageType.DEPLOY_ON_DEV_ENV) //revert to current version
        addPipelineStage(StageType.DEV_SIGN_OFF)
        addPipelineStage(StageType.RELEASE)
        addPipelineStage(StageType.DEPLOY_ON_QA_ENV)
        addPipelineStage(StageType.QA_SIGN_OFF)
//        addPipelineStage(StageType.DEPLOY_ON_STAGING_ENV)
        addPipelineStage(StageType.DEVOPS_SIGN_OFF)
        addPipelineStage(StageType.DEPLOY_ON_PROD_ENV)
        return this
    }

    FlowBuilder addServiceSimpleStages() {
        Logger.info("Load stages for service-simple")
        // TODO load from suit.yml & stage.yml
        addPipelineStage(StageType.BUILD_AND_UNIT_TESTS)
        addPipelineStage(StageType.DEPLOY_ON_DEV_ENV)
        addPipelineStage(StageType.AUTO_TESTS)
        addPipelineStage(StageType.RELEASE)
        addPipelineStage(StageType.DEVOPS_SIGN_OFF)
        addPipelineStage(StageType.DEPLOY_ON_PROD_ENV)
        return this
    }

    FlowBuilder addReleaseCommonLibsPipelineStages() {
        Logger.info("Load stages for libraries")
        addPipelineStage(StageType.BUILD_AND_UNIT_TESTS)
        addPipelineStage(StageType.AUTO_TESTS)
        addPipelineStage(StageType.DEV_SIGN_OFF)
        addPipelineStage(StageType.RELEASE)
        return this
    }

    AutoTestsService getAutoTestsService() {
        if (isUi(releaseInfo.getServiceName())) {
            return new UiAutoTestsService().withUiSystemTestsJobName("common/ui-system-tests")
        } else {
            backEndAutoTestsServiceBuilder.withSonarQubeService(sonarQubeService)
                    .withBuildService(buildService)
                    .withSuit(suit).build()
        }
    }

    ReleaseService getReleaseService() {
        if (suit == PipelineStagesSuit.LIBRARY
                && (buildType == BuildServiceType.MAVEN || buildType == BuildServiceType.GRADLE)) {
            return new BackEndLibraryReleaseService(buildService, uploadAware, revisionControlService)
        }
        if (buildType == BuildServiceType.NPM
                || buildType == BuildServiceType.NPM_DOCKER
                || buildType == BuildServiceType.NPM_YARN_DOCKER
                || buildType == BuildServiceType.SENCHA) {
            return new UiReleaseService(buildService, revisionControlService)
        }
        if (buildType == BuildServiceType.PYTHON
                || buildType == BuildServiceType.PHP
                || buildType == BuildServiceType.TEST_DOCKER
                || buildType == BuildServiceType.PHP_PYTHON) {
            return new VcsAndDockerRelease(buildService, revisionControlService, dockerService)
        }
        // TODO not obvious, why should not we throw exception at the end if no one matches
        boolean skipUploadService = suit == PipelineStagesSuit.SERVICE_SIMPLE
        return new BackEndReleaseService(buildService, uploadAware, revisionControlService, dockerService)
                .setSkipUploadService(skipUploadService)
    }

    FlowBuilder withRevisionControl(RevisionControlBuilder revisionControlBuilder) {
        this.revisionControlBuilder = revisionControlBuilder
        return this
    }

    FlowBuilder withSlack(SlackBuilder slackBuilder) {
        this.slackBuilder = slackBuilder
        return this
    }

    FlowBuilder withBuildType(String buildType) {
        this.buildType = BuildServiceType.fromString(buildType)
        return this
    }

    FlowBuilder withUseBuildNumberForVersion(Boolean useBuildNumberForVersion) {
        if (useBuildNumberForVersion == null) {
            return
        }
        this.useBuildNumberForVersion = useBuildNumberForVersion
        return this
    }

    FlowBuilder withDocker(DockerBuilder dockerBuilder) {
        this.dockerBuilder = dockerBuilder
        return this
    }

    FlowBuilder withCacheRequest(CacheRequest cacheRequest) {
        CacheRequestHolder.setCaches(cacheRequest.getCaches())
        return this
    }

    FlowBuilder addMaven(Maven maven) {
        this.maven = maven
        return this
    }

    FlowBuilder addPython(Python python) {
        this.python = python
        return this
    }

    FlowBuilder addJenkinsTool(JenkinsTool jenkinsTool) {
        this.jenkinsTool = jenkinsTool
        return this;
    }

    FlowBuilder withServiceName(String serviceName) {
        this.releaseInfo.setServiceName(serviceName)
        return this
    }

    FlowBuilder withSonar(SonarQubeBuilder sonarQubeBuilder) {
        this.sonarQubeBuilder = sonarQubeBuilder
        return this
    }

    FlowBuilder withSwagger(SwaggerBuilder swaggerBuilder) {
        this.swaggerBuilder = swaggerBuilder
        return this
    }

    FlowBuilder addNpm(Npm npm) {
        this.npm = npm
        return this
    }

    FlowBuilder withBackEndAutoTestsServiceBuilder(BackEndAutoTestsServiceBuilder backEndAutoTestsServiceBuilder) {
        this.backEndAutoTestsServiceBuilder = backEndAutoTestsServiceBuilder
        return this
    }

    FlowBuilder addModuleRepository(String repository) {
        this.moduleRepository = repository
        return this
    }

    FlowBuilder withS3(S3Builder s3Builder) {
        if (s3Builder == null) return this
        this.awsS3DeploymentService = s3Builder.build()
        return this
    }

    FlowBuilder withUiDeploymentType(String uiDeploymentTypeRowValue) {
        uiDeploymentType = UiDeploymentType.fromString(uiDeploymentTypeRowValue)
        return this
    }

    FlowBuilder setReleaseTypes(List<ReleaseType> releaseTypes) {
        this.releaseTypes = releaseTypes
        return this
    }

    FlowBuilder setReleaseUploadArtifactTypes(List<ReleaseUploadArtifactType> releaseUploadArtifactTypes) {
        this.releaseUploadArtifactTypes = releaseUploadArtifactTypes
        return this
    }

    MavenBuildService buildMavenBuildService(Maven maven, JenkinsTool tool) {
        MavenBuildService service = new MavenBuildService()
        service.setJava(maven.getJava())
        service.setMaven(maven.getMaven())
        service.setJavaTool(tool.java)
        service.setMavenTool(tool.maven)
        service.setMavenParams(maven.getMavenParams())
        service.setGroupId(maven.getGroupId())
        service.setArtifactId(maven.getArtifactId())
        service.setPackaging(maven.getPackaging())
        service.setRepositoryId(maven.getRepositoryId())
        service.setLayout(maven.getLayout())
        service.setRepoUrl(maven.getRepoUrl())
        return service
    }

    void populateServiceContextHolder() {
        ContextHolder.add(mavenBuildService)
        ContextHolder.add(dockerBuildService)
        ContextHolder.add(npmBuildService)
        ContextHolder.add(senchaService)
        ContextHolder.add(buildService)
        ContextHolder.add(deploymentService)
        ContextHolder.add(autoTestsService)
        ContextHolder.add(revisionControlService)
        ContextHolder.add(RevisionControlService.class, revisionControlService)
        ContextHolder.add(releaseService)
        ContextHolder.add(dockerService)
        ContextHolder.add(slackService)
        ContextHolder.add(sonarQubeService)
        ContextHolder.add(swaggerService)
        ContextHolder.add(dockerNpmBuildService)
    }

}

