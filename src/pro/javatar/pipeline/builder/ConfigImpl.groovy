/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder

import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.config.Config

/**
 * @author Borys Zora
 * @version 2019-11-05
 */
class ConfigImpl implements Config {

    AutoTestConfig autoTestConfig;

    @Override
    AutoTestConfig autoTest() {
        return autoTestConfig
    }

    ConfigImpl setAutoTestConfig(AutoTestConfig autoTestConfig) {
        this.autoTestConfig = autoTestConfig;
        return this;
    }

}
