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
package pro.javatar.pipeline.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.InvalidReleaseNumberException
import pro.javatar.pipeline.release.ArtifactReleaseInfo
import pro.javatar.pipeline.service.infra.model.InfraRequest
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.addPrefixIfNotExists
import static pro.javatar.pipeline.util.StringUtils.isBlank
import static pro.javatar.pipeline.util.StringUtils.isNotBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class ReleaseInfo implements ArtifactReleaseInfo, Serializable {

    private static final NOT_RELEASED_ARTIFACT_SUFFIX = "-SNAPSHOT"

    String currentVersion
    String nextVersion
    String releaseVersion
    String developVersion
    String repoFolder
    String serviceName
    String flowPrefix
    // we use list for multi docker deployments support
    List<String> dockerImageNames = new ArrayList<>()
    Map<String, String> customDockerFileNames = new HashMap<>()
    String dockerImageVersion
    String buildNumber
    String buildReleaseVersion
    String uiDistributionFolder = "dist"
    boolean isUi = false
    boolean optimizeDockerContext = false
    String buildDockerFromFolder = ".jenkins"

    InfraRequest infraRequest = new InfraRequest()

    @Override
    String nextVersion() {
        if (isNotBlank(nextVersion)) {
            return nextVersion
        }
        return toNextVersion(releaseVersion())
    }

    @Override
    String currentVersion() {
        return currentVersion
    }

    @Override
    String releaseVersion() {
        if (isNotBlank(releaseVersion)) {
            return releaseVersion
        }
        return toReleaseVersion(currentVersion())
    }

    @Override
    String releaseVersionWithBuildSuffix() {
        if (isNotBlank(buildNumber)) {
            return String.format("%s.%s", releaseVersion(), buildNumber)
        }
        return releaseVersion()
    }

// helper methods

    String toCurrentVersion(String currentVersion) {
        validateCurrentVersion(currentVersion)
        return currentVersion
    }

    private String toReleaseVersion(String currentVersion) {
        Logger.debug("BuildService:getReleaseNumber: with currentVersion: " + currentVersion)
        return currentVersion.replace(NOT_RELEASED_ARTIFACT_SUFFIX, "")
    }

    private String toNextVersion(String releaseVersion) {
        if (releaseVersion.contains(NOT_RELEASED_ARTIFACT_SUFFIX)) {
            Logger.error("it seams this artifact: " + releaseVersion + " has not been released, no need increment version")
            throw new IllegalStateException("artifact should not contain ${NOT_RELEASED_ARTIFACT_SUFFIX}")
        }
        Logger.debug("current released version: " + releaseVersion)
        int idx = releaseVersion.lastIndexOf(".") + 1
        String result = releaseVersion.substring(0, idx)
        int smallerVersion = Integer.parseInt(releaseVersion.substring(idx, releaseVersion.length()))
        result += ++smallerVersion + NOT_RELEASED_ARTIFACT_SUFFIX
        return result
    }

    private void validateCurrentVersion(String currentVersion) throws InvalidReleaseNumberException {
        if (!currentVersion.contains("-SNAPSHOT")) {
            Logger.error("ReleaseInfo:validateCurrentVersion:" +
                    " it seems this artifact: " + currentVersion + " has been released already")
            throw new InvalidReleaseNumberException("currentVersion: " + currentVersion +
                    " does not contain $NOT_RELEASED_ARTIFACT_SUFFIX")
        }
        if (currentVersion.length() <= NOT_RELEASED_ARTIFACT_SUFFIX.length()) {
            String msg = "ReleaseInfo:validateCurrentVersion: version must be defined. " +
                    "currentVersion: ${currentVersion} is too small"
            Logger.error(msg)
            throw new InvalidReleaseNumberException(msg)
        }
    }

    private void validateBuildNumber(String buildNumber) throws InvalidReleaseNumberException {
        if (isBlank(buildNumber)) {
            String errorMsg = "ReleaseInfo:validateBuildNumber: validation failed buildNumber must be specified"
            Logger.error(errorMsg)
            throw new InvalidReleaseNumberException(errorMsg)
        }
    }

    // getters & setters

    ReleaseInfo setCurrentVersion(String currentVersion) {
        this.currentVersion = toCurrentVersion(currentVersion)
        return this
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

    ReleaseInfo setServiceName(String serviceName) {
        this.serviceName = serviceName
        return this
    }

    void setFlowPrefix(String flowPrefix) {
        this.flowPrefix = flowPrefix
    }

    String getDockerImageName() {
        String dockerImageName = getDockerImageName(0)
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
        if (dockerImageNames.isEmpty()) {
            return null
        }
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
        return addPrefixIfNotExists(flowPrefix, releaseVersion())
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
            Logger.warn("dockerImageNames is null but isMultiDockerBuild was triggered ")
            return false
        }
        if (dockerImageNames.size() > 1) {
            Logger.info("multiDockerBuild is not best practice, please consider to refactor")
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

    String getBuildNumber() {
        return buildNumber
    }

    ReleaseInfo setBuildNumber(String buildNumber) {
        validateBuildNumber(buildNumber)
        this.buildNumber = buildNumber
        return this
    }

    void setIsUi(boolean isUi) {
        this.isUi = isUi
    }

    void setOptimizeDockerContext(boolean optimizeDockerContext) {
        this.optimizeDockerContext = optimizeDockerContext
    }

    void setBuildDockerFromFolder(String buildDockerFromFolder) {
        this.buildDockerFromFolder = buildDockerFromFolder
    }

    boolean getIsUi() {
        return isUi
    }

    boolean getOptimizeDockerContext() {
        return optimizeDockerContext
    }

    String getBuildDockerFromFolder() {
        return buildDockerFromFolder
    }

    @NonCPS
    @Override
    String toString() {
        return "ReleaseInfo{" +
                "releaseVersion='" + releaseVersion + '\'' +
                ", developVersion='" + developVersion + '\'' +
                ", repoFolder='" + repoFolder + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", flowPrefix='" + flowPrefix + '\'' +
                ", uiDistributionFolder='" + uiDistributionFolder + '\'' +
                '}'
    }
}
