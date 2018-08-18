package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.model.YamlFile

class FlowBuilderConverter {

    FlowBuilder toFlowBuilder(YamlFile yamlFile) {
        return new FlowBuilder()
                .addMaven(toMaven(yamlFile))
                .withDocker(toDockerBuilder(yamlFile))
    }

    Maven toMaven(YamlFile yamlFile) {
        def mvn = yamlFile.getMaven()
//        def jenkinsTools
        return new Maven()
                .withMavenParams(mvn.getParams())
                .withRepositoryId(mvn.getRepositoryId())
                .withRepoUrl(mvn.getRepositoryUrl())
    }

    DockerBuilder toDockerBuilder(YamlFile yamlFile) {
        def docker = yamlFile.docker.get(0)
        return new DockerBuilder()
                .withDockerDevRepo(docker.getRegistry())
                .withDockerCredentialsId(docker.getCredentialsId())
    }

}
