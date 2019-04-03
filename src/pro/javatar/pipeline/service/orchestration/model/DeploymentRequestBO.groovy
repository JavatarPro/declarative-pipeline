/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2019-03-28
 */
class DeploymentRequestBO implements Serializable {

    String imageName

    String imageVersion

    DockerRegistryBO dockerRegistry

    Env environment

    String buildNumber

    DeploymentRequestBO() {
        Logger.info("DeploymentRequestBO:default constructor")
    }

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

    DockerRegistryBO getDockerRegistry() {
        return dockerRegistry
    }

    void setDockerRegistry(DockerRegistryBO dockerRegistry) {
        this.dockerRegistry = dockerRegistry
    }

    DeploymentRequestBO withDockerRegistry(DockerRegistryBO dockerRegistry) {
        setDockerRegistry(dockerRegistry)
        return this;
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
                ", dockerRegistry='" + dockerRegistry + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
