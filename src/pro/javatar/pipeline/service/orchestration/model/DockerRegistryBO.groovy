/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
class DockerRegistryBO {

    String credentialsId

    String registry

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

    @Override
    public String toString() {
        return "DockerRegistryBO {" +
                "credentialsId='" + credentialsId + '\'' +
                ", registry='" + registry + '\'' +
                '}';
    }

}

