/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
class DockerRegistry {

    String credentialsId

    String registry

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

    @Override
    public String toString() {
        return "DockerRegistryBO {" +
                "credentialsId='" + credentialsId + '\'' +
                ", registry='" + registry + '\'' +
                '}';
    }

}
