/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

import pro.javatar.pipeline.model.ReleaseInfo

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
class SeparateFileBasedRelease implements CurrentVersionAware, ReleaseVersionAware, SetupVersionAware {

    private static String DEFAULT_VERSION_FILE_NAME = ".javatar/release.yml"
    private String versionFileName

    SeparateFileBasedRelease(String versionFileName) {
        this.versionFileName = versionFileName
    }

    SeparateFileBasedRelease() {
        this(DEFAULT_VERSION_FILE_NAME)
    }

    @Override
    String getCurrentVersion() {
        return null
    }

    @Override
    String getReleaseVersion(ReleaseInfo releaseInfo) {
        return null
    }

    @Override
    def setupVersion(String version) {
        return null
    }
}
