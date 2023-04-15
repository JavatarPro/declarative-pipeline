/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.AutoTest
import pro.javatar.pipeline.domain.BuildType
import pro.javatar.pipeline.domain.Command
import pro.javatar.pipeline.domain.CommandType
import pro.javatar.pipeline.domain.Config
import pro.javatar.pipeline.domain.Docker
import pro.javatar.pipeline.domain.Maven
import pro.javatar.pipeline.domain.Npm
import pro.javatar.pipeline.domain.Pipeline
import pro.javatar.pipeline.domain.ReleaseType
import pro.javatar.pipeline.domain.Slack
import pro.javatar.pipeline.domain.Vcs
import pro.javatar.pipeline.domain.VersionConfig
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.model.DockerOrchestrationServiceType
import pro.javatar.pipeline.model.PipelineStagesSuit
import pro.javatar.pipeline.util.LogLevel

import static pro.javatar.pipeline.util.StringUtils.replaceVariables

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
class ConfigYamlConverter {

    static Config toConfig(String yaml,
                           Map binding,
                           JenkinsDsl dsl) {
        String filledYaml = replaceVariables(yaml, binding)
        return toConfig(dsl.readYaml(filledYaml))
    }

    static Config toConfig(def yaml) {
        Config config = new Config()
        if (yaml == null) return config
        populatePipeline(config.pipeline, yaml.pipeline)
        populateVcs(config.vcs, yaml.vcs)
        populateMaven(config.maven, yaml.maven)
        populateNpm(config.npm, yaml.npm)
        populateDockers(config.docker, yaml.docker)
        populateAutoTests(config.autoTest, yaml.auto_test)
        config.log_level = LogLevel.fromString(yaml.log_level)
        populateSlack(config.slack, yaml.slack)
        populateVersion(config.version, yaml.version)
        return config
    }

    private static void populatePipeline(Pipeline pipeline, def yaml) {
        if (yaml == null) return
        pipeline.suit = PipelineStagesSuit.fromString(yaml.suit)
        pipeline.service = yaml.service
        pipeline.orchestration = DockerOrchestrationServiceType.fromString(yaml.orchestration)
        populateBuilds(pipeline.build, yaml.build)
        populateReleases(pipeline.release, yaml.release)
    }

    private static void populateBuilds(List<BuildType> build,
                                       List<String> list) {
        list.each { val -> build.add(BuildType.valueOf(val.toUpperCase())) }
    }

    private static void populateReleases(List<ReleaseType> release,
                                         List<String> list) {
        list.each { val -> release.add(ReleaseType.valueOf(val.toUpperCase())) }
    }

    private static void populateVcs(Vcs vcs, def yaml) {
        if (yaml == null) return
        vcs.url = yaml.url
        vcs.cred = yaml.cred
    }

    private static void populateMaven(Maven maven, def yaml) {
        if (yaml == null) return
        maven.params = yaml.params
        maven.jenkins_tool_mvn = yaml.jenkins_tool_mvn
        maven.jenkins_tool_jdk = yaml.jenkins_tool_jdk
    }

    private static void populateNpm(Npm npm, def yaml) {
        if (yaml == null) return
        npm.version = yaml.version
        npm.type = yaml.type
    }

    private static void populateDockers(List<Docker> dockers, List list) {
        list.each { val -> dockers.add(toDocker(val)) }
    }

    private static Docker toDocker(def yaml) {
        Docker docker = new Docker()
        if (yaml == null) docker
        docker.name = yaml.name
        docker.cred = yaml.cred
        docker.url = yaml.url
        return docker
    }

    private static void populateAutoTests(AutoTest autoTest, def yaml) {
        if (yaml == null) return
        List commands = yaml.commands
        if (commands == null) return
        commands.each { val -> autoTest.commands.add(toCommand(val))}
    }

    private static Command toCommand(def yaml) {
        Command command = new Command()
        if (yaml == null) return command
        command.name = yaml.name
        command.shell = yaml.shell
        command.job = yaml.job
        command.type = command.job != null ? CommandType.JENKINS_JOB : CommandType.SHELL
        return command
    }

    private static void populateSlack(Slack slack, def yaml) {
        if (yaml == null) return
        slack.enabled = yaml.enabled
        slack.webhookUrl = yaml.webhookUrl
    }

    private static void populateVersion(VersionConfig version, def yaml) {
        if (yaml == null) return
        version.file = yaml.file
        version.pattern = yaml.pattern
    }
}
