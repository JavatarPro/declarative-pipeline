/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.jenkins.dsl;

import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.jenkins.api.JenkinsExecutor
import pro.javatar.pipeline.stage.StageAware

import java.time.Duration

/**
 * @author Borys Zora
 * @version 2019-11-03
 */
class JenkinsDslServiceImpl implements JenkinsDslService {

    private def dsl;

    JenkinsDslServiceImpl(dsl) {
        this.dsl = dsl
    }

    @Override
    void executeStage(StageAware stage) {
        dsl.stage(stage.getName()) {
            stage.execute();
        }
    }

    @Override
    void executeWithinTimeoutInSpecifiedDirectory(Duration timeout, String directory, JenkinsExecutor executor) {
        dsl.timeout(time: timeout.toMinutes(), unit: 'MINUTES') { // TODO refactor
            dsl.dir(directory) {
                executor.execute();
            }
        }
    }

}
