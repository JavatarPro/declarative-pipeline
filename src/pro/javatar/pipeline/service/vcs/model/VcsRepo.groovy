package pro.javatar.pipeline.service.vcs.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.model.RevisionControlType
import pro.javatar.pipeline.model.VcsRepositoryType

class VcsRepo {

    String name

    String owner

    String credentialsId

    String domain

    private VcsRepositoryType type

    RevisionControlType revisionControlType

    boolean ssh = true

    String branch

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    VcsRepo withName(String name) {
        this.name = name
        return this
    }

    String getOwner() {
        return owner
    }

    void setOwner(String owner) {
        this.owner = owner
    }

    VcsRepo withOwner(String owner) {
        this.owner = owner
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    VcsRepo withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getDomain() {
        return domain
    }

    void setDomain(String domain) {
        this.domain = domain
    }

    VcsRepo withDomain(String domain) {
        this.domain = domain
        return this
    }

    VcsRepositoryType getType() {
        return type;
    }

    void setType(VcsRepositoryType type) {
        this.type = type;
    }

    void setType(String type) {
        setType(VcsRepositoryType.fromString(type));
    }

    VcsRepo withType(VcsRepositoryType type) {
        setType(type);
        return this;
    }

    boolean isSsh() {
        return ssh
    }

    void setSsh(boolean ssh) {
        this.ssh = ssh
    }

    VcsRepo withSsh(boolean ssh) {
        this.ssh = ssh
        return this
    }

    boolean getSsh() {
        return ssh
    }

    String getBranch() {
        return branch
    }

    void setBranch(String branch) {
        this.branch = branch
    }

    VcsRepo withBranch(String branch) {
        this.branch = branch
        return this
    }

    RevisionControlType getRevisionControlType() {
        return revisionControlType
    }

    void setRevisionControlType(RevisionControlType revisionControlType) {
        this.revisionControlType = revisionControlType
    }

    void setRevisionControlType(String revisionControlType) {
        this.revisionControlType = RevisionControlType.fromString(revisionControlType)
    }

    VcsRepo withRevisionControlType(RevisionControlType revisionControlType) {
        this.revisionControlType = revisionControlType
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "VcsRepo{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", domain='" + domain + '\'' +
                ", type=" + type +
                ", ssh=" + ssh +
                '}';
    }
}
