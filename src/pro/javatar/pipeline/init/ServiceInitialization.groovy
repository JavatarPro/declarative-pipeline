/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.jenkins.api.JenkinsDsl

import static pro.javatar.pipeline.service.ContextHolder.add

/**
 * @author Borys Zora
 * @version 2022-09-11
 */
class ServiceInitialization implements Serializable {

    static void createServices(JenkinsDsl dsl, Config config) {
        // TODO minimum to get service name and version from dev env
        add(new SlackChannelSender(config.slack, dsl))
    }
}
