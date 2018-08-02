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

package pro.javatar.pipeline.model

import static pro.javatar.pipeline.util.Utils.addPrefixIfNotExists
import static pro.javatar.pipeline.util.Utils.isNotBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class ReleaseInfo implements Serializable {

    String releaseVersion

    String developVersion

    String repoFolder

    String serviceName

    String flowPrefix

    String dockerImageName

    String dockerImageVersion

    String buildReleaseVersion

    String uiDistributionFolder = "dist"

    String getReleaseVersion() {
        return releaseVersion
    }

    void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion
    }

    String getDevelopVersion() {
        return developVersion
    }

    void setDevelopVersion(String developVersion) {
        this.developVersion = developVersion
    }

    String getRepoFolder() {
        return repoFolder
    }

    void setRepoFolder(String repoFolder) {
        this.repoFolder = repoFolder
    }

    String getServiceName() {
        return serviceName
    }

    void setServiceName(String serviceName) {
        this.serviceName = serviceName
    }

    String getFlowPrefix() {
        return flowPrefix
    }

    void setFlowPrefix(String flowPrefix) {
        this.flowPrefix = flowPrefix
    }

    String getDockerImageName() {
        if (isNotBlank(dockerImageName)) {
            return dockerImageName
        }
        return addPrefixIfNotExists(flowPrefix, serviceName)
    }

    void setDockerImageName(String dockerImageName) {
        this.dockerImageName = dockerImageName
    }

    String getDockerImageVersion() {
        if (isNotBlank(dockerImageVersion)) {
            return dockerImageVersion
        }
        return getPrefixedReleaseVersion()
    }

    void setDockerImageVersion(String dockerImageVersion) {
        this.dockerImageVersion = dockerImageVersion
    }

    String getBuildReleaseVersion() {
        if (isNotBlank(buildReleaseVersion)) {
            return buildReleaseVersion
        }
        return getPrefixedReleaseVersion()
    }

    String getPrefixedReleaseVersion() {
        return addPrefixIfNotExists(flowPrefix, releaseVersion)
    }

    String getPrefixedDevelopVersion() {
        return addPrefixIfNotExists(flowPrefix, developVersion)
    }

    void setBuildReleaseVersion(String buildReleaseVersion) {
        this.buildReleaseVersion = buildReleaseVersion
    }

    String getUiDistributionFolder() {
        return uiDistributionFolder
    }

    void setUiDistributionFolder(String uiDistributionFolder) {
        this.uiDistributionFolder = uiDistributionFolder
    }

    @Override
    public String toString() {
        return "ReleaseInfo{" +
                "releaseVersion='" + releaseVersion + '\'' +
                ", developVersion='" + developVersion + '\'' +
                ", repoFolder='" + repoFolder + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", flowPrefix='" + flowPrefix + '\'' +
                ", uiDistributionFolder='" + uiDistributionFolder + '\'' +
                ", dockerImageName='" + getDockerImageName() + '\'' +
                ", dockerImageVersion='" + getDockerImageVersion() + '\'' +
                ", buildReleaseVersion='" + getBuildReleaseVersion() + '\'' +
                '}';
    }
}
