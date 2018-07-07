package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService

class PythonBuildService extends BuildService {

    DockerService dockerService;

    PythonBuildService(DockerService dockerService) {
        this.dockerService = dockerService
    }

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
