package pro.javatar.pipeline.builder.model

class Gradle {

    String repositoryId = "nexus"

    String repositoryUrl = "nexus"

    String params = ""

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
