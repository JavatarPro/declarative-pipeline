/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service

import pro.javatar.pipeline.stage.StageAware

/**
 * @author Borys Zora
 * @version 2019-11-03
 */
interface JenkinsDslService {

    void executeStage(StageAware stage);

}