package pro.javatar.pipeline.builder

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.service.PipelineDslHolder
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl


class YamlFlowBuilder {

    static final DEFAULT_CONFIG_FILE = "javatar-declarative-pipeline.yml"

    def properties

    YamlFlowBuilder(def dsl) {
        this(dsl, DEFAULT_CONFIG_FILE)
    }

    YamlFlowBuilder(def dsl, String configFile) {
        PipelineDslHolder.dsl = dsl
        dsl.echo "YamlFlowBuilder used configFile: ${configFile}"
        this.properties = dsl.readYaml configFile
        dsl.echo "YamlFlowBuilder constructor finished with state: ${this.toString()}"
    }

    Flow build() {
        dsl.echo ""
        String buildType = ""
        FlowBuilder flowBuilder = new FlowBuilder().withBuildType()
    }

    @Override
    public String toString() {
        return "YamlFlowBuilder{" +
                "properties=" + properties +
                '}';
    }
}
