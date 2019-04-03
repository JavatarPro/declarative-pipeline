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

    void setImageName(String imageName) {
        this.imageName = imageName
    }

    String getImageVersion() {
        return imageVersion
    }

    void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion
    }

    String getEnv() {
        return env
    }

    void setEnv(String env) {
        this.env = env
    }

    String getTemplateFolder() {
        return templateFolder
    }

    void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder
    }

    List<String> getTemplateFiles() {
        return templateFiles
    }

    void setTemplateFiles(List<String> templateFiles) {
        this.templateFiles = templateFiles
    }

    String getService() {
        return service
    }

    void setService(String service) {
        this.service = service
    }

    String getBuildNumber() {
        return buildNumber
    }

    void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber
    }

    Map<String, Object> getTemplateVariables() {
        return templateVariables
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
