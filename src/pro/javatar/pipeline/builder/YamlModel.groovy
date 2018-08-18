package pro.javatar.pipeline.builder

class YamlModel {

    String version

    Npm npm = new Npm()

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
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
