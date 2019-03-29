package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.util.Logger

class Gradle implements Serializable {

    String repositoryId = "nexus"

    String repositoryUrl = "nexus"

    String params = ""

    Gradle() {
        Logger.debug("Gradle:default constructor")
    }

    String getRepositoryId() {
        return repositoryId
    }

    void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
    }

    Gradle withRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
        return this;
    }

    String getRepositoryUrl() {
        return repositoryUrl
    }

    void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl
    }

    Gradle withRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl
        return this
    }

    String getParams() {
        return params
    }

    void setParams(String params) {
        this.params = params
    }

    Gradle withParams(String params) {
        this.params = params
        return this
    }

    @Override
    public String toString() {
        return "Gradle{" +
                "repositoryId='" + repositoryId + '\'' +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
