package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.util.Logger

class OrchestrationRequest implements Serializable {

    String imageName

    String imageVersion

    String env

    String templateFolder

    List<String> templateFiles = new ArrayList<>()

    String service

    String buildNumber

    Map<String, Object> templateVariables = new HashMap<>()

    OrchestrationRequest() {
        Logger.debug("OrchestrationRequest default constructor")
    }

    String getImageName() {
        return imageName
    }

    OrchestrationRequest withImageName(String imageName) {
        setImageName(imageName)
        return this
    }

    String getImageVersion() {
        return imageVersion
    }

    OrchestrationRequest withImageVersion(String imageVersion) {
        setImageVersion(imageVersion)
        return this
    }

    String getEnv() {
        return env
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
        this.templateFiles = templateFiles
        return this
    }

    String getService() {
        return service
    }

    OrchestrationRequest withService(String service) {
        setService(service)
        return this
    }

    String getBuildNumber() {
        return buildNumber
    }

    OrchestrationRequest withBuildNumber(String buildNumber) {
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

    void setImageName(String imageName) {
        templateVariables.put("imageName", imageName)
        this.imageName = imageName
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

    void setBuildNumber(String buildNumber) {
        templateVariables.put("buildNumber", buildNumber)
        this.buildNumber = buildNumber
    }

    void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables
    }

    @Override
    public String toString() {
        return "OrchestrationRequest{" +
                "imageName='" + imageName + '\'' +
                ", imageVersion='" + imageVersion + '\'' +
                ", env='" + env + '\'' +
                ", templateFolder='" + templateFolder + '\'' +
                ", templateFiles=" + templateFiles.size() +
                ", service='" + service + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", templateVariables=" + templateVariables.size() +
                '}';
    }
}
