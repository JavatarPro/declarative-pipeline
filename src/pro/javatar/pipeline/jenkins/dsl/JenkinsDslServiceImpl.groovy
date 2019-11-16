/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.jenkins.dsl;

import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.jenkins.api.JenkinsExecutor
import pro.javatar.pipeline.service.PipelineDslHolder
import pro.javatar.pipeline.stage.StageAware

import java.time.Duration

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @version 2019-11-03
 */
class JenkinsDslServiceImpl implements JenkinsDslService {

    private def dsl;

    JenkinsDslServiceImpl(def dsl) {
        this.dsl = dsl
        PipelineDslHolder.dsl = dsl
    }

    @Override
    void executeStage(StageAware stage) {
        dsl.stage(stage.getName()) {
            stage.execute();
        }
    }

    @Override
    String readConfiguration(String configFile) {
        String config = dsl.readTrusted configFile;
        return config;
    }

    @Override
    Map getJenkinsJobParameters() {
        return dsl.params
    }

    @Override
    def readYaml(String yamlConfig) {
        def config = dsl.readYaml text: yamlConfig
        return config
    }

    @Override
    String getShellExecutionResponse(String command) {
        String result = dsl.sh returnStdout: true, script: command
        return result
    }

    @Override
    void executeShell(String command) {
        dsl.sh command;
    }

    @Override
    void addToPath(String toolName, String variable) {
        def tool="${dsl.tool toolName}";
        dsl.env."${variable}"= tool;
        dsl.env.PATH=String.format("%s/bin:%s", tool, dsl.env.PATH);
    }

    @Override
    void executeWithinTimeoutInSpecifiedDirectory(Duration timeout, String directory, JenkinsExecutor executor) {
        dsl.timeout(time: timeout.toMinutes(), unit: 'MINUTES') { // TODO refactor
            dsl.dir(directory) {
                executor.execute();
            }
        }
    }

    @Override
    def directDsl() {
        return dsl
    }

}
