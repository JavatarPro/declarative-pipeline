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
import pro.javatar.pipeline.model.BuildServiceType
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.release.CurrentVersionAware
import pro.javatar.pipeline.release.ReleaseVersionAware
import pro.javatar.pipeline.release.SetupVersionAware
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class BuildService implements CurrentVersionAware, SetupVersionAware, ReleaseVersionAware, Serializable {

    protected int unitTestsTimeout = 25
    boolean useBuildNumberForVersion = true
    boolean skipUnitTests = true
    String distributionFolder

    abstract void buildAndUnitTests(ReleaseInfo releaseInfo)

    abstract void setUp()

    String getReleaseVersion() throws InvalidReleaseNumberException {
        String currentVersion = getCurrentVersion()
        if (useBuildNumberForVersion) {
            return getReleaseNumber(currentVersion, dsl.currentBuild.number)
        }
        return getReleaseNumber(currentVersion)
    }

    String getDevelopVersion(String version) {
        Logger.info("getDevelopVersion for version: " + version)
        if (version.contains("-SNAPSHOT")) {
            Logger.error("it seams this artifact: " + version + " has not been released, no need increment version")
            throw new IllegalStateException("artifact should not contain SNAPSHOT");
        }
        Logger.debug("current version: " + version)
        int idx = version.lastIndexOf(".") + 1;
        String result = version.substring(0, idx);
        int smallerVersion = Integer.parseInt(version.substring(idx, version.length()));
        result += ++smallerVersion + "-SNAPSHOT";
        return result;
    }

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

    String getReleaseNumber(String currentVersion) {
        Logger.debug("BuildService:getReleaseNumber: with currentVersion: " + currentVersion)
        validateCurrentVersion(currentVersion)
        String releaseVersion = currentVersion.replace("-SNAPSHOT", "")
        if (releaseVersion.isEmpty()) {
            Logger.error("BuildService:getReleaseNumber: releaseVersion must be defined")
            throw new InvalidReleaseNumberException("releaseVersion must be defined")
        }
        return releaseVersion
    }

    @Override
    String getReleaseVersion(ReleaseInfo releaseInfo) {
        return getReleaseNumber(releaseInfo.getDevelopVersion(), releaseInfo.getBuildNumber())
    }

    String getReleaseNumber(String currentVersion, def buildNumber) throws InvalidReleaseNumberException {
        Logger.debug("BuildService:getReleaseNumber with currentVersion: " + currentVersion +
                "using buildNumber: " + buildNumber)
        validateBuildNumber(buildNumber)
        return String.format("%s.%s", getReleaseNumber(), buildNumber);
    }

    protected void validateCurrentVersion(String currentVersion) throws InvalidReleaseNumberException {
        if (!currentVersion.contains("-SNAPSHOT")) {
            Logger.error("BuildService:validateCurrentVersion:" +
                    " it seems this artifact: " + currentVersion + " has been released already")
            throw new InvalidReleaseNumberException("currentVersion: " + currentVersion + " does not contain -SNAPSHOT")
        }
    }

    protected void validateBuildNumber(def buildNumber) throws InvalidReleaseNumberException {
        if (buildNumber == null || buildNumber.isEmpty()) {
            Logger.error("BuildService:validateBuildNumber: buildNumber must be specified")
            throw new InvalidReleaseNumberException("buildNumber is not specified")
        }
    }

    protected BuildService withDistributionFolder(String distributionFolder) {
        this.distributionFolder = distributionFolder
        return this
    }

    protected BuildService withUnitTestsTimeout(int unitTestsTimeout) {
        this.unitTestsTimeout = unitTestsTimeout
        return this
    }

    protected String getServiceName() {
        return "buildService"
    }

}
