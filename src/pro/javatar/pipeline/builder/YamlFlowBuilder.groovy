package pro.javatar.pipeline.builder

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.converter.FlowBuilderConverter
import pro.javatar.pipeline.builder.converter.JenkinsBuildParamsConverter
import pro.javatar.pipeline.builder.converter.YamlConverter
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.replaceVariables
import pro.javatar.pipeline.service.PipelineDslHolder

class YamlFlowBuilder implements Serializable {

    private static final List<String> DEFAULT_CLASSPATH_CONFIGS = [
            "suit.yml",
            "stage.yml",
            "pipeline.yml",
            "integration.yml",
            "command.yml",
    ]

    private List<String> configFiles

    private JenkinsDsl dslService;

    private YamlConverter yamlConverter = new YamlConverter()

    private FlowBuilderConverter flowBuilderConverter = new FlowBuilderConverter()

    private JenkinsBuildParamsConverter jenkinsBuildParamsConverter = new JenkinsBuildParamsConverter()

    public YamlFlowBuilder(def dsl, String... configFiles) {
        this(PipelineDslHolder.createDsl(dsl), configFiles.toList())
    }

    public YamlFlowBuilder(def dsl, List<String> configFiles) {
        this(PipelineDslHolder.createDsl(dsl), configFiles)
    }

    protected YamlFlowBuilder(JenkinsDsl jenkinsDslService, List<String> configFiles) {
        dslService = jenkinsDslService;
        this.configFiles = configFiles;
        Logger.debug("YamlFlowBuilder#constructor: configFiles: " + configFiles)
    }

    public Flow build() {
        YamlConfig config = getEffectiveConfig(configFiles.get(0))
        FlowBuilder flowBuilder = flowBuilderConverter.toFlowBuilder(config, dslService);
        Logger.debug("flowBuilder: " + flowBuilder.toString());
        return flowBuilder.build();
    }

    protected Flow build2() {
        YamlConfig config = getEffectiveConfig2(configFiles)
        return null
    }

    protected YamlConfig getEffectiveConfig2(List<String> configFiles) {
        List<String> configs = new ArrayList<>()
        Logger.info("configs count: " + configFiles.size())
        for(String config: configFiles) {
            Logger.info("read configs: " + config)
            String configString = dslService.readConfiguration(config)
            Logger.info("read config: ${config} configString: ${configString}")
            configs.add(configString)
        }
        for(String config: DEFAULT_CLASSPATH_CONFIGS) {
            Logger.info("read classpath configs: " + config)
            String configString = readConfiguration(config)
            Logger.info("read config: ${config} configString: ${configString}")
            configs.add(configString)
        }
        return new YamlConfig()
    }

    YamlConfig getEffectiveConfig(String configFile) {
        Logger.info("YamlFlowBuilder:getEffectiveConfig used configFile: " + configFile);
        String yamlConfiguration = dslService.readConfiguration(configFile);
        Logger.trace("yamlConfiguration: " + yamlConfiguration);
        YamlConfig config = getYamlModelUsingJenkinsReadYamlCommand(yamlConfiguration);
        Logger.debug("YamlFlowBuilder:getEffectiveConfig:config: " + config.toString());
        return config;
    }

    YamlConfig getYamlModelUsingJenkinsReadYamlCommand(String yamlConfiguration) {
        Logger.debug("yamlConfiguration before replaceVariables: " + yamlConfiguration);
        Map parameters = dslService.getJenkinsJobParameters();
        String yamlConfig = replaceVariables(yamlConfiguration, parameters);
        Logger.debug("yamlConfig after replaceVariables: " + yamlConfig);
        def properties = dslService.readYaml(yamlConfig);
        jenkinsBuildParamsConverter.populateWithJenkinsBuildParams(parameters, properties);
        Logger.info("YamlFlowBuilder:getYamlModelUsingJenkinsReadYamlCommand: before: yamlConverter.toYamlModel");
        YamlConfig result = yamlConverter.toYamlModel(properties);
        Logger.info("YamlFlowBuilder:getYamlModelUsingJenkinsReadYamlCommand:result: " + result);
        return result;
    }

    String readConfiguration(String file) {
        return new String(getClass().getClassLoader().getResourceAsStream(file).bytes);
    }

    protected Flow build3() {
        YamlConfig config = getEffectiveConfig2(configFiles)
        return null
    }
}
