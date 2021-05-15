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
package pro.javatar.pipeline.service

import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.release.CurrentVersionAware
import pro.javatar.pipeline.release.SetupVersionAware
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class BuildService implements CurrentVersionAware, SetupVersionAware, Serializable {

    protected JenkinsDslService dslService
    protected int unitTestsTimeout = 25
    boolean useBuildNumberForVersion = true
    boolean skipUnitTests = true
    String distributionFolder

    abstract void buildAndUnitTests(ReleaseInfo releaseInfo)

    abstract void setUp()

    abstract def setupReleaseVersion(String releaseVersion)

    def runIntegrationTests() {
        Logger.info("BuildService:runIntegrationTests")
    }

    def populateReleaseInfo(ReleaseInfo releaseInfo) {
        Logger.debug("default populateReleaseInfo, nothing to change")
    }

    def publishArtifacts(ReleaseInfo releaseInfo) {
        Logger.info("default publish implementation will do nothing")
    }

    // helper methods

    BuildService withDistributionFolder(String distributionFolder) {
        this.distributionFolder = distributionFolder
        return this
    }

    BuildService withUnitTestsTimeout(int unitTestsTimeout) {
        this.unitTestsTimeout = unitTestsTimeout
        return this
    }

    String getServiceName() {
        return "buildService"
    }

    void withDslService(JenkinsDslService dslService) {
        this.dslService = dslService
    }
}
