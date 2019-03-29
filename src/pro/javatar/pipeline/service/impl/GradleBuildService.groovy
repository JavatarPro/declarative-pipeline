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

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class GradleBuildService extends BuildService {

    String gradle

    String java

    String params

    GradleBuildService(String gradle, String java) {
        this.gradle = gradle
        this.java = java
    }

    @Override
    void setUp() {
        dsl.echo "GradleBuildService setUp started"
        dsl.env.GRADLE_HOME="${dsl.tool gradle}"
        dsl.env.JAVA_HOME="${dsl.tool java}"
        dsl.env.PATH="${dsl.env.JAVA_HOME}/bin:${dsl.env.GRADLE_HOME}/bin:${dsl.env.PATH}"
        dsl.sh 'gradle -v'
        dsl.sh 'java -version'
        dsl.echo "GradleBuildService setUp finished"
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        dsl.echo "GradleBuildService buildAndUnitTests started"
        dsl.sh "gradle clean build ${params}"
        dsl.echo "GradleBuildService buildAndUnitTests finished"
    }

    @Override
    String getCurrentVersion() {
        dsl.echo "GradleBuildService getCurrentVersion started"
        dsl.echo "WARN: expected that in properties project version has variable with name: version"
        String version = dsl.sh returnStdout: true, script: "gradle  properties --no-daemon --console=plain -q | grep ^version: | awk '{printf \$2}'"
        return version.trim()
        dsl.echo "GradleBuildService getCurrentVersion finished"
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        dsl.echo "GradleBuildService setupReleaseVersion: ${releaseVersion} started"
        if (releaseVersion.contains("SNAPSHOT")) {
            String msg = "Release version must not contain SNAPSHOT, but was: ${releaseVersion}"
            dsl.echo "${msg}"
            throw new MalformedReleaseVersionException(msg)
        }
        setupVersion(releaseVersion)
        dsl.echo "GradleBuildService setupReleaseVersion: ${releaseVersion} finished"
    }

    @Override
    def setupVersion(String version) {
        dsl.echo "GradleBuildService setupVersion: ${releaseVersion} started"
        String currentVersion = getCurrentVersion()
        FileUtils.replace(currentVersion, releaseVersion, applicationFile)
        dsl.echo "GradleBuildService setupVersion: ${releaseVersion} finished"
    }

    GradleBuildService withParams(String params) {
        this.params = params
    }

}
