/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.integration.k8s.K8sVersionInfo
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.service.ContextHolder
import static pro.javatar.pipeline.util.StringUtils.toPrettyJson

/**
 * @author Borys Zora
 * @version 2022-09-12
 */
class VersionInfoStage extends Stage {

    K8sVersionInfo versionInfo () {return ContextHolder.get(K8sVersionInfo.class) }
    SlackChannelSender sender() {return ContextHolder.get(SlackChannelSender.class) }

    @Override
    void execute() throws PipelineException {
        def current = versionInfo().versionsCurrent()
        def next = versionInfo().versionsNext(current)
        def result = [currentVersions: current, proposedVersions: next]
        // TODO make slack template
        sender().send("```\n${toPrettyJson(result)}\n```")
    }

    @Override
    String name() {
        return "Version Info"
    }
}
