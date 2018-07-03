package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class PhpBuildService extends BuildService {

    String applicationFile = "composer.json"

    PhpBuildService() {
        dsl.echo "PhpBuildService constructor"
    }

    PhpBuildService(String applicationFile) {
        this.applicationFile = applicationFile
    }

    @Override
    void setUp() {
        dsl.sh "env | grep -i php"
        dsl.sh "env | grep -i symfony"
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        dsl.echo 'PhpBuildService buildAndUnitTests start'
        dsl.sh "pwd; ls -la; ls -la *"
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
