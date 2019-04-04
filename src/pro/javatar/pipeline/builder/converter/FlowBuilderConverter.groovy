package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.BackEndAutoTestsServiceBuilder
import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.RevisionControlBuilder
import pro.javatar.pipeline.builder.S3Builder
import pro.javatar.pipeline.builder.model.DockerRegistry
import pro.javatar.pipeline.builder.SonarQubeBuilder
import pro.javatar.pipeline.builder.model.Mesos
import pro.javatar.pipeline.builder.model.Nomad
import pro.javatar.pipeline.builder.model.S3
import pro.javatar.pipeline.builder.model.S3Repository
import pro.javatar.pipeline.builder.model.VcsRepoTO
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.model.DockerOrchestrationServiceType
import pro.javatar.pipeline.model.RevisionControlType
import pro.javatar.pipeline.model.VcsRepositoryType
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.MesosService
import pro.javatar.pipeline.service.orchestration.NomadService
import pro.javatar.pipeline.service.orchestration.model.NomadBO
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.isBlank

class FlowBuilderConverter {

    FlowBuilder toFlowBuilder(YamlConfig yamlFile) {
        return new FlowBuilder()
                .withServiceName(yamlFile.getService().getName())
                .withBuildType(yamlFile.getService().getBuildType())
                .withUseBuildNumberForVersion(yamlFile.getService().getUseBuildNumberForVersion())
                .addPipelineStages(yamlFile.getPipeline().getPipelineSuit())
                .addPipelineStages(yamlFile.getPipeline().getStages())
                .addMaven(toMaven(yamlFile))
                .addGradle(yamlFile.getGradle())
                .addJenkinsTool(yamlFile.getJenkinsTool())
                .addNpm(yamlFile.getNpm())
                .withUiDeploymentType(yamlFile.getUi().getDeploymentType())
                .withS3(toS3Builder(yamlFile))
                .withRevisionControl(toRevisionControlBuilder(yamlFile))
                .withDocker(toDockerBuilder(yamlFile))
                .withCacheRequest(yamlFile.getCacheRequest())
                .withBackEndAutoTestsServiceBuilder(toBackEndAutoTestsServiceBuilder(yamlFile))
                .withSonar(toSonar(yamlFile))
    }

    BackEndAutoTestsServiceBuilder toBackEndAutoTestsServiceBuilder(YamlConfig yamlConfig) {
        def autoTests = yamlConfig.getAutoTest()
        return new BackEndAutoTestsServiceBuilder()
                .withJobName(autoTests.jobName)
                .withSkipCodeQualityVerification(autoTests.skipCodeQualityVerification)
                .withSkipSystemTests(autoTests.skipSystemTests)
                .withSleepInSeconds(autoTests.sleepInSeconds)
    }

    Maven toMaven(YamlConfig yamlFile) {
        def mvn = yamlFile.getMaven()
        if (mvn == null) {
            return new Maven()
        }
        def jenkinsTools = yamlFile.getJenkinsTool()
        return new Maven()
                .withMavenParams(mvn.getParams())
                .withRepositoryId(mvn.getRepositoryId())
                .withRepoUrl(mvn.getRepositoryUrl())
                .withJava(jenkinsTools.getJava())
                .withMaven(jenkinsTools.getMaven())
    }

    DockerBuilder toDockerBuilder(YamlConfig yamlFile) {
        Logger.debug("FlowBuilderConverter:toDockerBuilder:started")
        Map<String, DockerRegistry> dockerRegistryMap = yamlFile.getDocker().getDockerRegistries()

        Logger.debug("FlowBuilderConverter:toDockerBuilder:dockerRegistryMap: ${dockerRegistryMap}")
        DockerBuilder builder = new DockerBuilder()

        dockerRegistryMap.each { String key, DockerRegistry value ->
            builder.addDockerRegistry(key, value.getCredentialsId(), value.getRegistry())
        }

        builder.withCustomDockerFileName(yamlFile.getDocker().getCustomDockerFileName())
                .withOrchestrationService(toOrchestrationService(yamlFile))

        Logger.debug("FlowBuilderConverter:toDockerBuilder:finished builder: ${builder}")
        return builder
    }

    DockerOrchestrationService toOrchestrationService(YamlConfig yamlFile) {
        Logger.info("FlowBuilderConverter: toOrchestrationService: started")
        String type = yamlFile.getOrchestrationService()
        if (isBlank(type)) {
            return null
        }
        DockerOrchestrationServiceType orchestrationServiceType = DockerOrchestrationServiceType.fromString(type)
        if (orchestrationServiceType == DockerOrchestrationServiceType.MESOS) {
            return toMesosService(yamlFile)
        }
        if (orchestrationServiceType == DockerOrchestrationServiceType.NOMAD) {
            return toNomadService(yamlFile)
        }

    }

    MesosService toMesosService(YamlConfig yamlFile) {
        Logger.info("FlowBuilderConverter: toMesosService: started")
        Mesos mesos = yamlFile.getMesos()
        if (mesos == null) {
            return null
        }
        MesosService mesosService = new MesosService()
        Map<String, VcsRepo> vcsRepoMap = toVcsRepoMap(mesos.getVcsConfigRepos())
        mesosService.setVcsRepoMap(vcsRepoMap)
        yamlFile.getMesos()
        Logger.info("FlowBuilderConverter: toMesosService: finished")
        return mesosService
    }

    NomadService toNomadService(YamlConfig yamlFile) {
        Logger.info("FlowBuilderConverter:toNomadService: started")
        Nomad nomad = yamlFile.getNomad()
        if (nomad == null) {
            return null
        }
        Map<String, NomadBO> nomadConfig = new HashMap<>()
        Map<String, VcsRepo> repos = toVcsRepoMap(yamlFile.getVcs().getRepo())
        Logger.info("FlowBuilderConverter:toNomadService: repos: ${repos.size()}")
        Logger.info("FlowBuilderConverter:toNomadService: nomad.getNomadConfig().size(): ${nomad.getNomadConfig().size()}")
        nomad.getNomadConfig().each { env, nomadItem ->
            Logger.trace("FlowBuilderConverter:toNomadService:nomad.nomadConfig.each: " +
                    "env: ${env.toString()}, nomadItem: ${nomadItem.toString()}")
            def vcsRepo = repos.get(nomadItem.getVcsConfig())
            Logger.trace("FlowBuilderConverter:toNomadService:nomad.nomadConfig.each: vcsRepo: ${vcsRepo.toString()}")
            NomadBO nomadBO = new NomadBO()
                    .withUrl(nomadItem.getUrl())
                    .withEnv(env)
                    .withVcsRepo(vcsRepo)
                    .withPeriod(nomadItem.getPeriod())
            Logger.trace("FlowBuilderConverter:toNomadService:nomad.nomadConfig.each: " +
                    "env: ${env.toString()}, nomadBO: ${nomadBO.toString()}")
            nomadConfig.put(env, nomadBO)
        }
        NomadService nomadService = new NomadService(nomadConfig)
        Logger.trace("FlowBuilderConverter:toNomadService: nomadService: ${nomadService.toString()}")
        Logger.info("FlowBuilderConverter:toNomadService: finished")
        return nomadService
    }

    Map<String, VcsRepo> toVcsRepoMap(Map<String, VcsRepoTO> vcsRepoToMap) {
        Logger.info("FlowBuilderConverter:toVcsRepoMap vcsRepoToMap: {}", vcsRepoToMap)
        Map<String, VcsRepo> result = new HashMap<>()
        vcsRepoToMap.each { key, value -> result.put(key, toVcsRepo(value)) }
        Logger.info("FlowBuilderConverter:toVcsRepoMap result: ${result.size()}")
        return result
    }

    VcsRepo toVcsRepo(VcsRepoTO vcsRepoTO) {
        if (vcsRepoTO == null) {
            return null
        }
        Logger.info("FlowBuilderConverter:toVcsRepo vcsRepoTO: ${vcsRepoTO.toString()}")
        return new VcsRepo()
                .withCredentialsId(vcsRepoTO.getCredentialsId())
                .withType(VcsRepositoryType.fromString(vcsRepoTO.getType()))
                .withRevisionControlType(RevisionControlType.fromString(vcsRepoTO.getRevisionControl()))
                .withDomain(vcsRepoTO.getDomain())
                .withName(vcsRepoTO.getName())
                .withOwner(vcsRepoTO.getOwner())
                .withBranch(vcsRepoTO.getBranch())
    }

    RevisionControlBuilder toRevisionControlBuilder(YamlConfig yamlFile) {
        def repo = yamlFile.getService().getRepo()
        return new RevisionControlBuilder()
                .withRepo(repo.getName())
                .withRepoOwner(repo.getOwner())
                .withRevisionControlType(repo.getRevisionControl())
                .withVcsRepositoryType(repo.getType())
                .withFlowPrefix(null)
                .withCredentialsId(repo.getCredentialsId())
                .withDomain(repo.getDomain())
                .withBranch(repo.getBranch())
    }

    S3Builder toS3Builder(YamlConfig yamlFile) {
        Logger.debug("FlowBuilderConverter:toS3Builder started")
        S3 s3 = yamlFile.getS3()
        if (s3 == null) {
            return null
        }
        Map<String, S3Repository> s3RepositoryMap = s3.getS3Repositories()
        S3Builder builder = new S3Builder()
        s3RepositoryMap.each { String key, S3Repository value ->
            builder.addS3Repository(key, value.getRegion(), value.getCredentialsId(),
                    value.getBucket(), value.getEnvFolder())
        }
        Logger.debug("FlowBuilderConverter:toS3Builder finished")
        Logger.trace("FlowBuilderConverter:toS3Builder builder: ${builder}")
        return builder
    }

    SonarQubeBuilder toSonar(YamlConfig yamlFile) {
        def sonar = yamlFile.getSonar()
        return new SonarQubeBuilder()
                .withEnabled(sonar.getEnabled())
                .withServerUrl(sonar.getServerUrl())
                .withQualityGateEnabled(sonar.getQualityGateEnabled())
                .withQualityGateSleepInSeconds(sonar.getQualityGateSleepInSeconds())
                .withJenkinsSettingsName(sonar.getJenkinsSettingsName())
                .withParams(sonar.getParams())
    }

}
