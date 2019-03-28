/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

/**
 * @author Borys Zora
 * @version 2019-03-28
 */
class DeploymentRequestBO {

    String imageName

    String imageVersion

    String dockerRepositoryUrl

    String environment

    String getImageName() {
        return imageName
    }

    void setImageName(String imageName) {
        this.imageName = imageName
    }

    DeploymentRequestBO withImageName(String imageName) {
        setImageName(imageName)
        return this;
    }

    String getImageVersion() {
        return imageVersion
    }

    void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion
    }

    DeploymentRequestBO withImageVersion(String imageVersion) {
        setImageVersion(imageVersion)
        return this;
    }

    String getDockerRepositoryUrl() {
        return dockerRepositoryUrl
    }

    void setDockerRepositoryUrl(String dockerRepositoryUrl) {
        this.dockerRepositoryUrl = dockerRepositoryUrl
    }

    DeploymentRequestBO withDockerRepositoryUrl(String dockerRepositoryUrl) {
        setDockerRepositoryUrl(dockerRepositoryUrl)
        return this
    }

    String getEnvironment() {
        return environment
    }

    void setEnvironment(String environment) {
        this.environment = environment
    }

    DeploymentRequestBO withEnvironment(String environment) {
        this.environment = environment
        return this
    }

    @Override
    public String toString() {
        return "DeploymentRequestBO{" +
                "imageName='" + imageName + '\'' +
                ", imageVersion='" + imageVersion + '\'' +
                ", dockerRepositoryUrl='" + dockerRepositoryUrl + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
