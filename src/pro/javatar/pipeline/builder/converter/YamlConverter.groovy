package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.Docker
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.YamlModel
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlConverter {

    YamlModel toYamlModel(def yml) {
        YamlModel model = new YamlModel()
        dsl.echo "model: ${model.toString()}"
        populateNpm(model, yml)
        dsl.echo "model: ${model.toString()}"
        populateMaven(model, yml)
        dsl.echo "model: ${model.toString()}"
        populateDocker(model, yml)
        dsl.echo "model: ${model.toString()}"
        return model
    }

    def populateDocker(YamlModel model, def yml) {
        def docker = yml.docker
        dsl.echo "populateDocker: docker: ${docker}"
        List<Docker> dockers = new ArrayList<>()
        docker.each{dockerItem -> dockers.add(retrieveDocker(dockerItem))}
        model.setDocker(dockers)
    }

    Docker retrieveDocker(def dockerItem) {
        dsl.echo "retrieveDocker: dockerItem: ${dockerItem}"
        Docker docker = new Docker()
                .withCredentialsId(dockerItem.credentialsId)
                .withRegistry(dockerItem.registry)
                .withEnv(retrieveEnvList(dockerItem.env))
        return docker
    }

    List<String> retrieveEnvList(def env) {
        dsl.echo "retrieveEnvList: env: ${env}"
        List<String> envList = new ArrayList<>()
        env.each{envItem -> envList.add(envItem)}
        return envList
    }

    def populateMaven(YamlModel model, def yml) {
        def maven = yml.maven
        dsl.echo "populateMaven: maven: ${maven}"
        model.setMaven(new Maven()
                .withRepositoryId(maven.repository.id)
                .withRepoUrl(maven.repository.url)
                .withMaven(maven.jenkins_tool.maven)
                .withJava(maven.jenkins_tool.java)
                .withMavenParams(maven.params)
        )
    }

    def populateNpm(YamlModel model, def yml) {
        def npm = yml.npm
        dsl.echo "populateNpm: npm: ${npm}"
        model.setNpm(new Npm()
                .withNpmType(npm.type)
                .withNpmVersion(npm.version)
                .withDistributionFolder(npm.distributionFolder)
                .withModuleRepository(npm.moduleRepository)
        )
    }

}
