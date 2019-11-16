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

    String gradleTool();

    String javaTool();

    String additionalBuildParameters();

    String versionFile();

    String repositoryUrl();

    String repositoryId();
}
