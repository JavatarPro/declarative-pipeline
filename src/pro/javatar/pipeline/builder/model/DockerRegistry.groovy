/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
class DockerRegistry implements Serializable {

    String credentialsId

    String registry

    DockerRegistry() {
        Logger.debug("DockerRegistry:default constructor")
    }

    DockerRegistry(String credentialsId, String registry) {
        Logger.debug("DockerRegistry: credentialsId: ${credentialsId}, registry: ${registry}")
        this.credentialsId = credentialsId
        this.registry = registry
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    DockerRegistry withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getRegistry() {
        return registry
    }

    void setRegistry(String registry) {
        this.registry = registry
    }

    DockerRegistry withRegistry(String registry) {
        this.registry = registry
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "DockerRegistry {" +
                "credentialsId='" + credentialsId + '\'' +
                ", registry='" + registry + '\'' +
                '}';
    }

}
