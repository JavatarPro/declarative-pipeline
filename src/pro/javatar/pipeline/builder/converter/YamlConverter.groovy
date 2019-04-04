package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.AutoTest
import pro.javatar.pipeline.builder.model.CacheRequest
import pro.javatar.pipeline.builder.model.Docker
import pro.javatar.pipeline.builder.model.DockerRegistry
import pro.javatar.pipeline.builder.model.Environment
import pro.javatar.pipeline.builder.model.Gradle
import pro.javatar.pipeline.builder.model.JenkinsTool
import pro.javatar.pipeline.builder.model.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.model.Mesos
import pro.javatar.pipeline.builder.model.Nomad
import pro.javatar.pipeline.builder.model.NomadItem
import pro.javatar.pipeline.builder.model.Pipeline
import pro.javatar.pipeline.builder.model.S3
import pro.javatar.pipeline.builder.model.S3Repository
import pro.javatar.pipeline.builder.model.Service
import pro.javatar.pipeline.builder.model.Sonar
import pro.javatar.pipeline.builder.model.Ui
import pro.javatar.pipeline.builder.model.Vcs
import pro.javatar.pipeline.builder.model.VcsRepoTO
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.util.LogLevel
import pro.javatar.pipeline.util.Logger

import java.time.Period

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isNotBlank

class YamlConverter {

    YamlConfig toYamlModel(def yml) {
        Logger.info("YamlConverter:toYamlModel:started")
        YamlConfig result = new YamlConfig()
                .withLogLevel(retrieveAndSetLogLevel(yml))
                .withJenkinsTool(retrieveJenkinsTools(yml))
                .withVcs(retrieveVcs(yml))
                .withService(retrieveService(yml))
                .withPipeline(retrievePipeline(yml))
                .withNpm(retrieveNpm(yml))
                .withUi(retrieveUi(yml))
                .withS3(retrieveS3(yml))
                .withMaven(retrieveMaven(yml))
                .withGradle(retrieveGradle(yml))
                .withDocker(retrieveDocker(yml))
                .withMesos(retrieveMesos(yml))
                .withNomad(retrieveNomad(yml))
                .withSonar(retrieveSonar(yml))
                .withAutoTest(retrieveAutoTest(yml))
                .withCacheRequest(retrieveCacheRequest(yml))
                .populateServiceRepo()
        Logger.info("YamlConverter:toYamlModel:finished")
        Logger.debug("YamlConverter:toYamlModel:result: ${result}")
        return result
    }

    JenkinsTool retrieveJenkinsTools(yml) {
        def tool = yml.jenkins_tool
        dsl.echo "retrieveJenkinsTools: jenkins_tool: ${tool}"
        if (tool == null) {
            Logger.warn("jenkins_tool is not provided in configuration")
            return null
        }
        return new JenkinsTool()
                .withJava(tool.java)
                .withMaven(tool.maven)
                .withGradle(tool.gradle)
                .withNpmVersion(tool["npm.version"])
                .withNpmType(tool["npm.type"])
    }

    Service retrieveService(def yml) {
        def service = yml.service
        dsl.echo "retrieveService: service: ${service}"
        if (service == null) {
            return new Service()
        }
        return new Service()
                .withName(service.name)
                .withBuildType(service.buildType)
                .withUseBuildNumberForVersion(service.useBuildNumberForVersion)
                .withVcsRepoId(service.vcs_repo)
                .withOrchestration(service.orchestration)
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

    LogLevel retrieveAndSetLogLevel(def yml) {
        def log = yml.log_level
        dsl.echo "YamlConverter:retrieveAndSetLogLevel: logLevel: ${log}"
        if (log == null) {
            return
        }
        LogLevel logLevel = LogLevel.fromString(log)
        Logger.LEVEL = logLevel
        return logLevel
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
                .withRevisionControl(yml.vcs.revisionControl)
                .withRepo(retrieveVcsRepos(vcs))
                .populateRevisionControl()
    }

    Map<String, VcsRepoTO> retrieveVcsRepos(def vcs) {
        Map<String, VcsRepoTO> vcsRepos = new HashMap<>()
        def repos = vcs.repo
        repos.each { key, value ->
            Logger.trace("YamlConverter:retrieveVcsRepos: key: ${key}, value: ${value}")
            vcsRepos.put(key, retrieveVcsRepo(value))
        }
        Logger.trace("YamlConverter:retrieveVcsRepos:vcsRepos: ${vcsRepos.toString()}")
        return vcsRepos
    }

    VcsRepoTO retrieveVcsRepo(def vcsRepo) {
        VcsRepoTO result = new VcsRepoTO()
                .withName(vcsRepo.name)
                .withOwner(vcsRepo.owner)
                .withCredentialsId(vcsRepo.credentialsId)
                .withDomain(vcsRepo.domain)
                .withType(vcsRepo.type)
                .withBranch(vcsRepo.branch)
                .withRevisionControl(vcsRepo.revisionControl)
        Logger.trace("YamlConverter:retrieveVcsRepo:result: ${result.toString()}")
        return result
    }

    Pipeline retrievePipeline(def yml) {
        def pipeline = yml.pipeline
        if (pipeline == null) {
            return new Pipeline()
        }
        dsl.echo "retrievePipeline: pipeline: ${pipeline}"
        List<String> stages = new ArrayList<>()
        pipeline.stages.each{stage -> stages.add(stage)}
        return new Pipeline()
                .withPipelineSuit(pipeline.suit)
                .withStages(stages)
    }

    Docker retrieveDocker(def yml) {
        Logger.debug("YamlConverter:retrieveDocker:started")
        def docker = yml.docker
        if (docker == null) {
            Logger.debug("YamlConverter:retrieveDocker: docker is null stub will be returned")
            return new Docker()
        }
        Logger.debug("YamlConverter:retrieveDocker: docker: ${docker}")
        def dockerRegistries = yml["docker-registries"]
        Logger.debug("YamlConverter:retrieveDocker: dockerRegistries: ${dockerRegistries}")
        Map<String, DockerRegistry> dockerRegistryMap = new HashMap<>()
        dockerRegistries.each { String key, def value ->
            Logger.trace("YamlConverter:retrieveDocker: dockerRegistries.each: key: ${key}, value: ${value}")
            String credentialsId = value.credentialsId
            String registry = value.registry
            Logger.trace("YamlConverter:retrieveDocker: dockerRegistries.each: " +
                    "credentialsId: ${credentialsId}, registry: ${registry}")
            DockerRegistry dockerRegistry = new DockerRegistry(credentialsId, registry)
            Logger.trace("YamlConverter:retrieveDocker: dockerRegistries.each: key: ${key}, " +
                    "dockerRegistry: ${dockerRegistry}")
            dockerRegistryMap.put(key, dockerRegistry);
        }
        Logger.trace("YamlConverter:retrieveDocker: next line in logs does not appeared")
        Logger.debug("YamlConverter:retrieveDocker: dockerRegistryMap: ${dockerRegistryMap.size()}")

        Map<String, DockerRegistry> resultMap = new HashMap<>()
        docker.registries.each { String key, String value ->
            Logger.trace("YamlConverter:retrieveDocker:docker.registries.each: key: ${key}, value: ${value}")
            // Environment env = new Environment(key)
            DockerRegistry dockerRegistry = dockerRegistryMap.get(value)
            Logger.trace("YamlConverter:retrieveDocker:docker.registries.each: env: ${key}," +
                    " dockerRegistry: ${dockerRegistry}")
            resultMap.put(key, dockerRegistry)
        }

        Logger.debug("YamlConverter:retrieveDocker:resultMap: ${resultMap.size()}")

        Docker result = new Docker().withDockerRegistries(resultMap)
                .withCustomDockerFileName(docker.customDockerFileName)
        Logger.debug("YamlConverter:retrieveDocker:finished with result: ${result.toString()}")
        return result
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

    Gradle retrieveGradle(def yml) {
        def gradle = yml.gradle
        if (gradle == null) {
            return null
        }
        dsl.echo "retrieveGradle: gradle: ${gradle}"
        return new Gradle()
                .withRepositoryId(gradle.repository.id)
                .withRepositoryUrl(gradle.repository.url)
                .withParams(gradle.params)
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

    Nomad retrieveNomad(def yml) {
        def nomad = yml.nomad
        Logger.debug("YamlConverter:retrieveNomad: nomad: ${nomad}")
        if (nomad == null) {
            Logger.debug("nomad not provided")
            return null
        }
        Nomad result = new Nomad()
        nomad.each { String env, def nomadItem ->
            Period deploymentTimeout = isNotBlank(nomadItem.period) ? Period.parse(nomadItem.deploymentTimeout) : null
            Environment environment = new Environment(env)
            NomadItem item = new NomadItem()
                    .withUrl(nomadItem.url)
                    .withDeploymentTimeout(deploymentTimeout)
                    .withVcsConfig(nomadItem.vcsConfig)
            result.addNomadItem(environment, item)
        }
        Logger.debug("YamlConverter:retrieveNomad: result: ${result}")
        return result
    }

    Npm retrieveNpm(def yml) {
        def npm = yml.npm
        dsl.echo "retrieveNpm: npm: ${npm}"
        if (npm == null) {
            Logger.info("npm is null new Npm() will be returned")
            return new Npm()
        }
        return new Npm()
                .withNpmType(npm.type)
                .withNpmVersion(npm.version)
                .withDistributionFolder(npm.distributionFolder)
                .withModuleRepository(npm.moduleRepository)
    }

    Ui retrieveUi(def yml) {
        def ui = yml.ui
        dsl.echo "retrieveUi: ui: ${ui}"
        if (ui == null) {
            Logger.info("ui is null new Ui() will be returned")
            return new Ui()
        }
        Logger.debug("YamlConverter:retrieveUi:ui:withDeploymentType")
        return new Ui().withDeploymentType(ui.deploymentType)
    }

    S3 retrieveS3(def yml) {
        def s3 = yml.s3
        Logger.debug("YamlConverter:retrieveS3: s3: ${s3}")
        if (s3 == null) {
            Logger.debug("YamlConverter:retrieveS3: new S3()")
            return new S3()
        }
        def s3Repositories = yml["s3-repositories"]
        Logger.debug("YamlConverter:retrieveS3: s3Repositories: ${s3Repositories}")
        Map<String, S3Repository> repositoryMap = new HashMap<>()
        s3Repositories.each { String key, def value ->
            repositoryMap.put(key, new S3Repository()
                    .withCredentialsId(value.credentialsId)
                    .withBucket(value.bucket)
                    .withRegion(value.region))
        }
        Logger.debug("YamlConverter:retrieveS3: repositoryMap: ${repositoryMap}")
        Map<String, S3Repository> resultMap = new HashMap<>()
        s3.repositories.each { String key, String value ->
            resultMap.put(key, repositoryMap.get(value))
        }
        Logger.debug("YamlConverter:retrieveS3: resultMap: ${resultMap}")
        S3 result = new S3().withS3Repositories(resultMap)
        Logger.trace("YamlConverter:retrieveS3: result: ${result}")
        return result
    }

}
