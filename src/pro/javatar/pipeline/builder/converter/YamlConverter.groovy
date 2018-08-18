package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.YamlModel
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlConverter {

    YamlModel toYamlModel(def yml) {
        YamlModel model = new YamlModel()
        populateNpm(model, yml)
        populateMaven(model, yml)
        populateDocker(model, yml)
        return model
    }

    def populateDocker(YamlModel model, def yml) {
        def docker = yml.docker
        dsl.echo "populateDocker: docker: ${docker}"
        model.setDocker()
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
