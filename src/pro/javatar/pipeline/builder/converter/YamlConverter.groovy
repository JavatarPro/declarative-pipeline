package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.Docker
import pro.javatar.pipeline.builder.model.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.model.YamlFile
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlConverter {

    YamlFile toYamlModel(def yml) {
        YamlFile model = new YamlFile()
        dsl.echo "model: ${model.toString()}"
        populateNpm(model, yml)
        dsl.echo "model: ${model.toString()}"
        populateMaven(model, yml)
        dsl.echo "model: ${model.toString()}"
        populateDocker(model, yml)
        dsl.echo "model: ${model.toString()}"
        return model
    }

    def populateDocker(YamlFile model, def yml) {
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

    def populateMaven(YamlFile model, def yml) {
        def maven = yml.maven
        dsl.echo "populateMaven: maven: ${maven}"
        model.setMaven(new Maven()
                .withRepositoryId(maven.repository.id)
                .withRepositoryUrl(maven.repository.url)
                .withParams(maven.params)
        )
    }

    def populateNpm(YamlFile model, def yml) {
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
