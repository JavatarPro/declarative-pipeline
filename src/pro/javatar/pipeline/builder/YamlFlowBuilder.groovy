package pro.javatar.pipeline.builder

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.converter.FlowBuilderConverter
import pro.javatar.pipeline.builder.converter.JenkinsBuildParamsConverter
import pro.javatar.pipeline.builder.converter.YamlConverter
import pro.javatar.pipeline.builder.model.YamlConfig
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.jenkins.dsl.JenkinsDslServiceImpl
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.replaceVariables

class YamlFlowBuilder implements Serializable {

    private String configFile

    private JenkinsDslService dslService;

    private YamlConverter yamlConverter = new YamlConverter()

    private FlowBuilderConverter flowBuilderConverter = new FlowBuilderConverter()

    private JenkinsBuildParamsConverter jenkinsBuildParamsConverter = new JenkinsBuildParamsConverter()

    public YamlFlowBuilder(def dsl, String configFile) {
        this(configFile, new JenkinsDslServiceImpl(dsl))
    }

    public YamlFlowBuilder(String configFile, JenkinsDslService jenkinsDslService) {
        dslService = jenkinsDslService;
        this.configFile = configFile;
        Logger.debug("YamlFlowBuilder#constructor: configFile: " + configFile)
    }

    public Flow build() {
        Logger.info("YamlFlowBuilder used configFile: " + configFile);
        String yamlConfiguration = dslService.readConfiguration(configFile);
        Logger.trace("yamlConfiguration: " + yamlConfiguration);
        YamlConfig model = getYamlModelUsingJenkinsReadYamlCommand(yamlConfiguration);
        Logger.debug("YamlConfig:build:model: " + model.toString());
        FlowBuilder flowBuilder = flowBuilderConverter.toFlowBuilder(model, dslService);
        Logger.debug("flowBuilder: " + flowBuilder.toString());
        return flowBuilder.build();
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

}
