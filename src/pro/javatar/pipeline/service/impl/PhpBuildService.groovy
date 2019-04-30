package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class PhpBuildService extends BuildService {

    DockerService dockerService;

    String applicationFile = "package.json"

    PhpBuildService(DockerService dockerService) {
        this.dockerService = dockerService
        Logger.debug("PhpBuildService constructor")
    }

    PhpBuildService(DockerService dockerService, String applicationFile) {
        this.dockerService = dockerService
        this.applicationFile = applicationFile
    }

    @Override
    void setUp() {
        dsl.sh "env"
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('PhpBuildService buildAndUnitTests start')
        dsl.sh "pwd; ls -la; ls -la *"
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        Logger.info('PhpBuildService buildAndUnitTests end')
    }

    @Override
    String getCurrentVersion() {
        def applicationFileJson = dsl.readJSON file: applicationFile
        return applicationFileJson.version
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        Logger.info("setupReleaseVersion: ${releaseVersion} started")
        String currentVersion = getCurrentVersion()
        FileUtils.replace(currentVersion, releaseVersion, applicationFile)
        Logger.info("setupReleaseVersion: ${releaseVersion} finished")
    }

    @Override
    def setupVersion(String version) {
        Logger.info("setupVersion: ${version} started")
        String currentVersion = getCurrentVersion()
        FileUtils.replace(currentVersion, version, applicationFile)
        Logger.info("setupVersion: ${version} finished")
    }

}
