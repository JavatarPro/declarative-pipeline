/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder

import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.config.Config
import pro.javatar.pipeline.config.GradleConfig

/**
 * @author Borys Zora
 * @version 2019-11-05
 */
class ConfigImpl implements Config {

    AutoTestConfig autoTestConfig;

    GradleConfig gradleConfig;

    @Override
    AutoTestConfig autoTest() {
        return autoTestConfig;
    }

    @Override
    GradleConfig gradleConfig() {
        return gradleConfig;
    }

    ConfigImpl setAutoTestConfig(AutoTestConfig autoTestConfig) {
        this.autoTestConfig = autoTestConfig;
        return this;
    }

    ConfigImpl setGradleConfig(GradleConfig gradleConfig) {
        this.gradleConfig = gradleConfig;
        return this;
    }
}
