package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class VcsAndDockerRelease implements ReleaseService {

    BuildService buildService
    RevisionControlService revisionControlService
    DockerService dockerService

    VcsAndDockerRelease(BuildService buildService, RevisionControlService revisionControlService,
                        DockerService dockerService) {
        this.buildService = buildService
        this.revisionControlService = revisionControlService
        this.dockerService = dockerService
    }

    @Override
    def release(ReleaseInfo releaseInfo) {
        Logger.info("VcsAndDockerRelease start release: " + releaseInfo.toString())
        validateReleaseVersion(releaseInfo.releaseVersion())
        dsl.parallel 'release revision control': {
            Logger.info("VcsAndDockerRelease releaseRevisionControl() started")
            revisionControlService.release(releaseInfo.releaseVersion())
            revisionControlService.switchToDevelopBranch()
            buildService.setupVersion(releaseInfo.nextVersion())
            revisionControlService.commitChanges("Update version to ${releaseInfo.nextVersion()}")
            revisionControlService.pushRelease()
            Logger.info("VcsAndDockerRelease releaseRevisionControl() finished")
        }, 'release docker artifacts': {
            dockerService.dockerPushImageToProdRegistry(releaseInfo.serviceName, releaseInfo.releaseVersion())
        }
        Logger.info("VcsAndDockerRelease end release")
    }

    def validateReleaseVersion(String releaseVersion) {
        Logger.info("VcsAndDockerRelease validateReleaseVersion: ${releaseVersion}")
        String currentVersion = buildService.getCurrentVersion()
        if (currentVersion.equalsIgnoreCase(releaseVersion)) {
            Logger.info("release version: ${releaseVersion} successfully validated")
        } else {
            String errorMsg = "release version: ${releaseVersion} is not valid, current version is ${currentVersion}"
            Logger.error(errorMsg)
            throw new InvalidReleaseNumberException(errorMsg)
        }
    }
}
