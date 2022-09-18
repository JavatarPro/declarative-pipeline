/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
enum Type {}

enum BuildType {
    MAVEN,
    GRADLE,
    NPM,
    DOCKER
}

enum ReleaseType {
    VCS,
    DOCKER
}

enum CommandType {
    SHELL,
    JENKINS_JOB
}
