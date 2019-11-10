/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.jenkins.api

import pro.javatar.pipeline.stage.StageAware

import java.time.Duration

/**
 * @author Borys Zora
 * @version 2019-11-03
 */
interface JenkinsDslService {

    void executeStage(StageAware stage);

    void executeWithinTimeoutInSpecifiedDirectory(Duration timeout, String directory, JenkinsExecutor executor);

}