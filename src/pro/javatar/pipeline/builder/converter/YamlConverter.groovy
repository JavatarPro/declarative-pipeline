package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.AutoTest
import pro.javatar.pipeline.builder.model.CacheRequest
import pro.javatar.pipeline.builder.model.Docker
import pro.javatar.pipeline.builder.model.DockerRegistry
import pro.javatar.pipeline.builder.model.Environment
import pro.javatar.pipeline.builder.model.JenkinsTool
import pro.javatar.pipeline.builder.model.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.model.Mesos
import pro.javatar.pipeline.builder.model.Pipeline
import pro.javatar.pipeline.builder.model.S3
import pro.javatar.pipeline.builder.model.S3Repository
import pro.javatar.pipeline.builder.model.Service
import pro.javatar.pipeline.builder.model.Sonar
import pro.javatar.pipeline.builder.model.Ui
import pro.javatar.pipeline.builder.model.Vcs
import pro.javatar.pipeline.builder.model.VcsRepoTO
import pro.javatar.pipeline.builder.model.YamlConfig
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlConverter {

    YamlConfig toYamlModel(def yml) {
        return new YamlConfig()
                .withJenkinsTool(retrieveJenkinsTools(yml))
                .withVcs(retrieveVcs(yml))
                .withService(retrieveService(yml))
                .withPipeline(retrievePipeline(yml))
                .withNpm(retrieveNpm(yml))
                .withUi(retrieveUi(yml))
                .withS3(retrieveS3(yml))
                .withMaven(retrieveMaven(yml))
                .withDocker(retrieveDocker(yml))
                .withOrchestrationService(yml.orchestrationService)
                .withMesos(retrieveMesos(yml))
                .withSonar(retrieveSonar(yml))
                .withAutoTest(retrieveAutoTest(yml))
                .withCacheRequest(retrieveCacheRequest(yml))
                .populateServiceRepo()
    }

    JenkinsTool retrieveJenkinsTools(yml) {
        def tool = yml.jenkins_tool
        if (tool == null) return null
        dsl.echo "retrieveJenkinsTools: jenkins_tool: ${tool}"
        return new JenkinsTool()
                .withJava(tool.java)
                .withMaven(tool.maven)
                .withNpmVersion(tool["npm.version"])
                .withNpmType(tool["npm.type"])
    }

    Service retrieveService(def yml) {
        def service = yml.service
        dsl.echo "retrieveService: service: ${service}"
        if (service == null) return new Service()
        return new Service()
                .withName(service.name)
                .withBuildType(service.buildType)
                .withUseBuildNumberForVersion(service.useBuildNumberForVersion)
                .withVcsRepoId(service.vcs_repo)
    }

    AutoTest retrieveAutoTest(def yml) {
        def autoTest = yml["auto-test"]
        if (autoTest == null) return null
        dsl.echo "retrieveAutoTest: autoTest: ${autoTest}"
        return new AutoTest()
                .withJobName(autoTest.jobName)
                .withSkipCodeQualityVerification(autoTest.skipCodeQualityVerification)
                .withSkipSystemTests(autoTest.skipSystemTests)
                .withSleepInSeconds(autoTest.sleepInSeconds)
    }

    CacheRequest retrieveCacheRequest(def yml) {
        def cache = yml.cache
        dsl.echo "retrieveCacheRequest: cache: ${cache}"
        Map<String, List<String>> cacheMap = new HashMap<>()
        cache.each {String service, List<String> folders ->
            List<String> folderList = new ArrayList<>()
            folders.each{folder -> folderList.add(folder)}
            cacheMap.put(service, folderList)
        }
        return new CacheRequest().withCaches(cacheMap)
    }

    Sonar retrieveSonar(def yml) {
        def sonar = yml.sonar
        dsl.echo "retrieveSonar: sonar: ${sonar}"
        if (sonar == null) {
            dsl.echo "empty sonar will be returned"
            return new Sonar()
        }
        return new Sonar()
                .withEnabled(sonar.enabled)
                .withServerUrl(sonar.serverUrl)
                .withQualityGateEnabled(sonar.qualityGateEnabled)
                .withQualityGateSleepInSeconds(sonar.qualityGateSleepInSeconds)
                .withJenkinsSettingsName(sonar.jenkinsSettingsName)
                .withParams(sonar.params)
    }

    Vcs retrieveVcs(def yml) {
        def vcs = yml.vcs
        if (vcs == null) return new Vcs()
        dsl.echo "retrieveVcsRepo: vcs: ${vcs}"
        return new Vcs()
                .withRevisionControl(yml.revisionControl)
                .withRepo(retrieveVcsRepos(vcs))
                .populateRevisionControl()
    }

    Map<String, VcsRepoTO> retrieveVcsRepos(def vcs) {
        Map<String, VcsRepoTO> vcsRepos = new HashMap<>()
        def repos = vcs.repo
        repos.each { key, value -> vcsRepos.put(key, retrieveVcsRepo(value)) }
        return vcsRepos
    }

    VcsRepoTO retrieveVcsRepo(def vcsRepo) {
        return new VcsRepoTO()
                .withName(vcsRepo.name)
                .withOwner(vcsRepo.owner)
                .withCredentialsId(vcsRepo.credentialsId)
                .withDomain(vcsRepo.domain)
                .withType(vcsRepo.type)
                .withBranch(vcsRepo.branch)
                .withRevisionControl(vcsRepo.revisionControl)
    }

    Pipeline retrievePipeline(def yml) {
        def pipeline = yml.pipeline
        if (pipeline == null) return new Pipeline()
        dsl.echo "retrievePipeline: pipeline: ${pipeline}"
        List<String> stages = new ArrayList<>()
        pipeline.stages.each{stage -> stages.add(stage)}
        return new Pipeline()
                .withPipelineSuit(pipeline.pipelineSuit)
                .withStages(stages)
    }

    Docker retrieveDocker(def yml) {
        def docker = yml.docker
        if (docker == null) return Collections.emptyList()
        dsl.echo "retrieveDocker: docker: ${docker}"
        def dockerRegistries = yml["docker-registries"]
        dsl.echo "retrieve: dockerRegistries: ${dockerRegistries}"
        Map<String, DockerRegistry> dockerRegistryMap = new HashMap<>()
        dockerRegistries.each{ String key, def value ->
            dockerRegistryMap.put(key, new DockerRegistry()
                    .withCredentialsId(value.credentialsId)
                    .withRegistry(value.registry))
        }
        Map<Environment, DockerRegistry> resultMap = new HashMap<>()
        docker.registries.each { String key, String value ->
            resultMap.put(new Environment(key), dockerRegistryMap.get(value))
        }
        return new Docker()
                .withDockerRegistries(resultMap)
                .withCustomDockerFileName(docker.customDockerFileName)
    }

    List<String> retrieveEnvList(def env) {
        dsl.echo "retrieveEnvList: env: ${env}"
        List<String> envList = new ArrayList<>()
        env.each{envItem -> envList.add(envItem)}
        return envList
    }

    Maven retrieveMaven(def yml) {
        def maven = yml.maven
        if (maven == null) return null
        dsl.echo "retrieveMaven: maven: ${maven}"
        return new Maven()
                .withRepositoryId(maven.repository.id)
                .withRepositoryUrl(maven.repository.url)
                .withParams(maven.params)
    }

    Mesos retrieveMesos(def yml) {
        def mesos = yml.mesos
        if (mesos == null) return null
        dsl.echo "retrieveMesos: mesos: ${mesos}"
        def vcsConfigRepos = mesos.vcsConfigRepos
        dsl.echo "retrieveMesos: vcsConfigRepos: ${vcsConfigRepos}"
        Mesos result = new Mesos()
        Map<String, VcsRepoTO> vcsRepos = new HashMap<>()
        Map<String, VcsRepoTO> vcsRepoMap = retrieveVcs(yml).getRepo()
        dsl.echo "retrieveMesos: vcsRepoMap: ${vcsRepoMap}"
        vcsConfigRepos.each { key, value -> vcsRepos.put(key, vcsRepoMap.get(value)) }
        result.setVcsConfigRepos(vcsRepos)
        dsl.echo "retrieveMesos: result: ${result}"
        return result
    }

    Npm retrieveNpm(def yml) {
        def npm = yml.npm
        if (npm == null) return new Npm()
        dsl.echo "retrieveNpm: npm: ${npm}"
        return new Npm()
                .withNpmType(npm.type)
                .withNpmVersion(npm.version)
                .withDistributionFolder(npm.distributionFolder)
                .withModuleRepository(npm.moduleRepository)
    }

    Ui retrieveUi(def yml) {
        def ui = yml.ui
        if (ui == null) return new Ui()
        dsl.echo "retrieveUi: ui: ${ui}"
        return new Ui().withDeploymentType(ui.deploymentType)
    }

    S3 retrieveS3(def yml) {
        def s3 = yml.s3
        if (s3 == null) return Collections.emptyList()
        dsl.echo "retrieveS3: s3: ${s3}"
        def s3Repositories = yml["s3-repositories"]
        dsl.echo "retrieveS3: s3Repositories: ${s3Repositories}"
        Map<String, S3Repository> repositoryMap = new HashMap<>()
        s3Repositories.each { String key, def value ->
            repositoryMap.put(key, new S3Repository()
                    .withCredentialsId(value.credentialsId)
                    .withBucket(value.bucket)
                    .withRegion(value.region))
        }
        Map<String, S3Repository> resultMap = new HashMap<>()
        s3.repositories.each { String key, String value ->
            resultMap.put(key, repositoryMap.get(value))
        }
        return new S3().withS3Repositories(resultMap)
    }

}
