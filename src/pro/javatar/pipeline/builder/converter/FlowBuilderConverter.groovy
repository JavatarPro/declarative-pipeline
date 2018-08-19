package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.RevisionControlBuilder
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.model.RevisionControlType
import pro.javatar.pipeline.model.VcsRepositoryType

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

    DockerBuilder toDockerBuilder(YamlConfig yamlFile) {
        // TODO add docker list to docker service
        def docker = yamlFile.docker.get(0)
        return new DockerBuilder()
                .withDockerDevRepo(docker.getRegistry())
                .withDockerCredentialsId(docker.getCredentialsId())
    }

    RevisionControlBuilder toRevisionControlBuilder(YamlConfig yamlFile) {
        def repo = yamlFile.getService().getRepo()
        return new RevisionControlBuilder()
                .withRepo(repo.getName())
                .withRepoOwner(repo.getOwner())
                .withRevisionControlType(RevisionControlType.fromString(repo.getRevisionControl()))
                .withVcsRepositoryType(VcsRepositoryType.fromString(repo.getType()))
                .withFlowPrefix(null)
                .withCredentialsId(repo.getCredentialsId())
                .withDomain(repo.getDomain())
                .withBranch(repo.getBranch())
    }

}
