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

    List<S3> s3 = new ArrayList<>()

    Vcs vcs = new Vcs()

    JenkinsTool jenkinsTool = new JenkinsTool()

    String orchestrationService

    Mesos mesos = new Mesos()

    AutoTest autoTest = new AutoTest()

    Sonar sonar = new Sonar()

    YamlConfig populateServiceRepo() {
        service.setRepo(vcs.getRepo().get(service.getVcsRepoId()))
        return this
    }

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

    List<S3> getS3() {
        return s3
    }

    void setS3(List<S3> s3) {
        this.s3 = s3
    }

    YamlConfig withS3(List<S3> s3) {
        this.s3 = s3
        return this
    }

    YamlConfig addS3(S3 s3) {
        this.s3.add(s3)
        return this
    }

    String getOrchestrationService() {
        return orchestrationService
    }

    void setOrchestrationService(String orchestrationService) {
        this.orchestrationService = orchestrationService
    }

    YamlConfig withOrchestrationService(String orchestrationService) {
        this.orchestrationService = orchestrationService
        return this
    }

    Mesos getMesos() {
        return mesos
    }

    void setMesos(Mesos mesos) {
        this.mesos = mesos
    }

    YamlConfig withMesos(Mesos mesos) {
        this.mesos = mesos
        return this
    }

    AutoTest getAutoTest() {
        return autoTest
    }

    void setAutoTest(AutoTest autoTest) {
        this.autoTest = autoTest
    }

    YamlConfig withAutoTest(AutoTest autoTest) {
        this.autoTest = autoTest
        return this
    }

    Sonar getSonar() {
        return sonar
    }

    void setSonar(Sonar sonar) {
        this.sonar = sonar
    }

    YamlConfig withSonar(Sonar sonar) {
        this.sonar = sonar
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
                ", ui=" + ui +
                ", docker=" + docker +
                ", s3=" + s3 +
                ", vcs=" + vcs +
                ", jenkinsTool=" + jenkinsTool +
                ", orchestrationService='" + orchestrationService + '\'' +
                ", mesos=" + mesos +
                ", autoTest=" + autoTest +
                '}';
    }
}
