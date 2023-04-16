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
package pro.javatar.pipeline.release

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

import static java.lang.String.format

/**
 * Release service for all types of services
 * @author Borys Zora
 * @since 2023-04-16
 */
class ReleaseServiceV3 implements ReleaseService {

    private BuildService buildService
    private RevisionControlService revisionControlService
    private DockerService dockerService

    ReleaseServiceV3(BuildService buildService,
                     RevisionControlService revisionControlService,
                     DockerService dockerService) {
        this.buildService = buildService
        this.revisionControlService = revisionControlService
        this.dockerService = dockerService
    }

    @Override
    def release(ReleaseInfo releaseInfo) {
        Logger.info("ReleaseServiceV3 start release: " + releaseInfo.toString())
        validateReleaseVersion(releaseInfo.releaseVersion())
        releaseRevisionControl(releaseInfo)
        // TODO comment out promotion of docker, it should be after QA sign off, but lets keep it before all steps in
        dockerService.dockerPushImageToProdRegistry(releaseInfo.serviceName, releaseInfo.releaseVersion())
        Logger.info("ReleaseServiceV3 end release")
    }

    def releaseRevisionControl(ReleaseInfo releaseInfo) {
        Logger.info("ReleaseServiceV3: releaseRevisionControl() started")
        revisionControlService.release(releaseInfo.releaseVersion())
        revisionControlService.switchToDevelopBranch()
        buildService.setupVersion(releaseInfo.nextVersion())
        revisionControlService.commitChanges("Update version to " + releaseInfo.nextVersion())
        revisionControlService.pushRelease()
        Logger.info("ReleaseServiceV3: releaseRevisionControl() finished")
    }

    def validateReleaseVersion(String releaseVersion) {
        Logger.debug("ReleaseServiceV3 validateReleaseVersion: " + releaseVersion)
        String currentVersion = buildService.getCurrentVersion()
        if (currentVersion.equalsIgnoreCase(releaseVersion)) {
            Logger.debug(format("ReleaseServiceV3: release version: %s successfully validated", releaseVersion));
        } else {
            String errorMsg = format("ReleaseServiceV3: release version: %s is not valid, current version is %s",
                    releaseVersion, currentVersion);
            Logger.error(errorMsg);
            throw new InvalidReleaseNumberException(errorMsg)
        }
    }


    @NonCPS @Override
    String toString() {
        return "ReleaseServiceV3{" +
                "buildService=" + buildService +
                ", revisionControlService=" + revisionControlService +
                ", dockerService=" + dockerService +
                '}'
    }
}
