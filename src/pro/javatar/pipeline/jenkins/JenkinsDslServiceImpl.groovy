/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.jenkins;

import pro.javatar.pipeline.service.JenkinsDslService
import pro.javatar.pipeline.stage.StageAware

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

}
