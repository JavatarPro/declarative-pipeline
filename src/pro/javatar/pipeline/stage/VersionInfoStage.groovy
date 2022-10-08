/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.domain.K8sVersions
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.integration.k8s.K8sVersionInfo
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.service.ContextHolder
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.toPrettyJson

/**
 * @author Borys Zora
 * @version 2022-09-12
 */
class VersionInfoStage extends Stage {

    K8sVersionInfo info
    SlackChannelSender sender

    @Override
    void execute() throws PipelineException {
        Logger.debug("VersionInfoStage#execute started")
        info = ContextHolder.get(K8sVersionInfo.class)
        sender = ContextHolder.get(SlackChannelSender.class)
        def current = info.versionsCurrent()
        def next = info.versionsNext(current)

        String msg = "Dev versions:\n```\n${toPrettyJson(current)}\n```"
        Logger.debug(msg)
        // TODO make slack template
        sender.send(msg)

        msg = "${K8sVersions.PROPOSED_VERSIONS}\n```\n${toPrettyJson(next)}\n```"
        Logger.debug(msg)
        // TODO make slack template
        sender.send(msg)

        Logger.debug("VersionInfoStage#execute completed")
    }

    @Override
    String name() {
        return "Version Info"
    }
}
