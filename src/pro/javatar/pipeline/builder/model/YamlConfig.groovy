package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.util.Logger
import pro.javatar.pipeline.util.StringUtils

class YamlConfig implements Serializable {

    String version

    Service service = new Service()

    Pipeline pipeline = new Pipeline()

    Maven maven = new Maven()

    Gradle gradle = new Gradle()

    Npm npm = new Npm()

    Ui ui = new Ui()

    Docker docker = new Docker()

    S3 s3 = new S3()

    Vcs vcs = new Vcs()

    JenkinsTool jenkinsTool = new JenkinsTool()

    String orchestrationService = null

    Mesos mesos = new Mesos()

    AutoTest autoTest = new AutoTest()

    CacheRequest cacheRequest = new CacheRequest()

    Sonar sonar = new Sonar()

    YamlConfig() {
        Logger.debug("YamlConfig:default constructor")
    }

    YamlConfig populateServiceRepo() {
        Logger.info("YamlConfig:populateServiceRepo")
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

    Gradle getGradle() {
        return gradle
    }

    void setGradle(Gradle gradle) {
        this.gradle = gradle
    }

    YamlConfig withGradle(Gradle gradle) {
        Logger.info("YamlConfig:withGradle: ${gradle}")
        setGradle(gradle)
        return this
    }

    YamlConfig withMaven(Maven maven) {
        Logger.info("YamlConfig:withMaven: ${maven}")
        setMaven(maven)
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
        Logger.info("YamlConfig:withUi: ${ui}")
        setUi(ui)
        return this
    }

    Docker getDocker() {
        return docker
    }

    void setDocker(Docker docker) {
        this.docker = docker
    }

    YamlConfig withDocker(Docker docker) {
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

    S3 getS3() {
        return s3
    }

    void setS3(S3 s3) {
        if (s3 == null) {
            return
        }
        this.s3 = s3
    }

    YamlConfig withS3(S3 s3) {
        Logger.info("YamlConfig:withS3: ${s3}")
        setS3(s3)
        return this
    }

    String getOrchestrationService() {
        return service.getOrchestration()
    }

    void setOrchestrationService(String orchestrationService) {
        if (StringUtils.isNotBlank(orchestrationService)) {
            this.orchestrationService = orchestrationService
        }
    }

    YamlConfig withOrchestrationService(String orchestrationService) {
        setOrchestrationService(orchestrationService)
        return this
    }

    Mesos getMesos() {
        return mesos
    }

    void setMesos(Mesos mesos) {
        this.mesos = mesos
    }

    YamlConfig withMesos(Mesos mesos) {
        Logger.info("YamlConfig:withMesos: ${mesos}")
        setMesos(mesos)
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

    CacheRequest getCacheRequest() {
        return cacheRequest
    }

    void setCacheRequest(CacheRequest cacheRequest) {
        this.cacheRequest = cacheRequest
    }

    YamlConfig withCacheRequest(CacheRequest cacheRequest) {
        this.cacheRequest = cacheRequest
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
                ", cacheRequest=" + cacheRequest +
                '}';
    }
}
