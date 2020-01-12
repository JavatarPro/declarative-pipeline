/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

/**
 * @author Borys Zora
 * @version 2019-11-13
 */
interface GradleConfig extends Serializable {

    String DEFAULT_VERSION_FILE = "gradle.properties";

    String REPO_VARIABLE_BUILD_GRADLE = "repoUrl";

    String USER_VARIABLE_BUILD_GRADLE = "repoUser";

    String PASSWORD_VARIABLE_BUILD_GRADLE = "repoPassword";

    String USER_VARIABLE_TO_BE_REPLACED = "USERNAME";

    String  PASSWORD_VARIABLE_TO_BE_REPLACED = "PASSWORD";

    String gradleTool();

    String javaTool();

    String additionalBuildParameters();

    String versionFile();

    String repositoryUrl();

    String repositoryId();

    String repositoryCredentialsId();

    String publishParams();
}
