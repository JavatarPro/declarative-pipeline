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

    Maven getMaven() {
        return maven
    }

    void setMaven(Maven maven) {
        this.maven = maven
    }

    Npm getNpm() {
        return npm
    }

    void setNpm(Npm npm) {
        this.npm = npm
    }

    List<Docker> getDocker() {
        return docker
    }

    void setDocker(List<Docker> docker) {
        this.docker = docker
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
