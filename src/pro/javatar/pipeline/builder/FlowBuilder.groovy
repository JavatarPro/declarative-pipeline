/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/pipeline-utils/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.javatar.pipeline.builder

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.exception.*
import pro.javatar.pipeline.model.*
import pro.javatar.pipeline.service.*
import pro.javatar.pipeline.service.test.*
import pro.javatar.pipeline.service.orchestration.*
import pro.javatar.pipeline.service.impl.*
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.stage.*
import pro.javatar.pipeline.stage.deploy.*
import pro.javatar.pipeline.stage.sign.*

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
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
    BackEndAutoTestsServiceBuilder backEndAutoTestsServiceBuilder

    // services
    Maven maven
    MavenBuildService mavenBuildService
    DockerMavenBuildService dockerMavenBuildService
    Npm npm = new Npm()
    String moduleRepository = ""
    NpmBuildService npmBuildService
    DockerNpmBuildService dockerNpmBuildService
    SenchaService senchaService
    BuildService buildService
    CdnDeploymentService cdnDeploymentService
    AwsS3DeploymentService awsS3DeploymentService
    UiDeploymentType uiDeploymentType = UiDeploymentType.NONE
    DeploymentService deploymentService
    AutoTestsService autoTestsService
    RevisionControlService revisionControlService
    ReleaseService releaseService
    DockerService dockerService
    SlackService slackService
    SonarQubeService sonarQubeService
    SwaggerService swaggerService
    PipelineStagesSuit suit

    FlowBuilder() {}

    FlowBuilder(def dsl) {
        PipelineDslHolder.dsl = dsl
    }

    Flow build() {
        dsl.echo "build Flow started"
        createServices()
        createStages()

        Flow flow = new Flow(releaseInfo)
        populateStages(flow, stageTypes)

        dsl.echo "build Flow finished: ${toString()}"
        return flow
    }

    def populateStages(Flow flow, List<StageType> stageTypes) {
        for (StageType stageType: stageTypes) {
            dsl.echo "populateStages for stageType: ${stageType.name()}"
            Stage stage = availableStages.get(stageType)
            if (stageTypesToBeSkipped.contains(stageType)) {
                stage.skipStage = true
            }
            flow.addStage(stage)
        }
    }

    void createServices() {
        dsl.echo "createServices started"
        revisionControlService = revisionControlBuilder.build()
        dsl.echo "created revisionControlService: ${revisionControlService.toString()}"
        setupBuildServiceType()
        dsl.echo "complete setup build service"
        dsl.echo "current build type ${buildType}"
        populate(maven)
        dsl.echo "population complete"
        dockerService = dockerBuilder.build()
        dsl.echo "docker service preparation complete"
        prepareSonarQube()
        dsl.echo "DockerMavenBuildService debug"
        dsl.echo "new DockerMavenBuildService(${maven.toString()})"
        dsl.echo "new DockerMavenBuildService(${dockerService.toString()})"
        dockerMavenBuildService = new DockerMavenBuildService(maven, dockerService)
        dsl.echo "dockerMavenBuildService.populateMaven(maven)"
        dockerMavenBuildService.populateMaven(maven)
        dsl.echo "before maven build"
        mavenBuildService = buildMavenBuildService(maven)
        mavenBuildService.setUp()
        setupBuildService()
        cdnDeploymentService = new CdnDeploymentService(releaseInfo.getServiceName(), mavenBuildService, buildService)
        deploymentService = getAppropriateDeploymentService(buildType)
        autoTestsService = getAutoTestsService()
        releaseService = getReleaseService()
        dsl.echo "created buildService: ${buildService.toString()}"
        populateServiceContextHolder()
        dsl.echo "createServices finished"
    }

    def prepareSonarQube() {
        if (sonarQubeBuilder != null) {
            dsl.echo "sonarQubeBuilder start build: ${sonarQubeBuilder.toString()}"
            sonarQubeService = sonarQubeBuilder.build()
            dsl.echo "sonarQubeBuilder finish build"
        } else {
            dsl.echo "sonarQubeBuilder is not provided"
        }
    }

    DeploymentService getAppropriateDeploymentService(BuildServiceType buildServiceType) {
        if (buildType == BuildServiceType.MAVEN
                || buildType == BuildServiceType.PHP
                || buildType == BuildServiceType.PYTHON) {
            return new DockerDeploymentService(releaseInfo, dockerService)
        }
        if (uiDeploymentType == UiDeploymentType.AWS_S3) {
            return awsS3DeploymentService
        }
        if (buildType == BuildServiceType.NPM || buildType == BuildServiceType.SENCHA) {
            return cdnDeploymentService
        }
        throw DeploymentServiceCreationException("Could not find this buildServiceType: ${buildServiceType}")
    }

    void createStages() {
        dsl.echo "createStages started"
        availableStages.put(StageType.BUILD_AND_UNIT_TESTS,
                new BuildAndUnitTestStage(buildService, revisionControlService))
        availableStages.put(StageType.AUTO_TESTS, new AutoTestsStage(autoTestsService, revisionControlService))
        availableStages.put(StageType.RELEASE, new ReleaseArtifactsStage(releaseService))
        createSignOffStages()
        createDeployStages()
        dsl.echo "createStages finished"
    }

    void createDeployStages() {
        availableStages.put(StageType.DEPLOY_ON_DEV_ENV, new DeployToDevEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_QA_ENV, new DeployToQAEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_STAGING_ENV, new DeployToStagingEnvStage(deploymentService))
        availableStages.put(StageType.DEPLOY_ON_PROD_ENV, new DeployToProdEnvStage(deploymentService))
    }

    void createSignOffStages() {
        availableStages.put(StageType.DEV_SIGN_OFF, new DeveloperSignOffStage())
        availableStages.put(StageType.QA_SIGN_OFF, new QaSignOffStage())
        availableStages.put(StageType.DEVOPS_SIGN_OFF, new DevOpsSignOffStage())
    }

    FlowBuilder skipStage(StageType stageType) {
        stageTypesToBeSkipped.add(stageType)
        return this
    }

    FlowBuilder skipStages(String commaSeparatedStageTypes) {
        List<String> stageTypes = commaSeparatedStageTypes.split(",")
        for(String stageType: stageTypes) {
            stageTypesToBeSkipped.add(StageType.valueOf(stageType))
        }
        return this
    }

    FlowBuilder addPipelineStages(List<String> stageTypeList) {
        stageTypeList.each {stageType -> stageTypes.add(StageType.valueOf(stageType))}
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

    def populate(Maven maven) { // TODO
        dsl.echo "populate maven: ${maven} started"
        if (isUi(releaseInfo.getServiceName())) {
            maven.withPackaging("zip")
            maven.withArtifactId(releaseInfo.getServiceName().replace("-ui", ""))
        }
        dsl.echo "populate maven: ${maven} finished"
    }

    boolean isUi(String repo) {
        return (repo.endsWith("-ui")
                || buildType == BuildServiceType.NPM
                || buildType == BuildServiceType.SENCHA)
    }

    void setupBuildServiceType() {
        dsl.echo "setupBuildServiceType started"
        if (buildType != null) {
            dsl.echo "setupBuildServiceType manually provided, buildType: ${buildType}"
            return
        }
        if (revisionControlService.repo.endsWith("ui")) {
            buildType = BuildServiceType.NPM
        } else if (revisionControlService.repo.endsWith("service")) {
            buildType = BuildServiceType.MAVEN
        } else {
            if (buildType == null) {
                throw new UnrecognizedBuildServiceTypeException("type is null");
            }
        }
        dsl.echo "setupBuildServiceType finished, buildType: ${buildType}"
    }

    boolean isNpm() {
        if (buildType == BuildServiceType.NPM) {
            return true
        }
        return false
    }

    def setupBuildService() {
        dsl.echo "setupBuildService started buildType: ${buildType}, maven: ${maven.toString()}"

        if (buildType == BuildServiceType.MAVEN && suit == PipelineStagesSuit.SERVICE) {
            buildService = dockerMavenBuildService
        } else if (buildType == BuildServiceType.MAVEN && suit == PipelineStagesSuit.LIBRARY) {
            buildService = mavenBuildService
        } else if (buildType == BuildServiceType.MAVEN) {
            buildService = dockerMavenBuildService
        } else if (buildType == BuildServiceType.NPM) {
            dsl.echo "before build npm"
            npmBuildService = npm.build()
            dsl.echo "after build npm"
            buildService = npmBuildService
        } else if (buildType == BuildServiceType.NPM_DOCKER) {
            dockerNpmBuildService = new DockerNpmBuildService(dockerService, npm)
            buildService = npmBuildService
        } else if (buildType == BuildServiceType.SENCHA) {
            senchaService = new SenchaService()
            buildService = senchaService
        } else if (buildType == BuildServiceType.PHP) {
            buildService = new PhpBuildService(dockerService)
        } else if (buildType == BuildServiceType.PYTHON) {
            buildService = new PythonBuildService(dockerService)
        }

        buildService.useBuildNumberForVersion = useBuildNumberForVersion
        dsl.echo "buildService: ${buildService.toString()}"
        dsl.echo "setupBuildService finished"
    }

    void validate() throws PipelineException {
        validateRevisionControl(revisionControlBuilder)
    }

    void validateRevisionControl(RevisionControlBuilder revisionControlBuilder) throws InvalidRepositoryNameException {

    }

    FlowBuilder addPipelineStages(String pipelineStagesSuit) {
        suit = PipelineStagesSuit.fromString(pipelineStagesSuit)
        if (suit == PipelineStagesSuit.SERVICE) {
            return addDefaultPipelineStages()
        }
        if (suit == PipelineStagesSuit.LIBRARY) {
            return addReleaseCommonLibsPipelineStages()
        }
        throw new UnrecognizedPipelineStagesSuitException("can not find suit: ${pipelineStagesSuit}")
    }

    FlowBuilder addDefaultPipelineStages() {
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

    FlowBuilder addReleaseCommonLibsPipelineStages() {
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
            // TODO provide default, root cause of npe
            if (backEndAutoTestsServiceBuilder == null) return null
            if (suit == PipelineStagesSuit.LIBRARY) {
                if (sonarQubeService == null) {
                    return backEndAutoTestsServiceBuilder.buildLibrary();
                }
                return backEndAutoTestsServiceBuilder.buildLibrary(sonarQubeService)
            }
            if (sonarQubeService == null) {
                return backEndAutoTestsServiceBuilder.buildLibrary();
            }
            return backEndAutoTestsServiceBuilder.build(sonarQubeService)
        }
    }

    ReleaseService getReleaseService() {
        if (suit == PipelineStagesSuit.LIBRARY && buildType == BuildServiceType.MAVEN) {
            return new BackEndLibraryReleaseService(mavenBuildService, revisionControlService)
        }
        if (buildType == BuildServiceType.NPM || buildType == BuildServiceType.NPM_DOCKER
                || buildType == BuildServiceType.SENCHA) {
            return new UiReleaseService(buildService, revisionControlService)
        }
        if (buildType == BuildServiceType.PYTHON || buildType == BuildServiceType.PHP
                || buildType == BuildServiceType.PHP_PYTHON) {
            return new VcsAndDockerRelease(buildService, revisionControlService, dockerService)
        }
        return new BackEndReleaseService(mavenBuildService, revisionControlService, dockerService)
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

    FlowBuilder withUseBuildNumberForVersion(boolean useBuildNumberForVersion) {
        this.useBuildNumberForVersion = useBuildNumberForVersion
        return this
    }

    FlowBuilder withDocker(DockerBuilder dockerBuilder) {
        this.dockerBuilder = dockerBuilder
        return this
    }

    FlowBuilder addMaven(Maven maven) {
        this.maven = maven
        return this
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
        this.awsS3DeploymentService = s3Builder.build()
        return this
    }

    FlowBuilder withUiDeploymentType(String uiDeploymentTypeRowValue) {
        uiDeploymentType = UiDeploymentType.fromString(uiDeploymentTypeRowValue)
        return this
    }

    MavenBuildService buildMavenBuildService(Maven maven) {
        MavenBuildService mavenBuildService = new MavenBuildService()
        mavenBuildService.setJava(maven.getJava())
        mavenBuildService.setMaven(maven.getMaven())
        mavenBuildService.setMavenParams(maven.getMavenParams())
        mavenBuildService.setGroupId(maven.getGroupId())
        mavenBuildService.setArtifactId(maven.getArtifactId())
        mavenBuildService.setPackaging(maven.getPackaging())
        mavenBuildService.setRepositoryId(maven.getRepositoryId())
        mavenBuildService.setLayout(maven.getLayout())
        mavenBuildService.setRepoUrl(maven.getRepoUrl())
        return mavenBuildService
    }

    void populateServiceContextHolder() {
        ServiceContextHolder.addService(mavenBuildService)
        ServiceContextHolder.addService(dockerMavenBuildService)
        ServiceContextHolder.addService(npmBuildService)
        ServiceContextHolder.addService(senchaService)
        ServiceContextHolder.addService(buildService)
        ServiceContextHolder.addService(deploymentService)
        ServiceContextHolder.addService(autoTestsService)
        ServiceContextHolder.addService(revisionControlService)
        ServiceContextHolder.addService(RevisionControlService.class, revisionControlService)
        ServiceContextHolder.addService(releaseService)
        ServiceContextHolder.addService(dockerService)
        ServiceContextHolder.addService(slackService)
        ServiceContextHolder.addService(sonarQubeService)
        ServiceContextHolder.addService(swaggerService)
        ServiceContextHolder.addService(dockerNpmBuildService)
    }

    @Override
    public String toString() {
        return "FlowBuilder{" +
                "releaseInfo=" + releaseInfo +
                ", buildType=" + buildType +
                ", useBuildNumberForVersion=" + useBuildNumberForVersion +
                ", revisionControlBuilder=" + revisionControlBuilder +
                ", stageTypes=" + stageTypes +
                ", availableStages=" + availableStages +
                ", stageTypesToBeSkipped=" + stageTypesToBeSkipped +
                ", slackBuilder=" + slackBuilder +
                ", dockerBuilder=" + dockerBuilder +
                ", sonarQubeBuilder=" + sonarQubeBuilder +
                ", swaggerBuilder=" + swaggerBuilder +
                ", maven=" + maven +
                ", mavenBuildService=" + mavenBuildService +
                ", npmBuildService=" + npmBuildService +
                ", buildService=" + buildService +
                ", cdnDeploymentService=" + cdnDeploymentService +
                ", deploymentService=" + deploymentService +
                ", autoTestsService=" + autoTestsService +
                ", revisionControlService=" + revisionControlService +
                ", releaseService=" + releaseService +
                ", dockerService=" + dockerService +
                ", slackService=" + slackService +
                ", sonarQubeService=" + sonarQubeService +
                ", swaggerService=" + swaggerService +
                '}';
    }
}

