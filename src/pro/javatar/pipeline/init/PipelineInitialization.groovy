/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.stage.StageAware

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
class PipelineInitialization {

    static List<StageAware> createStages(JenkinsDsl dsl,
                                         Config config) {
        return null;
    }
}
