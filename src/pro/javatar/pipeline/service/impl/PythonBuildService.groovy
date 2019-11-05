package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.exception.MalformedReleaseVersionException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class PythonBuildService extends BuildService {

    DockerService dockerService

    String versionFile

    String versionParameter

    String projectDirectory

    PythonBuildService(DockerService dockerService, String versionFile, String versionParameter, String projectDirectory) {
        this.dockerService = dockerService
        this.versionFile = versionFile
        this.versionParameter = versionParameter
        this.projectDirectory = projectDirectory
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info("PythonBuildService buildAndUnitTests started")
        dsl.sh "python -m unittest discover -s tests"
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        Logger.info("PythonBuildService buildAndUnitTests finished")
    }

    @Override
    void setUp() {
        Logger.debug("PythonBuildService setUp started")
        dsl.sh "python --version"
        dsl.sh "apt-get -y install python-pip python-setuptools"
        dsl.sh "pip install -r requirements.txt"
        Logger.debug("PythonBuildService setUp finished")
    }

    @Override
    String getCurrentVersion() {
        Logger.debug("PythonBuildService getCurrentVersion started")
        Logger.info("expected that in properties project version has variable with name: version")
        String versionFilePath = "${projectDirectory}/${versionFile}"
        String version = dsl.sh returnStdout: true, script: "grep ^${versionParameter} ${versionFilePath} | sed -n 's/.*\"\\([^\"]*\\)\"/\\1/p' "
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
        String versionFilePath = "${projectDirectory}/${versionFile}"
        Logger.trace("PythonBuildService ${versionFilePath} before version setup")
        dsl.sh "cat ${versionFilePath}"
        //todo: replace with new version
        FileUtils.replace(currentVersion, version, versionFilePath)
        Logger.trace("PythonBuildService ${versionFilePath} after version setup")
        dsl.sh "cat ${versionFilePath}"
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
