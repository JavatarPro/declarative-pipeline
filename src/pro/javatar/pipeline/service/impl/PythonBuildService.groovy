package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.exception.MalformedReleaseVersionException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class PythonBuildService extends BuildService {

    DockerService dockerService;

    String versionFile

    String versionParameter

    String projectDirectory

    PythonBuildService(DockerService dockerService) {
        this.dockerService = dockerService
    }

    PythonBuildService(String versionFile, String versionParameter, String projectDirectory) {
        this.versionFile = versionFile
        this.versionParameter = versionParameter
        this.projectDirectory = projectDirectory
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info("PythonBuildService buildAndUnitTests started")
        dsl.sh "python -m compileall -f ./"
        Logger.info("PythonBuildService buildAndUnitTests finished")
    }

    @Override
    void setUp() {
        Logger.debug("PythonBuildService setUp started")
        dsl.sh "python --version"
        Logger.debug("PythonBuildService setUp finished")
    }

    @Override
    String getCurrentVersion() {
        Logger.debug("PythonBuildService getCurrentVersion started")
        Logger.info("expected that in properties project version has variable with name: version")
        String version = dsl.sh returnStdout: true, script: "grep ^${versionParameter} ${versionFile} | sed -n 's/.*\"\\([^\"]*\\)\"/\\1/p' "
        Logger.info("PythonBuildService:getCurrentVersion:result: ${version}")
        Logger.debug("PythonBuildService getCurrentVersion finished")
        return version.trim()
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        Logger.debug("PythonBuildService setupReleaseVersion: ${releaseVersion} started")
        if (releaseVersion.contains("SNAPSHOT")) {
            String msg = "Release version must not contain SNAPSHOT, but was: ${releaseVersion}"
            Logger.error(msg)
            throw new MalformedReleaseVersionException(msg)
        }
        setupVersion(releaseVersion)
        Logger.debug("PythonBuildService setupReleaseVersion: ${releaseVersion} finished")
    }

    @Override
    def setupVersion(String version) {
        Logger.info("PythonBuildService setupVersion: ${version} started")
        String currentVersion = getCurrentVersion()
        Logger.trace("PythonBuildService ${versionFile} before version setup")
        dsl.sh "cat ${versionFile}"
        //todo: replace with new version
        FileUtils.replace(currentVersion, version, versionFile)
        Logger.trace("PythonBuildService ${versionFile} after version setup")
        dsl.sh "cat ${versionFile}"
        Logger.info("PythonBuildService setupVersion: ${version} finished")
    }


    @Override
    public String toString() {
        return "PythonBuildService{" +
                ", versionFile='" + versionFile + '\'' +
                ", versionParameter='" + versionParameter + '\'' +
                ", projectDirectory='" + projectDirectory + '\'' +
                '}';
    }
}
