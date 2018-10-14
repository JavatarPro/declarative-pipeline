package pro.javatar.pipeline.builder.model

class VcsRepoTO {

    String name

    String owner

    String credentialsId

    String domain

    String type

    String revisionControl

    String branch

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    VcsRepoTO withName(String name) {
        this.name = name
        return this
    }

    String getOwner() {
        return owner
    }

    void setOwner(String owner) {
        this.owner = owner
    }

    VcsRepoTO withOwner(String owner) {
        this.owner = owner
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    VcsRepoTO withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getDomain() {
        return domain
    }

    void setDomain(String domain) {
        this.domain = domain
    }

    VcsRepoTO withDomain(String domain) {
        this.domain = domain
        return this
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    VcsRepoTO withType(String type) {
        this.type = type
        return this
    }

    String getRevisionControl() {
        return revisionControl
    }

    void setRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
    }

    VcsRepoTO withRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
        return this
    }

    String getBranch() {
        return branch
    }

    void setBranch(String branch) {
        this.branch = branch
    }

    VcsRepoTO withBranch(String branch) {
        this.branch = branch
        return this
    }

    @Override
    public String toString() {
        return "VcsRepoTO{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", domain='" + domain + '\'' +
                ", type='" + type + '\'' +
                ", revisionControl='" + revisionControl + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
