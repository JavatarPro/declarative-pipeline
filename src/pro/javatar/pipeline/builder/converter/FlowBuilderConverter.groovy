package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.BackEndAutoTestsServiceBuilder
import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.RevisionControlBuilder
import pro.javatar.pipeline.builder.S3Builder
import pro.javatar.pipeline.builder.model.DockerRegistry
import pro.javatar.pipeline.builder.model.Mesos
import pro.javatar.pipeline.builder.model.S3
import pro.javatar.pipeline.builder.model.S3Repository
import pro.javatar.pipeline.builder.model.VcsRepoTO
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.model.DockerOrchestrationServiceType
import pro.javatar.pipeline.model.RevisionControlType
import pro.javatar.pipeline.model.VcsRepositoryType
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.MesosService
import pro.javatar.pipeline.service.vcs.model.VcsRepo

import static pro.javatar.pipeline.util.Utils.isEmpty
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class FlowBuilderConverter {

    FlowBuilder toFlowBuilder(YamlConfig yamlFile) {
        return new FlowBuilder()
                .withServiceName(yamlFile.getService().getName())
                .withBuildType(yamlFile.getService().getBuildType())
                .withUseBuildNumberForVersion(yamlFile.getService().getUseBuildNumberForVersion())
                .addPipelineStages(yamlFile.getPipeline().getPipelineSuit())
                .addPipelineStages(yamlFile.getPipeline().getStages())
                .addMaven(toMaven(yamlFile))
                .addNpm(yamlFile.getNpm())
                .withUiDeploymentType(yamlFile.getUi().getDeploymentType())
                .withS3(toS3Builder(yamlFile))
                .withRevisionControl(toRevisionControlBuilder(yamlFile))
                .withDocker(toDockerBuilder(yamlFile))
                .withCacheRequest(yamlFile.getCacheRequest())
                .withBackEndAutoTestsServiceBuilder(toBackEndAutoTestsServiceBuilder(yamlFile))
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
        def jenkinsTools = yamlFile.getJenkinsTool()
        return new Maven()
                .withMavenParams(mvn.getParams())
                .withRepositoryId(mvn.getRepositoryId())
                .withRepoUrl(mvn.getRepositoryUrl())
                .withJava(jenkinsTools.getJava())
                .withMaven(jenkinsTools.getMaven())
    }

    DockerBuilder toDockerBuilder(YamlConfig yamlFile) {
        Map<String, DockerRegistry> dockerRegistryMap = yamlFile.getDocker().getDockerRegistries()
        DockerBuilder builder = new DockerBuilder()

        dockerRegistryMap.each { String key, DockerRegistry value ->
            builder.withDockerRegistry(key, value.getCredentialsId(), value.getRegistry())
        }
        return builder.withCustomDockerFileName(yamlFile.getDocker().getCustomDockerFileName())
                .withOrchestrationService(toOrchestrationService(yamlFile))
    }

    DockerOrchestrationService toOrchestrationService(YamlConfig yamlFile) {
        dsl.echo "INFO: FlowBuilderConverter: toOrchestrationService: started"
        String type = yamlFile.getOrchestrationService()
        if (isEmpty(type)) {
            return null
        }
        DockerOrchestrationServiceType orchestrationServiceType = DockerOrchestrationServiceType.fromString(type)
        if (orchestrationServiceType == DockerOrchestrationServiceType.MESOS) {
            return toMesosService(yamlFile)
        }
    }

    MesosService toMesosService(YamlConfig yamlFile) {
        dsl.echo "INFO: FlowBuilderConverter: toMesosService: started"
        MesosService mesosService = new MesosService()
        Mesos mesos = yamlFile.getMesos()
        Map<String, VcsRepo> vcsRepoMap = toVcsRepoMap(mesos.getVcsConfigRepos())
        mesosService.setVcsRepoMap(vcsRepoMap)
        yamlFile.getMesos()
        dsl.echo "INFO: FlowBuilderConverter: toMesosService: finished"
        return mesosService
    }

    Map<String, VcsRepo> toVcsRepoMap(Map<String, VcsRepoTO> vcsRepoToMap) {
        dsl.echo "toVcsRepoMap vcsRepoToMap: ${vcsRepoToMap}"
        Map<String, VcsRepo> result = new HashMap<>()
        vcsRepoToMap.each { key, value -> result.put(key, toVcsRepo(value)) }
        return result
    }

    VcsRepo toVcsRepo(VcsRepoTO vcsRepoTO) {
        dsl.echo "toVcsRepo vcsRepoTO: ${vcsRepoTO.toString()}"
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
        S3 s3 = yamlFile.getS3()
        Map<String, S3Repository> s3RepositoryMap = s3.getS3Repositories()
        S3Builder builder = new S3Builder()
        s3RepositoryMap.each { String key, S3Repository value ->
            builder.addS3Repository(key, value.getRegion(), value.getCredentialsId(),
                    value.getBucket(), value.getEnvFolder())
        }
        return builder
    }

}
