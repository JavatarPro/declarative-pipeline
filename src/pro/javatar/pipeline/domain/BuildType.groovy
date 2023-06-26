/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-19
 */
enum BuildType implements Serializable {
    MAVEN,
    GRADLE,
    NPM,
    NODE,
    DOCKER
}