/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-19
 */
// e.g. "docker-dev.javatar.com/absence-tracker:0.0.15.30"
class DockerImage implements Serializable {
    String dockerUrl
    String deployment
    Version version

    static DockerImage fromString(String image) {
        DockerImage result = new DockerImage()
        String[] items = image.split(":")
        result.version = Version.fromString(items[1])
        String[] it = items[0].split("/")
        result.dockerUrl = it[0]
        result.deployment = it[1]
        return result
    }
}
