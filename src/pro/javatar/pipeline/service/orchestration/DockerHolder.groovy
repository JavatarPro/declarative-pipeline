/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
class DockerHolder {

    // full image name, registries
    static Set<String> alreadyDeployedVersions = new HashSet<>()

    static def addToAlreadyPublished(String imageName, String imageVersion, String registry) {
        String image = getImageFullName(imageName, imageVersion, registry)
        alreadyDeployedVersions.add(image)
    }

    static boolean isImageAlreadyPublished(String imageName, String imageVersion, String registry) {
        String image = getImageFullName(imageName, imageVersion, registry)
        return alreadyDeployedVersions.contains(image)
    }

    static String getImageFullName(String imageName, String imageVersion, String registry) {
        return "${registry}/${imageName}:${imageVersion}"
    }

}
