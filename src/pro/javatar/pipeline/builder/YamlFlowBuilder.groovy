package pro.javatar.pipeline.builder

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.converter.FlowBuilderConverter
import pro.javatar.pipeline.builder.converter.JenkinsBuildParamsConverter
import pro.javatar.pipeline.builder.converter.YamlConverter
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.service.PipelineDslHolder
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlFlowBuilder {

    static final DEFAULT_CONFIG_FILE = "javatar-declarative-pipeline.yml"

    String configFile

    YamlConverter yamlConverter = new YamlConverter()

    FlowBuilderConverter flowBuilderConverter = new FlowBuilderConverter()

    JenkinsBuildParamsConverter jenkinsBuildParamsConverter = new JenkinsBuildParamsConverter()

    YamlFlowBuilder(def dsl) {
        this(dsl, DEFAULT_CONFIG_FILE)
    }

    YamlFlowBuilder(def dsl, String configFile) {
        PipelineDslHolder.dsl = dsl
        this.configFile = configFile
    }

    Flow build() {
        dsl.echo "YamlFlowBuilder used configFile: ${configFile}"
        def yamlConfiguration = dsl.readTrusted configFile
        dsl.echo "yamlConfiguration: ${yamlConfiguration}"
        YamlConfig model = getYamlModelUsingJenkinsReadYamlCommand(yamlConfiguration)
        dsl.echo "YamlConfig: ${model.toString()}"
        FlowBuilder flowBuilder = flowBuilderConverter.toFlowBuilder(model)
        dsl.echo "flowBuilder: ${flowBuilder.toString()}"
        return flowBuilder.build()
    }

    YamlConfig getYamlModelUsingJenkinsReadYamlCommand(def yamlConfiguration) {
        def properties = dsl.readYaml text: yamlConfiguration
        jenkinsBuildParamsConverter.populateWithJenkinsBuildParams(properties)
        return yamlConverter.toYamlModel(properties)
    }

    @Override
    public String toString() {
        return "YamlFlowBuilder{" +
                "configFile='" + configFile + '\'' +
                '}';
    }
}
