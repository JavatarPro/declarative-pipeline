/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.jenkins.api.JenkinsDsl

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
class ConfigInitialization {

    static Config createEffectiveConfig(JenkinsDsl dsl,
                                        List<String> configFiles) {
        Map bindings = dsl.getJenkinsJobParameters()
        Config result = new Config()
        configFiles.each {it ->
            String yaml = dsl.readConfiguration(it)
            Config config = ConfigYamlConverter.toConfig(yaml, bindings, dsl)
            ConfigMerger.merge(result, config)
        }
        return result
    }
}
