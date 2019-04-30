package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.util.Logger

class OrchestrationRequest implements Serializable {

    String env

    String dockerRegistry

    String dockerImage

    String templateFolder

    List<String> templateFiles = new ArrayList<>()

    String service

    Integer buildNumber

    Map<String, Object> templateVariables = new HashMap<>()

    OrchestrationRequest() {
        Logger.debug("OrchestrationRequest default constructor")
    }

    String getEnv() {
        return env
    }

    String getDockerRegistry() {
        return dockerRegistry
    }

    String getDockerImage() {
        return dockerImage
    }

    OrchestrationRequest withEnv(String env) {
        setEnv(env)
        return this
    }

    String getTemplateFolder() {
        return templateFolder
    }

    void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder
    }

    OrchestrationRequest withTemplateFolder(String templateFolder) {
        setTemplateFolder(templateFolder)
        return this
    }

    List<String> getTemplateFiles() {
        return templateFiles
    }

    OrchestrationRequest withTemplateFiles(List<String> templateFiles) {
        setTemplateFiles(templateFiles)
        return this
    }

    String getService() {
        return service
    }

    OrchestrationRequest withService(String service) {
        setService(service)
        return this
    }

    Integer getBuildNumber() {
        return buildNumber
    }

    OrchestrationRequest withBuildNumber(Integer buildNumber) {
        setBuildNumber(buildNumber)
        return this
    }

    Map<String, Object> getTemplateVariables() {
        return templateVariables
    }

    OrchestrationRequest withTemplateVariables(Map<String, Object> templateVariables) {
        setTemplateVariables(templateVariables)
        return this
    }

    void setImageVersion(String imageVersion) {
        templateVariables.put("imageVersion", imageVersion)
        this.imageVersion = imageVersion
    }

    void setEnv(String env) {
        templateVariables.put("env", env)
        this.env = env
    }

    void setTemplateFiles(List<String> templateFiles) {
        this.templateFiles = templateFiles
    }

    void setService(String service) {
        templateVariables.put("service", service)
        this.service = service
    }

    void setBuildNumber(Integer buildNumber) {
        templateVariables.put("buildNumber", buildNumber)
        this.buildNumber = buildNumber
    }

    void setDockerRegistry(String dockerRegistry) {
        templateVariables.put("dockerRegistry", dockerRegistry)
        this.dockerRegistry = dockerRegistry
    }

    OrchestrationRequest withDockerRegistry(String dockerRegistry) {
        setDockerRegistry(dockerRegistry)
        return this
    }

    void setDockerImage(String dockerImage) {
        templateVariables.put("dockerImage", dockerImage)
        this.dockerImage = dockerImage
    }

    OrchestrationRequest withDockerImage(String dockerImage) {
        setDockerImage(dockerImage)
        return this
    }

    void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables
    }

    @Override
    public String toString() {
        return "OrchestrationRequest{" +
                "dockerRegistry='" + dockerRegistry + '\'' +
                ", dockerImage='" + dockerImage + '\'' +
                ", env='" + env + '\'' +
                ", templateFolder='" + templateFolder + '\'' +
                ", templateFiles=" + templateFiles.size() +
                ", service='" + service + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", templateVariables=" + templateVariables.size() +
                '}';
    }
}
