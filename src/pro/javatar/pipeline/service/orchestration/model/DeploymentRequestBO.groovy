/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

import com.cloudbees.groovy.cps.NonCPS
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

    Env env

    Integer buildNumber

    String service

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
        return env
    }

    void setEnvironment(Env environment) {
        this.env = environment
    }

    void setEnvironment(String environment) {
        setEnvironment(Env.fromString(environment))
    }

    DeploymentRequestBO withEnvironment(String environment) {
        setEnvironment(environment)
        return this
    }

    DeploymentRequestBO withEnvironment(Env environment) {
        setEnvironment(environment)
        return this
    }

    Integer getBuildNumber() {
        return buildNumber
    }

    void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber
    }

    DeploymentRequestBO withBuildNumber(Integer buildNumber) {
        setBuildNumber(buildNumber)
        return this
    }

    String getService() {
        return service
    }

    void setService(String service) {
        this.service = service
    }

    DeploymentRequestBO withService(String service) {
        setService(service)
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "DeploymentRequestBO{" +
                "imageName='" + imageName + '\'' +
                ", imageVersion='" + imageVersion + '\'' +
                ", dockerRegistry=" + dockerRegistry +
                ", environment=" + environment +
                ", buildNumber=" + buildNumber +
                ", service='" + service + '\'' +
                '}';
    }
}
