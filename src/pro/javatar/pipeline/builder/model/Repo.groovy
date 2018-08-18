package pro.javatar.pipeline.builder.model

class Repo {

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

    Repo withName(String name) {
        this.name = name
        return this
    }

    String getOwner() {
        return owner
    }

    void setOwner(String owner) {
        this.owner = owner
    }

    Repo withOwner(String owner) {
        this.owner = owner
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    Repo withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getDomain() {
        return domain
    }

    void setDomain(String domain) {
        this.domain = domain
    }

    Repo withDomain(String domain) {
        this.domain = domain
        return this
    }

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    Repo withType(String type) {
        this.type = type
        return this
    }

    String getRevisionControl() {
        return revisionControl
    }

    void setRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
    }

    Repo withRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
        return this
    }

    @Override
    public String toString() {
        return "Repo{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", domain='" + domain + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
