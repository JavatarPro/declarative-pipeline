package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.vcs.RevisionControlService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class VcsAndDockerRelease implements ReleaseService {

    BuildService buildService
    RevisionControlService revisionControlService
    DockerService dockerService

    VcsAndDockerRelease(BuildService buildService, RevisionControlService revisionControlService,
                        DockerService dockerService) {
        this.buildService = buildService
        this.revisionControlService = revisionControlService
    }

    @Override
    def release(ReleaseInfo releaseInfo) {
        dsl.echo "VcsAndDockerRelease start release: ${releaseInfo.toString()}"
        validateReleaseVersion(releaseInfo.releaseVersion)
        dsl.parallel 'release revision control': {
            dsl.echo "VcsAndDockerRelease releaseRevisionControl() started"
            revisionControlService.release(releaseInfo.releaseVersion)
            revisionControlService.switchToDevelopBranch()
            buildService.setupVersion(releaseInfo.developVersion)
            revisionControlService.commitChanges("Update version to ${releaseInfo.developVersion}")
            revisionControlService.pushRelease()
            dsl.echo "VcsAndDockerRelease releaseRevisionControl() finished"
        }, 'release docker artifacts': {
            dockerService.dockerPushImageToProdRegistry(releaseInfo.serviceName, releaseInfo.releaseVersion)
        }
        dsl.echo "VcsAndDockerRelease end release"
    }

    def validateReleaseVersion(String releaseVersion) {
        dsl.echo "VcsAndDockerRelease validateReleaseVersion: ${releaseVersion}"
        String currentVersion = buildService.getCurrentVersion()
        if (currentVersion.equalsIgnoreCase(releaseVersion)) {
            dsl.echo "release version: ${releaseVersion} successfully validated"
        } else {
            String errorMsg = "release version: ${releaseVersion} is not valid, current version is ${currentVersion}"
            dsl.echo "ERROR: ${errorMsg}"
            throw new InvalidReleaseNumberException(errorMsg)
        }
    }
}
