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
        this.imageName = imageName
        return this
    }

    String getImageVersion() {
        return imageVersion
    }

    OrchestrationRequest withImageVersion(String imageVersion) {
        this.imageVersion = imageVersion
        return this
    }

    String getEnv() {
        return env
    }

    OrchestrationRequest withEnv(String env) {
        this.env = env
        return this
    }

    String getTemplateFolder() {
        return templateFolder
    }

    void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder
    }

    OrchestrationRequest withTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder
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
        this.service = service
        return this
    }

    String getBuildNumber() {
        return buildNumber
    }

    OrchestrationRequest withBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber
        return this
    }

    Map<String, Object> getTemplateVariables() {
        return templateVariables
    }

    OrchestrationRequest withTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables
        return this
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
