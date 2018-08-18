package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.YamlModel

class YamlConverter {

    YamlModel toYamlModel(def yml) {
        YamlModel model = new YamlModel()
        populateNpm(model, yml)
        return model
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
