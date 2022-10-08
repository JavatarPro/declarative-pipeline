/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
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

    private K8sVersionInfo info

//    @NonCPS
    @Override
    void execute() throws PipelineException {
        Logger.debug("VersionInfoStage#execute started")
        info = ContextHolder.get(K8sVersionInfo.class)
        SlackChannelSender sender = ContextHolder.get(SlackChannelSender.class)
        // def current = new HashMap()
        // def next = new HashMap()
        def current = info.versionsCurrent()
        def next = info.versionsNext(current)
        def result = [
                "${K8sVersions.DEV_VERSIONS}": current,
                "${K8sVersions.PROPOSED_VERSIONS}": next
        ]
        // TODO make slack template
        Logger.debug("```\n${toPrettyJson(result)}\n```")
        // sender.send("```\n${toPrettyJson(result)}\n```")
        Logger.debug("VersionInfoStage#execute completed")
    }

    @Override
    String name() {
        return "Version Info"
    }
}
