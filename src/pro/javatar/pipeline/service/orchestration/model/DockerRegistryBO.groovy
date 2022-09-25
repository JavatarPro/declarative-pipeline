/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

import com.cloudbees.groovy.cps.NonCPS

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
@Deprecated // TODO remove in favor to domain.Docker
class DockerRegistryBO {

    String registry
    String credentialsId

    DockerRegistryBO() {}

    DockerRegistryBO(String registry, String credentialsId) {
        this.registry = registry
        this.credentialsId = credentialsId
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    DockerRegistryBO withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getRegistry() {
        return registry
    }

    void setRegistry(String registry) {
        this.registry = registry
    }

    DockerRegistryBO withRegistry(String registry) {
        this.registry = registry
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "DockerRegistryBO {" +
                "credentialsId='" + credentialsId + '\'' +
                ", registry='" + registry + '\'' +
                '}';
    }

}

