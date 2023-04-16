/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model;

import pro.javatar.pipeline.config.GradleConfig

import static java.lang.String.format
import static pro.javatar.pipeline.util.StringUtils.isBlank;

/**
 * @author Borys Zora
 * @version 2019-11-17
 */
@Deprecated
public class GradleConfigTO implements GradleConfig {

    private String gradleTool;

    private String javaTool;

    private String additionalBuildParameters;

    private String versionFile;

    private String repositoryUrl;

    private String repositoryId;

    private String repositoryCredentialsId;

    private GradleConfigTO() {}

    static GradleConfigTO ofGradleYmlConfigAndJenkinsTool(def gradle, JenkinsTool tool) {
        return new GradleConfigTO()
                .setGradleTool(tool.getGradle())
                .setJavaTool(tool.getJava())
                .setVersionFile(retrieveVersionFile(gradle))
                .setAdditionalBuildParameters(retrieveAdditionalBuildParameters(gradle))
                .setRepositoryId(retrieveRepositoryId(gradle))
                .setRepositoryUrl(retrieveRepositoryUrl(gradle))
                .setRepositoryCredentialsId(retrieveRepositoryCredentialsId(gradle))
                .validate()
    }

    @Override
    public String gradleTool() {
        return gradleTool;
    }

    @Override
    public String javaTool() {
        return javaTool;
    }

    @Override
    public String additionalBuildParameters() {
        return additionalBuildParameters;
    }

    @Override
    public String versionFile() {
        return versionFile;
    }

    @Override
    public String repositoryUrl() {
        return repositoryUrl;
    }

    @Override
    public String repositoryId() {
        return repositoryId;
    }

    @Override
    String repositoryCredentialsId() {
        return repositoryCredentialsId
    }

    @Override
    String publishParams() {
        String params = repositoryUrl != null ? format("-P%s=%s ", REPO_VARIABLE_BUILD_GRADLE, repositoryUrl) : "";
         if (repositoryCredentialsId != null) {
             params += format("-P%s=%s -P%s=%s", USER_VARIABLE_BUILD_GRADLE, USER_VARIABLE_TO_BE_REPLACED,
                     PASSWORD_VARIABLE_BUILD_GRADLE, PASSWORD_VARIABLE_TO_BE_REPLACED)
         }
        return params;
    }

    GradleConfigTO setGradleTool(String gradleTool) {
        this.gradleTool = gradleTool;
        return this;
    }

    GradleConfigTO setJavaTool(String javaTool) {
        this.javaTool = javaTool;
        return this;
    }

    GradleConfigTO setAdditionalBuildParameters(String additionalBuildParameters) {
        this.additionalBuildParameters = additionalBuildParameters;
        return this;
    }

    GradleConfigTO setVersionFile(String versionFile) {
        this.versionFile = versionFile;
        return this;
    }

    GradleConfigTO setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        return this;
    }

    GradleConfigTO setRepositoryCredentialsId(String repositoryCredentialsId) {
        this.repositoryCredentialsId = repositoryCredentialsId
        return this;
    }

    GradleConfigTO setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    GradleConfigTO validate() {
        // TODO
        return this;
    }

    // helper retrieval methods

    private static String retrieveAdditionalBuildParameters(def gradle) {
        String gradleParams = gradle.params;
        if (isBlank(gradleParams)) {
            return "";
        }
        return gradleParams.trim();
    }

    private static String retrieveVersionFile(def gradle) {
        String versionFile = gradle.versionFile;
        if (isBlank(versionFile)) {
            return DEFAULT_VERSION_FILE;
        }
        return versionFile;
    }

    private static String retrieveRepositoryUrl(def gradle) {
        if (gradle.repository == null || isBlank(gradle.repository.url)) {
            return ""
        }
        return gradle.repository.url;
    }

    private static String retrieveRepositoryCredentialsId(def gradle) {
        if (gradle.repository == null || isBlank(gradle.repository.credentialsId)) {
            return ""
        }
        return gradle.repository.credentialsId;
    }

    private static String retrieveRepositoryId(def gradle) {
        if (gradle.repository == null || isBlank(gradle.repository.id)) {
            return ""
        }
        return gradle.repository.id;
    }
}
