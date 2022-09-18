/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.AutoTest
import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.domain.Maven
import pro.javatar.pipeline.domain.Pipeline
import pro.javatar.pipeline.domain.Slack
import pro.javatar.pipeline.domain.Vcs
import pro.javatar.pipeline.domain.VersionConfig

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
class ConfigMerger {

    static Config merge(Config base, Config overrides) {
        mergePipeline(base.pipeline, overrides.pipeline)
        base.docker = list(base.docker, overrides.docker)
        mergeVcs(base.vcs, overrides.vcs)
        mergeMaven(base.maven, overrides.maven)
        base.log_level = item(base.log_level, overrides.log_level)
        mergeAutoTest(base.autoTest, overrides.autoTest)
        mergeSlack(base.slack, overrides.slack)
        mergeVersion(base.version, overrides.version)
        return base
    }

    private static void mergePipeline(Pipeline base, Pipeline overrides) {
        base.suit = item(base.suit, overrides.suit)
        base.service = item(base.service, overrides.service)
        base.orchestration = item(base.orchestration, overrides.orchestration)
        base.build = list(base.build, overrides.build)
        base.release = list(base.release, overrides.release)
    }

    private static void mergeSlack(Slack base, Slack overrides) {
        base.enabled = item(base.enabled, overrides.enabled)
        base.webhookUrl = item(base.webhookUrl, overrides.webhookUrl)
    }

    private static void mergeVersion(VersionConfig base, VersionConfig overrides) {
        base.file = item(base.file, overrides.file)
        base.pattern = item(base.pattern, overrides.pattern)
    }

    private static void mergeAutoTest(AutoTest base, AutoTest overrides) {
        base.commands = list(base.commands, overrides.commands)
    }

    private static void mergeVcs(Vcs base, Vcs overrides) {
        base.url = item(base.url, overrides.url)
        base.cred = item(base.cred, overrides.cred)
    }

    private static void mergeMaven(Maven base, Maven overrides) {
        base.repo_id = item(base.repo_id, overrides.repo_id)
        base.repo_url = item(base.repo_url, overrides.repo_url)
        base.jenkins_tool_mvn = item(base.jenkins_tool_mvn, overrides.jenkins_tool_mvn)
        base.jenkins_tool_jdk = item(base.jenkins_tool_jdk, overrides.jenkins_tool_jdk)
        base.build_cmd = item(base.build_cmd, overrides.build_cmd)
        base.integration_test_cmd = item(base.integration_test_cmd, overrides.integration_test_cmd)
        base.params = item(base.params, overrides.params)
    }

    static def item(def base, def overrides) {
        if (overrides != null) return overrides
        return base
    }

    static List list(List base, List overrides) {
        if (overrides == null || overrides.empty) return base
        return overrides
    }
}
