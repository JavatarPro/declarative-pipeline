/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/declarative-pipeline/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.NexusUploadAware
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

import static java.lang.String.format
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class BackEndReleaseService implements ReleaseService {

    private BuildService buildService
    private NexusUploadAware uploadService;
    private RevisionControlService revisionControlService
    private DockerService dockerService


    BackEndReleaseService(BuildService buildService,
                          NexusUploadAware uploadAware,
                          RevisionControlService revisionControlService,
                          DockerService dockerService) {
        this.buildService = buildService;
        this.uploadService = uploadAware;
        this.revisionControlService = revisionControlService;
        this.dockerService = dockerService;
    }

    @Override
    def release(ReleaseInfo releaseInfo) {
        Logger.info("BackEndReleaseService start release: " + releaseInfo.toString())
        validateReleaseVersion(releaseInfo.releaseVersion)
        uploadService.uploadMaven2Artifacts();
        // TODO comment out promotion of docker, it should be after QA sign off
        dsl.parallel 'release revision control': {
            releaseRevisionControl(releaseInfo)
        }, 'release docker artifacts': {
            dockerService.dockerPushImageToProdRegistry(releaseInfo.serviceName, releaseInfo.releaseVersion)
        }
        Logger.info("BackEndReleaseService end release")
    }

    def releaseRevisionControl(ReleaseInfo releaseInfo) {
        Logger.info("BackEndReleaseService: releaseRevisionControl() started")
        revisionControlService.release(releaseInfo.releaseVersion)
        revisionControlService.switchToDevelopBranch()
        buildService.setupVersion(releaseInfo.developVersion)
        revisionControlService.commitChanges("Update version to " + releaseInfo.developVersion)
        revisionControlService.pushRelease()
        Logger.info("BackEndReleaseService: releaseRevisionControl() finished")
    }

    def validateReleaseVersion(String releaseVersion) {
        Logger.debug("BackEndReleaseService validateReleaseVersion: " + releaseVersion)
        String currentVersion = buildService.getCurrentVersion()
        if (currentVersion.equalsIgnoreCase(releaseVersion)) {
            Logger.debug(format("release version: %s successfully validated", releaseVersion));
        } else {
            String errorMsg = format("release version: %s is not valid, current version is %s",
                    releaseVersion, currentVersion);
            Logger.error(errorMsg);
            throw new InvalidReleaseNumberException(errorMsg)
        }
    }

}
