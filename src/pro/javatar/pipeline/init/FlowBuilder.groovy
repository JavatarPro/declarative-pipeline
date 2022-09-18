/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.PipelineDslHolder
import pro.javatar.pipeline.stage.StageAware
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.init.ConfigInitialization.createEffectiveConfig
import static pro.javatar.pipeline.init.PipelineInitialization.createStages
import static pro.javatar.pipeline.init.ServiceInitialization.createServices

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
class FlowBuilder {

    static Flow build(def dsl, String... configFiles) {
        return build(PipelineDslHolder.createDsl(dsl), configFiles.toList());
    }

    static Flow build(JenkinsDsl dsl,
                      List<String> configFiles) {
        Logger.debug("build Flow from dsl and configFiles: ${configFiles}")
        Config config = createEffectiveConfig(dsl, configFiles)
        createServices(dsl, config)
        List<StageAware> stages = createStages(dsl, config)
        ReleaseInfo info = releaseInfo(dsl, config)
        return new Flow(info, dsl)
                .addStages(stages)
    }

    static ReleaseInfo releaseInfo(JenkinsDsl dsl, Config config) {
        ReleaseInfo info = new ReleaseInfo()
        info.buildNumber = dsl.buildNumber()
        info.serviceName = config.pipeline.service
        info.config = config
        return info
    }

}
