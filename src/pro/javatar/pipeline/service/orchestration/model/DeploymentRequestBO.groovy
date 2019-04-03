/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.model.Env

/**
 * @author Borys Zora
 * @version 2019-03-28
 */
class DeploymentRequestBO {

    String imageName

    String imageVersion

    String dockerRepositoryUrl

    Env environment

    String buildNumber

    String getImageVersionWithBuildNumber() {
        return "${imageVersion}.${buildNumber}"
    }

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

    Env getEnvironment() {
        return environment
    }

    void setEnvironment(Env environment) {
        this.environment = environment
    }

    DeploymentRequestBO withEnvironment(Env environment) {
        setEnvironment(environment)
        return this
    }

    String getBuildNumber() {
        return buildNumber
    }

    void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber
    }

    DeploymentRequestBO withBuildNumber(String buildNumber) {
        setBuildNumber(buildNumber)
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
