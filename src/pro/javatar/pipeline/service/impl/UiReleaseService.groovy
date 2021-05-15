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

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class UiReleaseService implements ReleaseService {

    BuildService buildService
    RevisionControlService revisionControlService

    UiReleaseService(BuildService buildService, RevisionControlService revisionControlService) {
        this.buildService = buildService
        this.revisionControlService = revisionControlService
    }

    @Override
    def release(ReleaseInfo releaseInfo) {
        Logger.info("UiReleaseService releaseRevisionControl() started")
        // String releaseVersion = buildService.getCurrentVersion()
        // String developVersion = buildService.getDevelopVersion(releaseVersion)
        revisionControlService.release(releaseInfo.releaseVersion())
        revisionControlService.switchToDevelopBranch()
        buildService.setupVersion(releaseInfo.nextVersion())
        revisionControlService.commitChanges("Update develop version to :${releaseInfo.nextVersion()}")
        revisionControlService.pushRelease()
        Logger.info("UiReleaseService releaseRevisionControl() finished")
    }

}
