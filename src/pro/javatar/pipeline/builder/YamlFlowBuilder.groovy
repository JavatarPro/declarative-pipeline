package pro.javatar.pipeline.builder

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature

@Grab('org.yaml:snakeyaml:1.21')
import org.yaml.snakeyaml.Yaml
@Grab('com.fasterxml.jackson.core:jackson-databind:2.9.6')
@Grab('com.fasterxml.jackson.core:jackson-annotations:2.9.6')
import com.fasterxml.jackson.databind.ObjectMapper;
@Grab('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.6')
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.converter.FlowBuilderConverter
import pro.javatar.pipeline.builder.converter.YamlConverter
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.service.PipelineDslHolder
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlFlowBuilder {

    static final DEFAULT_CONFIG_FILE = "javatar-declarative-pipeline.yml"

    String configFile

    YamlConverter yamlConverter = new YamlConverter()

    FlowBuilderConverter flowBuilderConverter = new FlowBuilderConverter()

    YamlFlowBuilder(def dsl) {
        this(dsl, DEFAULT_CONFIG_FILE)
    }

    YamlFlowBuilder(def dsl, String configFile) {
        PipelineDslHolder.dsl = dsl
        this.configFile = configFile
    }

    Flow build() {
        dsl.echo "YamlFlowBuilder used configFile: ${configFile}"
        dsl.sh "pwd; ls -la"
        def yamlConfiguration = dsl.readTrusted configFile
        dsl.echo "yamlConfiguration: ${yamlConfiguration}"
        YamlConfig model = null
//        model = getYamlModelUsingJackson(yamlConfiguration)
//        model = getYamlModelUsingSnakeYaml(yamlConfiguration)
        model = getYamlModelUsingJenkinsReadYamlCommand(yamlConfiguration)
        dsl.echo "YamlConfig: ${model.toString()}"
        FlowBuilder flowBuilder = flowBuilderConverter.toFlowBuilder(model)
        dsl.echo "flowBuilder: ${flowBuilder.toString()}"
//        return flowBuilder.build()
        return new Flow(null)
    }

    YamlConfig getYamlModelUsingJenkinsReadYamlCommand(def yamlConfiguration) {
        def properties = dsl.readYaml text: yamlConfiguration
        populateWithJenkinsBuildParams(properties)
        return yamlConverter.toYamlModel(properties)
    }

    void populateWithJenkinsBuildParams(def properties) {
        def params = dsl.params
        params.each { param -> replaceVariablesOrSetProperty(param, properties)}
    }

    void replaceVariablesOrSetProperty(def param, def properties) {
        dsl.echo "param: ${param}"
        dsl.echo "param: ${param.key}:${param.value}"
    }

    YamlConfig getYamlModelUsingSnakeYaml(def yamlConfiguration) {
        Yaml parser = new Yaml()
        YamlConfig model = parser.loadAs(yamlConfiguration, YamlConfig.class)
        return model
    }

    YamlConfig getYamlModelUsingJackson(def yamlConfiguration) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
        YamlConfig model = mapper.readValue(yamlConfiguration, YamlConfig.class)
        return model
    }

    @Override
    public String toString() {
        return "YamlFlowBuilder{" +
                "configFile='" + configFile + '\'' +
                '}';
    }
}
