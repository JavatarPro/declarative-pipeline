/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/pipeline-utils/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.javatar.pipeline.stage;

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class BuildAndUnitTestStage extends Stage {

    BuildService buildService
    RevisionControlService revisionControl

    BuildAndUnitTestStage(BuildService buildService,
                          RevisionControlService revisionControlService) {
        this.buildService = buildService
        this.revisionControl = revisionControlService
    }

    @Override
    void execute() throws PipelineException {
        dsl.echo "BuildAndUnitTestStage execute started: ${toString()}"
        dsl.timeout(time: buildService.unitTestsTimeout, unit: 'MINUTES') {
            revisionControl.cleanUp()
            dsl.dir(revisionControl.folder) {
                revisionControl.setUp()
                revisionControl.checkoutProdBranch()
                revisionControl.setUpVcsFlowPreparations()
                revisionControl.switchToDevelopBranch()
                buildService.setUp()
                def releaseVersion = buildService.getReleaseVersion()
                populateReleaseInfo(releaseVersion)
                dsl.currentBuild.description =
                        "try to build ${releaseInfo.serviceName}:${releaseInfo.getBuildReleaseVersion()}"
                revisionControl.createReleaseBranchLocally(releaseInfo.getPrefixedReleaseVersion())
                buildService.setupReleaseVersion(releaseInfo.getBuildReleaseVersion())
                revisionControl.commitChanges("Starting release ${releaseInfo.getPrefixedReleaseVersion()}")
                buildService.buildAndUnitTests(releaseInfo)
            }
        }
        dsl.currentBuild.description =
                "build of ${releaseInfo.serviceName}:${releaseInfo.getBuildReleaseVersion()} completed"
        dsl.echo "BuildAndUnitTestStage execute finished: ${toString()}"
    }

    @Override
    String getName() {
        return "build and unit test";
    }

    def populateReleaseInfo(String releaseVersion) {
        dsl.echo "BuildAndUnitTestStage populateReleaseInfo started with releaseVersion: ${releaseVersion}"
        releaseInfo.setRepoFolder(revisionControl.folder)
        releaseInfo.setReleaseVersion(releaseVersion)
        releaseInfo.setDevelopVersion(buildService.getDevelopVersion(releaseVersion))
        releaseInfo.setFlowPrefix(revisionControl.getFlowPrefix())
        buildService.populateReleaseInfo(releaseInfo)
        dsl.echo "BuildAndUnitTestStage populateReleaseInfo finished: ${releaseInfo.toString()}"
    }

    @Override
    public String toString() {
        return "BuildAndUnitTestStage{" +
                "buildService=" + buildService +
                ", revisionControl=" + revisionControl +
                ", skipStage=" + skipStage +
                ", exitFromPipeline=" + exitFromPipeline +
                ", releaseInfo=" + releaseInfo +
                '}';
    }
}
