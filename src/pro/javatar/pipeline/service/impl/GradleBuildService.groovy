/**
 * Copyright Javatar LLC 2019 ©
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

import pro.javatar.pipeline.exception.MalformedReleaseVersionException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isBlank

class GradleBuildService extends BuildService {

    String gradle

    String java

    String params

    String versionFile = "gradle.properties"

    GradleBuildService(String gradleTool, String javaTool) {
        Logger.debug("GradleBuildService: gradleTool: ${gradleTool} javaTool: ${javaTool}")
        this.gradle = gradleTool
        this.java = javaTool
    }

    @Override
    void setUp() {
        Logger.debug("GradleBuildService setUp started")
        dsl.env.GRADLE_HOME="${dsl.tool gradle}"
        dsl.env.JAVA_HOME="${dsl.tool java}"
        dsl.env.PATH="${dsl.env.JAVA_HOME}/bin:${dsl.env.GRADLE_HOME}/bin:${dsl.env.PATH}"
        dsl.sh 'gradle -v'
        dsl.sh 'java -version'
        Logger.debug("GradleBuildService setUp finished")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        dsl.echo "GradleBuildService buildAndUnitTests started"
        dsl.sh "gradle clean build ${getParams()}"
        dsl.echo "GradleBuildService buildAndUnitTests finished"
    }

    @Override
    String getCurrentVersion() {
        Logger.debug("GradleBuildService getCurrentVersion started")
        Logger.info("expected that in properties project version has variable with name: version")
        String version = dsl.sh returnStdout: true, script: "gradle  properties --no-daemon --console=plain -q | grep ^version: | awk '{printf \$2}'"
        Logger.info("GradleBuildService:getCurrentVersion:result: ${version}")
        Logger.debug("GradleBuildService getCurrentVersion finished")
        return version.trim()
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        Logger.debug("GradleBuildService setupReleaseVersion: ${releaseVersion} started")
        if (releaseVersion.contains("SNAPSHOT")) {
            String msg = "Release version must not contain SNAPSHOT, but was: ${releaseVersion}"
            Logger.error(msg)
            throw new MalformedReleaseVersionException(msg)
        }
        setupVersion(releaseVersion)
        Logger.debug("GradleBuildService setupReleaseVersion: ${releaseVersion} finished")
    }

    @Override
    def setupVersion(String version) {
        Logger.info("GradleBuildService setupVersion: ${version} started")
        String currentVersion = getCurrentVersion()
        Logger.trace("GradleBuildService ${versionFile} before version setup")
        dsl.sh "cat ${versionFile}"
        FileUtils.replace(currentVersion, version, versionFile)
        Logger.trace("GradleBuildService ${versionFile} after version setup")
        dsl.sh "cat ${versionFile}"
        Logger.info("GradleBuildService setupVersion: ${version} finished")
    }

    @Override
    def publishArtifacts(ReleaseInfo releaseInfo) {
        Logger.info("GradleBuildService:publishArtifacts:started ${releaseInfo}")
        dsl.sh "gradle publish"
        Logger.info("GradleBuildService:publishArtifacts:finished")
    }

    GradleBuildService withParams(String params) {
        this.params = params
        return this
    }

    String getParams() {
        if (isBlank(params)) {
            return ""
        }
        return params
    }

    @Override
    public String toString() {
        return "GradleBuildService{" +
                "gradle='" + gradle + '\'' +
                ", java='" + java + '\'' +
                ", params='" + params + '\'' +
                ", versionFile='" + versionFile + '\'' +
                '}';
    }
}
