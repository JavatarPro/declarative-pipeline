package pro.javatar.pipeline.service.vcs.model

import pro.javatar.pipeline.model.RevisionControlType

class VscCheckoutRequest implements Serializable {

    private String repoUrl

    private String credentialsId

    private String branch = "develop"

    private RevisionControlType revisionControlType = RevisionControlType.GIT

    String getRepoOwner() {
        return repoOwner
    }

    void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner
    }

    VscCheckoutRequest withRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner
        return this
    }

    String getRepo() {
        return repo
    }

    void setRepo(String repo) {
        this.repo = repo
    }

    VscCheckoutRequest withRepo(String repo) {
        this.repo = repo
        return this
    }

    String getRepoUrl() {
        return repoUrl
    }

    void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl
    }

    VscCheckoutRequest withRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    VscCheckoutRequest withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    boolean getSsh() {
        return ssh
    }

    boolean isSsh() {
        return ssh
    }

    void setSsh(boolean ssh) {
        this.ssh = ssh
    }

    VscCheckoutRequest withSsh(boolean ssh) {
        this.ssh = ssh
        return this
    }

    String getBranch() {
        return branch
    }

    void setBranch(String branch) {
        this.branch = branch
    }

    VscCheckoutRequest withBranch(String branch) {
        this.branch = branch
        return this
    }

    RevisionControlType getRevisionControlType() {
        return revisionControlType
    }

    void setRevisionControlType(RevisionControlType revisionControlType) {
        this.revisionControlType = revisionControlType
    }

    VscCheckoutRequest withRevisionControlType(RevisionControlType revisionControlType) {
        this.revisionControlType = revisionControlType
        return this
    }

    @Override
    public String toString() {
        return "VscCheckoutRequest{" +
                "repoUrl='" + repoUrl + '\'' +
                ", repoOwner='" + repoOwner + '\'' +
                ", repo='" + repo + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", ssh=" + ssh +
                ", branch='" + branch + '\'' +
                ", revisionControlType=" + revisionControlType +
                '}';
    }
}
