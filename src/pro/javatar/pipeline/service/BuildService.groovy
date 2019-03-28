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

package pro.javatar.pipeline.service

import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.model.ReleaseInfo

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class BuildService implements Serializable {

    protected int unitTestsTimeout = 25

    boolean useBuildNumberForVersion = true

    boolean skipUnitTests = true

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
        dsl.echo "getDevelopVersion for version: ${version}"
        if (version.contains("-SNAPSHOT")) {
            dsl.echo "it seams this artifact: ${version} has not been released, no need increment version"
            throw new IllegalStateException("artifact should not contain SNAPSHOT");
        }
        dsl.echo "current version: ${version}"
        int idx = version.lastIndexOf(".") + 1;
        String result = version.substring(0, idx);
        int smallerVersion = Integer.parseInt(version.substring(idx, version.length()));
        result += ++smallerVersion + "-SNAPSHOT";
        return result;
    }

    abstract String getCurrentVersion()

    abstract def setupReleaseVersion(String releaseVersion)

    abstract def setupVersion(String version)

    def populateReleaseInfo(ReleaseInfo releaseInfo) {
        dsl.echo "default populateReleaseInfo, nothing to change"
    }

    // helper methods

    String getReleaseNumber(String currentVersion) {
        dsl.echo "getReleaseNumber with currentVersion: ${currentVersion}"
        validateCurrentVersion(currentVersion)
        String releaseVersion = currentVersion.replace("-SNAPSHOT", "")
        if (releaseVersion.isEmpty()) {
            dsl.echo "Error: releaseVersion must be defined"
            throw new InvalidReleaseNumberException("releaseVersion must be defined")
        }
        return releaseVersion
    }

    String getReleaseNumber(String currentVersion, def buildNumber) throws InvalidReleaseNumberException {
        dsl.echo "getReleaseNumber with currentVersion: ${currentVersion} using buildNumber: ${buildNumber}"
        validateBuildNumber(buildNumber)
        return "${getReleaseNumber()}.${buildNumber}"
    }

    protected void validateCurrentVersion(String currentVersion) throws InvalidReleaseNumberException {
        if (!currentVersion.contains("-SNAPSHOT")) {
            dsl.echo "Error: it seems this artifact: ${currentVersion} has been released already"
            throw new InvalidReleaseNumberException("currentVersion: ${currentVersion} does not contain -SNAPSHOT")
        }
    }

    protected void validateBuildNumber(def buildNumber) throws InvalidReleaseNumberException {
        if (buildNumber == null || buildNumber.isEmpty()) {
            dsl.echo "Error: buildNumber must be specified"
            throw new InvalidReleaseNumberException("buildNumber is not specified")
        }
    }

    void setDistributionFolder(String distributionFolder) {
        this.distributionFolder = distributionFolder
    }

    protected BuildService withUnitTestsTimeout(int unitTestsTimeout) {
        this.unitTestsTimeout = unitTestsTimeout
        return this
    }

    protected String getServiceName() {
        return "buildService"
    }

}
