/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

/**
 * @author Borys Zora
 * @version 2021-05-15
 */
interface DockerReleaseInfo {

    /**
     * dev version should be applied to service orchestration and respect that real artifact version
     * might not changed due to previous failures for example, so it should guarantee uniqueness to be applied
     * it could be achieved by adding buildNumber devVersion => repo/name:version.buildNumber
     * @return docker dev registry image dockerDevRegistry/imageName:devVersion
     */
    String dockerDevImage()

    /**
     * releasable docker image, should not be overridden in docker registry (make sure you have correct configuration
     * for your chosen docker registry e.g. sonatype nexus, native docker registry etc.)
     * version - in this case version should be unique due to it could be only created after applying next dev version
     * dockerRegistry is a prod or just final artifacts registry
     * @return docker registry image dockerRegistry/imageName:version
     */
    String dockerImage()

}