/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.jenkins.api

import pro.javatar.pipeline.stage.StageAware

import java.time.Duration

/**
 * Abstraction for jenkins declarative pipeline dsl language
 * to decouple domain from jenkins implementation
 *
 * @author Borys Zora
 * @version 2019-11-03
 */
public interface JenkinsDslService {

    void executeStage(StageAware stage);

    void executeWithinTimeoutInSpecifiedDirectory(Duration timeout, String directory, JenkinsExecutor executor);

    /**
     * @param file close to Jenkinsfile (relative pass)
     * @return raw configuration string
     */
    String readConfiguration(String file);

    Map getJenkinsJobParameters();

    def readYaml(String yamlConfig);

    String getShellExecutionResponse(String command);

    void executeShell(String command);

    void executeSecureShell(String command, String credentialsId, String userVariable, String passwordVariable);

    void addToPath(String toolName, String variable);

    /**
     * return direct/raw jenkins dsl
     */
    def directDsl();
}
