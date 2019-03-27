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

import pro.javatar.pipeline.service.infra.model.InfraRequest

import static pro.javatar.pipeline.util.Utils.addPrefixIfNotExists
import static pro.javatar.pipeline.util.Utils.isNotBlank
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

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

    List<String> dockerImageNames = new ArrayList<>()

    Map<String, String> customDockerFileNames = new HashMap<>()

    String dockerImageVersion

    String buildReleaseVersion

    String uiDistributionFolder = "dist"

    InfraRequest infraRequest = new InfraRequest()

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
        if (dockerImageNames.isEmpty()) {
            return null
        }
        String dockerImageName = dockerImageNames.get(0)
        if (isNotBlank(dockerImageName)) {
            return dockerImageName
        }
        return addPrefixIfNotExists(flowPrefix, serviceName)
    }

    void setDockerImageName(String dockerImageName) {
        this.dockerImageNames.set(0, dockerImageName)
    }

    void addDockerImageName(String dockerImageName) {
        this.dockerImageNames.add(dockerImageName)
    }

    String getDockerImageName(int id) {
        this.dockerImageNames.get(id)
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

    InfraRequest getInfraRequest() {
        return infraRequest
    }

    void setInfraRequest(InfraRequest infraRequest) {
        this.infraRequest = infraRequest
    }

    ReleaseInfo withInfraRequest(InfraRequest infraRequest) {
        this.infraRequest = infraRequest
        return this
    }

    List<String> getDockerImageNames() {
        return dockerImageNames
    }

    void setDockerImageNames(List<String> dockerImageNames) {
        this.dockerImageNames = dockerImageNames
    }

    boolean isMultiDockerBuild() {
        if (dockerImageNames == null) {
            dsl.echo "WARN: dockerImageNames is null but isMultiDockerBuild was triggered "
            return false
        }
        if (dockerImageNames.size() > 1) {
            dsl.echo "INFO: multiDockerBuild is not best practice, please consider to refactor"
            return true
        }
        return false
    }

    String getCustomDockerFileName(String dockerImageName) {
        return customDockerFileNames.get(dockerImageName)
    }

    void addCustomDockerFileName(String dockerImageName, String customDockerFileName) {
        customDockerFileNames.put(dockerImageName, customDockerFileName)
    }

    Map<String, String> getCustomDockerFileNames() {
        return customDockerFileNames
    }

    void setCustomDockerFileNames(Map<String, String> customDockerFileNames) {
        this.customDockerFileNames = customDockerFileNames
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
                ", dockerImageNames='" + getDockerImageNames() + '\'' +
                ", customDockerFileNames='" + getCustomDockerFileNames() + '\'' +
                ", dockerImageVersion='" + getDockerImageVersion() + '\'' +
                ", buildReleaseVersion='" + getBuildReleaseVersion() + '\'' +
                '}';
    }
}
