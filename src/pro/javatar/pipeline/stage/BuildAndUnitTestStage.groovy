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
package pro.javatar.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.ContextHolder.get
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * TODO remove dsl
 * @author Borys Zora
 * @since 2018-03-09
 */
class BuildAndUnitTestStage extends Stage {

    BuildAndUnitTestStage() {
        Logger.debug("BuildAndUnitTestStage#constructor")
    }

    @Override
    void execute() throws PipelineException {
        Logger.info("BuildAndUnitTestStage execute started")
        RevisionControlService revisionControl = get(RevisionControlService.class)
        BuildService buildService = get(BuildService.class)
        // TODO make decorator to wrap dsl.timeout to simplify for unit tests
//        JenkinsDsl dsl = get(JenkinsDsl.class)
//        options {
//            timeout(time: 1, unit: 'HOURS')
//            checkoutToSubdirectory()
//        }
//        if (buildService != null) return // uncomment for debug purposes
        dsl.timeout(time: buildService.unitTestsTimeout, unit: 'MINUTES') {
            revisionControl.cleanUp()
            dsl.dir(revisionControl.folder) {
                revisionControl.setUp()
                revisionControl.checkoutProdBranch()
                revisionControl.setUpVcsFlowPreparations()
                revisionControl.switchToDevelopBranch()
                buildService.setUp()
                populateReleaseInfo()
                dsl.currentBuild.description =
                        "try to build " + releaseInfo().getServiceName() + ":" + releaseInfo().getBuildReleaseVersion();
                revisionControl.createReleaseBranchLocally(releaseInfo().getPrefixedReleaseVersion())
                buildService.setupReleaseVersion(releaseInfo().getBuildReleaseVersion())
                revisionControl.commitChanges("Starting release " + releaseInfo().getPrefixedReleaseVersion())
                buildService.buildAndUnitTests(releaseInfo())
            }
        }
        dsl.currentBuild.description = "build of " + releaseInfo().getServiceName() + ":" +
                releaseInfo().getBuildReleaseVersion() + " completed"
        Logger.info("BuildAndUnitTestStage execute finished")
    }

    @Override
    String name() {
        return "build and unit test";
    }

    def populateReleaseInfo() {
        Logger.info("BuildAndUnitTestStage populateReleaseInfo started with")
        RevisionControlService revisionControl = get(RevisionControlService.class)
        BuildService buildService = get(BuildService.class)
        releaseInfo().setRepoFolder(revisionControl.folder)
        def currentVersion = buildService.getCurrentVersion()
        releaseInfo().setCurrentVersion(currentVersion)
        releaseInfo().setFlowPrefix(revisionControl.getFlowPrefix())
        buildService.populateReleaseInfo(releaseInfo())
        Logger.info("BuildAndUnitTestStage populateReleaseInfo finished")
    }

    @NonCPS
    @Override
    String toString() {
        RevisionControlService revisionControl = get(RevisionControlService.class)
        BuildService buildService = get(BuildService.class)
        return "BuildAndUnitTestStage{" +
                "skipStage=" + skipStage +
                ", exitFromPipeline=" + exitFromPipeline +
                ", revisionControl=" + revisionControl.toString() +
                ", buildService=" + buildService.toString() +
                '}'
    }
}
