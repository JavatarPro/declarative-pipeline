package pro.javatar.pipeline.builder.model

class VcsRepo {

    String name

    String owner

    String credentialsId

    String domain

    String type

    String revisionControl

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

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    VcsRepo withType(String type) {
        this.type = type
        return this
    }

    String getRevisionControl() {
        return revisionControl
    }

    void setRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
    }

    VcsRepo withRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
        return this
    }

    @Override
    public String toString() {
        return "VcsRepo{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", domain='" + domain + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
