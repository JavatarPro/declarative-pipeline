package pro.javatar.pipeline.builder

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature

//@Grab('org.yaml:snakeyaml:1.21')
//@Grab('org.apache.commons:commons-lang3:3.4')
import org.yaml.snakeyaml.Yaml
@Grab('com.fasterxml.jackson.core:jackson-databind:2.9.6')
@Grab('com.fasterxml.jackson.core:jackson-annotations:2.9.6')
import com.fasterxml.jackson.databind.ObjectMapper;
@Grab('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.6')
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.service.PipelineDslHolder
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlFlowBuilder {

    static final DEFAULT_CONFIG_FILE = "javatar-declarative-pipeline.yml"

    def properties

    String configFile

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
        YamlModel model = null
//        model = getYamlModelUsingJackson(yamlConfiguration)
        model = getYamlModelUsingSnakeYaml(yamlConfiguration)
//        model = getYamlModelUsingJenkinsReadYamlCommand(yamlConfiguration)
        dsl.echo "YamlModel: ${model.toString()}"
        FlowBuilder flowBuilder = new FlowBuilder()
        return flowBuilder.build()
    }

    YamlModel getYamlModelUsingJenkinsReadYamlCommand(def yamlConfiguration) {
        properties = dsl.readYaml text: yamlConfiguration
        dsl.echo "${properties.docker.dev.credentialsId}"
    }

    YamlModel getYamlModelUsingSnakeYaml(def yamlConfiguration) {
        Yaml parser = new Yaml()
        YamlModel model = parser.loadAs(yamlConfiguration, YamlModel.class)
        return model
    }

    YamlModel getYamlModelUsingJackson(def yamlConfiguration) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
        YamlModel model = mapper.readValue(yamlConfiguration, YamlModel.class)
        return model
    }

    @Override
    public String toString() {
        return "YamlFlowBuilder{" +
                "properties=" + properties.size() +
                '}';
    }
}
