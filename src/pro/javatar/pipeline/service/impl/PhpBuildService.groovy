package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class PhpBuildService extends BuildService {

    DockerService dockerService;

    String applicationFile = "composer.json"

    PhpBuildService(DockerService dockerService) {
        this.dockerService = dockerService
        dsl.echo "PhpBuildService constructor"
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
        dsl.echo 'PhpBuildService buildAndUnitTests start'
        dsl.sh "pwd; ls -la; ls -la *"
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        dsl.echo 'PhpBuildService buildAndUnitTests end'
    }

    @Override
    String getCurrentVersion() {
        def applicationFileJson = dsl.readJSON file: applicationFile
        return applicationFileJson.version
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        dsl.echo "setupReleaseVersion: ${releaseVersion} started"
        String currentVersion = getCurrentVersion()
        replace(currentVersion, releaseVersion, applicationFile)
        dsl.echo "setupReleaseVersion: ${releaseVersion} finished"
    }

    @Override
    def setupVersion(String version) {
        dsl.echo "setupVersion: ${version} started"
        String currentVersion = getCurrentVersion()
        replace(currentVersion, version, applicationFile)
        dsl.echo "setupVersion: ${version} finished"
    }

}
