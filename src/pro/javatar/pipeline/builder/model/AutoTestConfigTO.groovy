/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model;

import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.util.Logger;

import java.time.Duration

import static pro.javatar.pipeline.util.StringUtils.isBlank;

/**
 * @author Borys Zora
 * @version 2019-11-17
 */
class AutoTestConfigTO implements AutoTestConfig {

    private boolean enabled;

    private Duration timeout;

    private Duration initialDelay;

    private String jobName;

    private String command;

    private boolean staticCodeAnalysisEnabled;

    AutoTestConfigTO() {}

    static AutoTestConfigTO ofAutoTestYmlConfig(def autoTest) {
        return new AutoTestConfigTO()
                .setEnabled(retrieveEnabled(autoTest))
                .setTimeout(retrieveTimeout(autoTest))
                .setInitialDelay(retrieveInitialDelay(autoTest))
                .setJobName(retrieveJobName(autoTest))
                .setCommand(retrieveCommand(autoTest))
                .setStaticCodeAnalysisEnabled(retrieveStaticCodeAnalysisEnabled(autoTest))
                .validate();

    }

    @Override
    boolean enabled() {
        return enabled
    }

    @Override
    Duration timeout() {
        return timeout;
    }

    @Override
    Duration initialDelay() {
        return initialDelay
    }

    @Override
    String jobName() {
        return jobName;
    }

    @Override
    String command() {
        return command;
    }

    @Override
    boolean staticCodeAnalysisEnabled() {
        return staticCodeAnalysisEnabled;
    }

    AutoTestConfigTO setEnabled(boolean enabled) {
        this.enabled = enabled
        return this;
    }

    AutoTestConfigTO setTimeout(Duration timeout) {
        this.timeout = timeout
        return this;
    }

    AutoTestConfigTO setInitialDelay(Duration initialDelay) {
        this.initialDelay = initialDelay
        return this;
    }

    AutoTestConfigTO setJobName(String jobName) {
        this.jobName = jobName
        return this;
    }

    AutoTestConfigTO setCommand(String command) {
        this.command = command
        return this;
    }

    AutoTestConfigTO setStaticCodeAnalysisEnabled(boolean staticCodeAnalysisEnabled) {
        this.staticCodeAnalysisEnabled = staticCodeAnalysisEnabled
        return this;
    }

    private AutoTestConfigTO validate() {
        // TODO
        return this;
    }

    // helper retrieval methods

    private static boolean retrieveEnabled(def autoTest) {
        if (autoTest.enabled == null) {
            return DEFAULT_AUTO_TESTS_ENABLED;
        }
        return autoTest.enabled
    }

    private static Duration retrieveTimeout(def autoTest) {
        if (autoTest.timeout == null) {
            return DEFAULT_TIMEOUT;
        }
        try {
            return Duration.parse(autoTest.timeout)
        } catch (Exception e) {
            Logger.error("could not parse duration in field autoTest.timeout: "
                    + autoTest.timeout + " " + e.getMessage());
            Logger.warn("default value will be used for AutoTestConfig.timeout(): " + DEFAULT_TIMEOUT.toString());
            return DEFAULT_TIMEOUT;
        }
    }

    private static Duration retrieveInitialDelay(def autoTest) {
        if (autoTest.initialDelay == null) {
            return DEFAULT_INITIAL_DELAY;
        }
        try {
            return Duration.parse(autoTest.initialDelay)
        } catch (Exception e) {
            Logger.error("could not parse duration in field yml.initialDelay: "
                    + autoTest.initialDelay + " " + e.getMessage());
            Logger.warn("default value will be used for AutoTestConfig.initialDelay(): "
                    + DEFAULT_TIMEOUT.toString());
            return DEFAULT_INITIAL_DELAY;
        }
    }

    private static String retrieveJobName(def autoTest) {
        if (isBlank(autoTest.jobName)) {
            return DEFAULT_JOB_NAME;
        }
        return autoTest.jobName
    }

    private static String retrieveCommand(def autoTest) {
        return autoTest.command
    }

    private static boolean retrieveStaticCodeAnalysisEnabled(def autoTest) {
        return false; // TODO
    }
}
