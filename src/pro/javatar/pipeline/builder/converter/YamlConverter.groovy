package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.YamlModel

class YamlConverter {

    YamlModel toYamlModel(def yml) {
        YamlModel model = new YamlModel()
        populateNpm(model, yml)
        populateMaven(model, yml)
        return model
    }

    def populateMaven(YamlModel model, def yml) {
        def maven = yml.maven
        model.setNpm(new Maven()
                .withRepositoryId(maven.repository.id)
                .withRepoUrl(maven.repository.url)
//                .withMaven(maven.jenkins_tools.maven)
//                .withJava(maven.jenkins_tools.java)
                .withMavenParams(maven.params)
        )
    }

    def populateNpm(YamlModel model, def yml) {
        def npm = yml.npm
        model.setNpm(new Npm()
                .withNpmType(npm.type)
                .withNpmVersion(npm.version)
                .withDistributionFolder(npm.distributionFolder)
                .withModuleRepository(npm.moduleRepository)
        )
    }

}
