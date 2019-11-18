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

import pro.javatar.pipeline.config.GradleConfig
import pro.javatar.pipeline.exception.MalformedReleaseVersionException
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.NexusUploadAware
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

class GradleBuildService extends BuildService implements NexusUploadAware {

    private JenkinsDslService dslService;

    private GradleConfig config;

    GradleBuildService(JenkinsDslService dslService, GradleConfig config) {
        Logger.debug(String.format("GradleBuildService: gradleConfig: " + config.toString()));
        this.dslService = dslService;
        this.config = config;
    }

    @Override
    void setUp() {
        Logger.debug("GradleBuildService setUp started")
        dslService.addToPath(config.gradleTool(), 'GRADLE_HOME')
        dslService.addToPath(config.javaTool(), 'JAVA_HOME')
        dslService.executeShell('gradle -v');
        dslService.executeShell('java -version')
        Logger.debug("GradleBuildService setUp finished")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info("GradleBuildService buildAndUnitTests started")
        dslService.executeShell("gradle clean build " + config.additionalBuildParameters());
        Logger.info("GradleBuildService buildAndUnitTests finished")
    }

    @Override
    String getCurrentVersion() {
        Logger.debug("GradleBuildService getCurrentVersion started")
        Logger.info("expected that in properties project version has variable with name: version")
        String command = "gradle  properties --no-daemon --console=plain -q | grep ^version: | awk '{printf \$2}'";
        String version = dslService.getShellExecutionResponse(command);
        Logger.info("GradleBuildService:getCurrentVersion:result: " + version)
        Logger.debug("GradleBuildService getCurrentVersion finished")
        return version.trim()
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        Logger.debug(String.format("GradleBuildService setupReleaseVersion: %s started", releaseVersion));
        if (releaseVersion.contains("SNAPSHOT")) {
            String msg = "Release version must not contain SNAPSHOT, but was: " + releaseVersion;
            Logger.error(msg)
            throw new MalformedReleaseVersionException(msg)
        }
        setupVersion(releaseVersion)
        Logger.debug(String.format("GradleBuildService setupReleaseVersion: %s finished", releaseVersion));
    }

    @Override
    def setupVersion(String version) {
        Logger.info(String.format("GradleBuildService setupVersion: %s started", version));
        String currentVersion = getCurrentVersion()
        Logger.trace(String.format("GradleBuildService %s before version setup", config.versionFile()));
        dslService.executeShell("cat " + config.versionFile());
        FileUtils.replace(currentVersion, version, config.versionFile())
        Logger.trace(String.format("GradleBuildService %s after version setup", config.versionFile()));
        dslService.executeShell("cat " + config.versionFile());
        Logger.info(String.format("GradleBuildService setupVersion: %s finished", version));
    }

    @Override
    def publishArtifacts(ReleaseInfo releaseInfo) {
        Logger.info("GradleBuildService:publishArtifacts:started " + releaseInfo);
        dslService.executeSecureShell("gradle publish " + config.publishParams());
        Logger.info("GradleBuildService:publishArtifacts:finished")
    }

    @Override
    void uploadMaven2Artifacts(ReleaseInfo releaseInfo) {
        publishArtifacts(releaseInfo);
    }

}
