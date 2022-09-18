/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-10
 */
enum Type implements Serializable {}

enum BuildType implements Serializable {
    MAVEN,
    GRADLE,
    NPM,
    DOCKER
}

enum ReleaseType implements Serializable {
    VCS,
    DOCKER
}

enum CommandType implements Serializable {
    SHELL,
    JENKINS_JOB
}
