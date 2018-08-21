package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.builder.Npm

class YamlConfig {

    String version

    Service service = new Service()

    Pipeline pipeline = new Pipeline()

    Maven maven = new Maven()

    Npm npm = new Npm()

    Ui ui = new Ui()

    List<Docker> docker = new ArrayList<>()

    Vcs vcs = new Vcs()

    YamlConfig populateServiceRepo() {
        service.setRepo(vcs.getRepo().get(service.getVcsRepoId()))
        return this
    }

    JenkinsTool jenkinsTool = new JenkinsTool()

    Vcs getVcs() {
        return vcs
    }

    void setVcs(Vcs vcs) {
        this.vcs = vcs
    }

    YamlConfig withVcs(Vcs vcs) {
        this.vcs = vcs
        return this
    }

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
    }

    YamlConfig withVersion(String version) {
        this.version = version
        return this
    }

    Maven getMaven() {
        return maven
    }

    void setMaven(Maven maven) {
        this.maven = maven
    }

    YamlConfig withMaven(Maven maven) {
        this.maven = maven
        return this
    }

    Npm getNpm() {
        return npm
    }

    void setNpm(Npm npm) {
        this.npm = npm
    }

    YamlConfig withNpm(Npm npm) {
        this.npm = npm
        return this
    }

    Ui getUi() {
        return ui
    }

    void setUi(Ui ui) {
        this.ui = ui
    }

    YamlConfig withUi(Ui ui) {
        this.ui = ui
        return this
    }
    
    List<Docker> getDocker() {
        return docker
    }

    void setDocker(List<Docker> docker) {
        this.docker = docker
    }

    YamlConfig withDocker(List<Docker> docker) {
        this.docker = docker
        return this
    }

    Service getService() {
        return service
    }

    void setService(Service service) {
        this.service = service
    }

    YamlConfig withService(Service service) {
        this.service = service
        return this
    }

    Pipeline getPipeline() {
        return pipeline
    }

    void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline
    }

    YamlConfig withPipeline(Pipeline pipeline) {
        this.pipeline = pipeline
        return this
    }

    JenkinsTool getJenkinsTool() {
        return jenkinsTool
    }

    void setJenkinsTool(JenkinsTool jenkinsTool) {
        this.jenkinsTool = jenkinsTool
    }

    YamlConfig withJenkinsTool(JenkinsTool jenkinsTool) {
        this.jenkinsTool = jenkinsTool
        return this
    }

    @Override
    public String toString() {
        return "YamlConfig{" +
                "version='" + version + '\'' +
                ", service=" + service +
                ", pipeline=" + pipeline +
                ", maven=" + maven +
                ", npm=" + npm +
                ", docker=" + docker +
                ", vcs=" + vcs +
                ", jenkinsTool=" + jenkinsTool +
                '}';
    }
}
