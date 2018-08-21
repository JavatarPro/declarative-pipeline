package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.model.BuildServiceType
import pro.javatar.pipeline.model.UiDeploymentType

enum JenkinsBuildParams {

    PROFILES("profile", ""),

    VERSION("version", "1.0"),

    SERVICE_NAME("service.name", ""),
    SERVICE_BUILD_TYPE("service.buildType", BuildServiceType.MAVEN),
    SERVICE_BUILD_NUMBER_FOR_VERSION("service.useBuildNumberForVersion", false),
    SERVICE_REPO("service.vcs.repo", false),

    UI_DEPLOYMENT_TYPE("ui.deploymentType", UiDeploymentType.AWS_S3),

    PIPELINE_PIPELINE_SUIT("pipeline.pipelineSuit", "service"),
    PIPELINE_STAGES("pipeline.stages", new ArrayList<>()),

    MAVEN_PARAMS("maven.params", ""),
    MAVEN_REPOSITORY_ID("maven.repository.id", "nexus"),
    MAVEN_REPOSITORY_URL("maven.repository.url", ""),

    NPM_DISTRIBUTION_FOLDER("npm.distributionFolder", "dist"),

    ORCHESTRATION_SERVICE("orchestrationService", "mesos"),

    JENKINS_TOOL_MAVEN("jenkins_tool.maven", "maven"),
    JENKINS_TOOL_JAVA("jenkins_tool.java", "jdk"),
    JENKINS_TOOL_NPM_VERSION("jenkins_tool.npm.version", "nodejs"),
    JENKINS_TOOL_NPM_TYPE("jenkins_tool.npm.type", "nodejs")

    // instance variables

    private def defaultValue

    private String key

    JenkinsBuildParams(String key, def defaultValue){
        this.key = key
        this.defaultValue = defaultValue
    }

    def getDefaultValue() {
        return defaultValue
    }

    String getKey() {
        return key
    }

    static boolean hasKey(String key) {
        for (JenkinsBuildParams param: JenkinsBuildParams.values()) {
            if (param.getKey().equalsIgnoreCase(key)) {
                return true
            }
        }
        return false
    }
}
