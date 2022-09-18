/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.service.ContextHolder

/**
 * @author Borys Zora
 * @version 2022-09-12
 */
class VersionInfoStage extends Stage {

    SlackChannelSender sender = ContextHolder.get(SlackChannelSender.class)

    @Override
    void execute() throws PipelineException {
        sender.send("test")
    }

    @Override
    String name() {
        return "Version Info"
    }
}
