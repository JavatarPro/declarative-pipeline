package pro.javatar.pipeline.builder.model

class Maven {

    String repositoryId = "nexus"

    String repositoryUrl = "nexus"

    String params

    String getRepositoryId() {
        return repositoryId
    }

    void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
    }

    Maven withRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
        return this
    }

    String getRepositoryUrl() {
        return repositoryUrl
    }

    void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl
    }

    Maven withRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl
        return this
    }

    String getParams() {
        return params
    }

    void setParams(String params) {
        this.params = params
    }

    Maven withParams(String params) {
        this.params = params
        return this
    }

    @Override
    public String toString() {
        return "Maven{" +
                "repositoryId='" + repositoryId + '\'' +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
