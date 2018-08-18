package pro.javatar.pipeline.builder

class YamlModel {

    String version

    Maven maven = new Maven()

    Npm npm = new Npm()

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

    @Override
    public String toString() {
        return "YamlModel{" +
                "npm=" + npm +
                '}';
    }
}
