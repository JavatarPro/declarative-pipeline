/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

/**
 * @author Borys Zora
 * @version 2021-05-15
 */
interface ArtifactReleaseInfo {

    /**
     * if current version 1.0.0-SNAPSHOT => nex development version will be 1.0.1-SNAPSHOT
     * @return next development version
     */
    String nextVersion()

    /**
     * current version that specified in build tool related files e.g. pom.xml, package.json etc.
     * in most cases expected that current version contains "-SNAPSHOT" at the end of version name
     * @return current application version
     */
    String currentVersion()

    /**
     * release version in most cases is current version without "-SNAPSHOT" suffix
     * e.g. 1.0.0 if current version was 1.0.0-SNAPSHOT
     * @return release version
     */
    String releaseVersion()

    /**
     * releaseVersionWithBuildSuffix is current releaseVersion with suffix of buildNumber
     * e.g. 1.0.0.459 if releaseVersion was 1.0.0 and buildNumber in jenkins is 459
     * this is useful for systems where while release has not been incremented
     * and need somehow to distinguish separate CI cycles
     * @return release version with build suffix
     */
    String releaseVersionWithBuildSuffix()
}