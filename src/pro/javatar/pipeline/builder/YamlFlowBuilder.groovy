package pro.javatar.pipeline.builder

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
        def trustedText = dsl.readTrusted configFile
        dsl.echo "trustedText: ${trustedText}"
        properties = dsl.readYaml text: trustedText
        dsl.echo "${properties.docker.dev.credentialsId}"
        dsl.echo "YamlFlowBuilder constructor finished with state: ${this.toString()}"
        String buildType = ""
        FlowBuilder flowBuilder = new FlowBuilder()
        return flowBuilder.build()
    }

    @Override
    public String toString() {
        return "YamlFlowBuilder{" +
                "properties=" + properties.size() +
                '}';
    }
}
