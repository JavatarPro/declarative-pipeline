/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.model.BuildServiceType
import pro.javatar.pipeline.model.DockerOrchestrationServiceType
import pro.javatar.pipeline.model.PipelineStagesSuit
import pro.javatar.pipeline.release.ReleaseType
import pro.javatar.pipeline.util.LogLevel

/**
 * @author Borys Zora
 * @version 2021-06-12
 */
class Config implements Serializable {
    Pipeline pipeline = new Pipeline()
    List<Docker> docker = new ArrayList<>()
    Vcs vcs = new Vcs()
    Maven maven = new Maven()
    Npm npm = new Npm()
    LogLevel log_level = LogLevel.DEBUG
    AutoTest autoTest = new AutoTest()
    Slack slack = new Slack()
    VersionConfig version = new VersionConfig()
}

class Docker implements Serializable {
    String name
    String cred
    String url
}

class Pipeline implements Serializable {
    PipelineStagesSuit suit
    String service
    DockerOrchestrationServiceType orchestration
    List<BuildServiceType> build = new ArrayList<>()
    List<ReleaseType> release = new ArrayList<>()
}

class Vcs implements Serializable { // version control system
    String url
    String cred

    @NonCPS @Override
    String toString() { return "Vcs{ url='" + url + '\'' + ", cred='" + cred + '\'' + '}' }
}

class AutoTest implements Serializable {
    List<Command> commands = new ArrayList<>()
}

class Command implements Serializable {
    String shell
    String job
    String name
    String file
    String params
    CommandType type
}

class Slack implements Serializable {
    boolean enabled
    String webhookUrl
    // TODO
    String credentialsId
}

class VersionConfig implements Serializable {
    String file
    String pattern
}