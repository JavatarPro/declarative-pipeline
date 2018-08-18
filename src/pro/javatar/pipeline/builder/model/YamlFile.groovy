package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.builder.Npm

class YamlFile {

    String version

    Maven maven = new Maven()

    Npm npm = new Npm()

    List<Docker> docker = new ArrayList<>()

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
    }

    YamlFile withVersion(String version) {
        this.version = version
        return this
    }

    Maven getMaven() {
        return maven
    }

    void setMaven(Maven maven) {
        this.maven = maven
    }

    YamlFile withMaven(Maven maven) {
        this.maven = maven
        return this
    }

    Npm getNpm() {
        return npm
    }

    void setNpm(Npm npm) {
        this.npm = npm
    }

    YamlFile withNpm(Npm npm) {
        this.npm = npm
        return this
    }

    List<Docker> getDocker() {
        return docker
    }

    void setDocker(List<Docker> docker) {
        this.docker = docker
    }

    YamlFile withDocker(List<Docker> docker) {
        this.docker = docker
        return this
    }

    @Override
    public String toString() {
        return "YamlFile{" +
                "version='" + version + '\'' +
                ", maven=" + maven +
                ", npm=" + npm +
                ", docker=" + docker +
                '}';
    }
}
