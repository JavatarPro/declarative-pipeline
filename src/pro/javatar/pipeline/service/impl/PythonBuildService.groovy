package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService

class PythonBuildService extends BuildService {

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {

    }

    @Override
    void setUp() {

    }

    @Override
    String getCurrentVersion() {
        return null
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        return null
    }

    @Override
    def setupVersion(String version) {
        return null
    }
}
