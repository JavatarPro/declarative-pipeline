package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.RevisionControlBuilder
import pro.javatar.pipeline.builder.S3Builder
import pro.javatar.pipeline.builder.model.Docker
import pro.javatar.pipeline.builder.model.S3
import pro.javatar.pipeline.builder.model.YamlConfig

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

    // TODO refactor DockerBuilder env coupling
    DockerBuilder toDockerBuilder(YamlConfig yamlFile) {
        def docker = yamlFile.getDocker()
        Map<String, Docker> dockerMap = new HashMap<>()
        DockerBuilder builder = new DockerBuilder()
        docker.each { it ->
            it.env.each {envItem -> dockerMap.put(envItem, it)}
        }
        Docker prod = dockerMap.get("prod")
        Docker dev = dockerMap.get("dev")
        return builder.withDockerDevRepo(dev.getRegistry())
                .withDockerRepo(prod.getRegistry())
                .withDockerDevCredentialsId(dev.getCredentialsId())
                .withDockerProdCredentialsId(prod.getCredentialsId())
                .withDockerOrchestrationService(yamlFile.getOrchestrationService())
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

    // TODO refactor S3Builder env coupling
    S3Builder toS3Builder(YamlConfig yamlFile) {
        def s3 = yamlFile.getS3()
        Map<String, S3> s3Map = new HashMap<>()
        S3Builder builder = new S3Builder()
        s3.each { it ->
            it.env.each {envItem -> s3Map.put(envItem, it)}
        }
        S3 prod = s3Map.get("prod")
        return builder.withRegion(prod.getRegion())
                .withCredentials(prod.getCredentialsId())
                .withBucketDev(s3Map.get("dev").getBucket())
                .withBucketProd(prod.getBucket())
    }

}
