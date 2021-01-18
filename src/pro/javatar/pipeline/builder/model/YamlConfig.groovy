package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.config.Config
import pro.javatar.pipeline.config.GradleConfig
import pro.javatar.pipeline.util.LogLevel
import pro.javatar.pipeline.util.Logger
import pro.javatar.pipeline.util.StringUtils

class YamlConfig implements Config, Serializable {

    String version
    Service service = new Service()
    Pipeline pipeline = new Pipeline()
    Maven maven = new Maven()
    Python python = new Python()
    Npm npm = new Npm()
    Ui ui = new Ui()
    Docker docker = new Docker()
    S3 s3 = new S3()
    Vcs vcs = new Vcs()
    JenkinsTool jenkinsTool = new JenkinsTool()
    String orchestrationService = null
    Mesos mesos = new Mesos()
    Nomad nomad = new Nomad()
    AutoTest autoTest = new AutoTest()
    AutoTestConfig autoTestConfig;
    GradleConfig gradleConfig;
    CacheRequest cacheRequest = new CacheRequest()
    Sonar sonar = new Sonar()
    LogLevel logLevel = LogLevel.INFO

    YamlConfig() {
        Logger.debug("YamlConfig:default constructor")
    }

    @Override
    AutoTestConfig autoTestConfig() {
        return autoTestConfig;
    }

    @Override
    GradleConfig gradleConfig() {
        return gradleConfig;
    }

    YamlConfig setAutoTestConfig(AutoTestConfig autoTestConfig) {
        this.autoTestConfig = autoTestConfig
        return this;
    }

    YamlConfig setGradleConfig(GradleConfig gradleConfig) {
        this.gradleConfig = gradleConfig
        return this;
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

    Python getPython() {
        return python
    }

    void setPython(Python python) {
        this.python = python
    }

    YamlConfig withPython(Python python) {
        Logger.info("YamlConfig:withPython: ${python}")
        setPython(python)
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

    Nomad getNomad() {
        return nomad
    }

    void setNomad(Nomad nomad) {
        this.nomad = nomad
    }

    YamlConfig withNomad(Nomad nomad) {
        Logger.info("YamlConfig:withNomad: " + nomad.toString())
        setNomad(nomad)
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

    LogLevel getLogLevel() {
        return logLevel
    }

    void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel
    }

    YamlConfig withLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "YamlConfig{" +
                "version='" + version + '\'' +
                ", service=" + service +
                ", pipeline=" + pipeline +
                ", maven=" + maven +
                ", python=" + python +
                ", npm=" + npm +
                ", ui=" + ui +
                ", docker=" + docker +
                ", s3=" + s3 +
                ", vcs=" + vcs +
                ", jenkinsTool=" + jenkinsTool +
                ", orchestrationService='" + orchestrationService + '\'' +
                ", mesos=" + mesos +
                ", autoTestConfig=" + autoTest +
                ", cacheRequest=" + cacheRequest +
                '}';
    }
}
