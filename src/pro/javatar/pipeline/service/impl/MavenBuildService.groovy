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
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.domain.Maven
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.NexusUploadAware
import pro.javatar.pipeline.util.Logger

import static java.lang.String.format
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isNotBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class MavenBuildService extends BuildService implements NexusUploadAware {

    Maven maven = new Maven()

    MavenBuildService() {
        Logger.info("MavenBuildService default constructor")
        maven.build_cmd = "mvn clean package"
    }

    MavenBuildService(Maven maven) {
        Logger.info("MavenBuildService constructor")
        this.maven = maven
    }

    @Override
    void setUp() {
        Logger.info("MavenBuildService: setUp started")
        dsl.env.M2_HOME="${dsl.tool maven.jenkins_tool_mvn}"
        dsl.env.JAVA_HOME="${dsl.tool maven.jenkins_tool_jdk}"
        dsl.env.PATH="${dsl.env.JAVA_HOME}/bin:${dsl.env.M2_HOME}/bin:${dsl.env.PATH}"
        dsl.sh 'java -version'
        dsl.sh 'mvn -version'
        Logger.info("MavenBuildService: setUp finished")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info("MavenBuildService buildAndUnitTests started")
        dsl.sh maven.build_cmd + " " + maven.params
        Logger.info("MavenBuildService buildAndUnitTests finished")
    }

    @Override
    String getCurrentVersion() {
        def pom = dsl.readMavenPom file: 'pom.xml'
        return pom.version
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        setupVersion(releaseVersion)
    }

    @Override
    def setupVersion(String version) {
        Logger.info("setupVersion: ${version} started")
        dsl.sh "mvn versions:set -DnewVersion=${version}"
        dsl.sh "mvn versions:commit"
        Logger.info("setupVersion: ${version} finished")
    }

    @Override
    def runIntegrationTests() {
        Logger.info("MavenBuildService:integrationTests with mavenParams: ${maven.params} started")
        dsl.sh "mvn -B verify ${maven.params} -DskipITs=false"
        Logger.info("MavenBuildService:integrationTests:finished")
    }

    def deployFile(String version, String file) {
        Logger.info("deployFile version: ${version}, file: ${file} started")
        deployFile(groupId, artifactId, version, packaging, file, repositoryId, repoUrl)
        Logger.info("deployFile version: ${version}, file: ${file} finished")
    }

    def deployFile(String groupId, String artifactId, String version, String packaging, String file,
                   String repositoryId, String repoUrl) {
        String deployFileCmd = "-DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} " +
                "-Dpackaging=${packaging} -Dfile=${file} -DrepositoryId=${repositoryId} -Durl=${repoUrl}"
        dsl.sh "mvn org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy-file ${deployFileCmd}"
    }

    def downloadArtifact(String repoUrl, String groupId, String artifactId, String version, String packaging) {
        String url = getMavenRepoUrl(repoUrl, groupId, artifactId, version, packaging)
        Logger.debug("downloadArtifact url: ${url}")
        dsl.sh "curl -O ${url}"
    }

    String getMavenRepoUrl(String version) {
        def pom = dsl.readMavenPom file: 'pom.xml'
        getMavenRepoUrl(maven.repo_url, pom.group, pom.artifact, version, pom.package)
    }

    String getMavenRepoUrl(String repoUrl, String groupId, String artifactId, String version, String packaging) {
        String artifactName = "${artifactName(artifactId, version, packaging)}"
        return "${repoUrl}/${groupIdToUrl(groupId)}/${artifactId}/${version}/${artifactName}"
    }

    String artifactName(String version) {
        def pom = dsl.readMavenPom file: 'pom.xml'
        return artifactName(pom.artifact, version, pom.package)
    }

    String artifactName(String artifactId, String version, String packaging) {
        return format("%s-%s.%s", artifactId, version, packaging);
    }

    String groupIdToUrl(String groupId) {
        return groupId.replace(".", "/")
    }

    def deployMavenArtifactsToNexus(mavenParams) {
        String command = format("mvn deploy -DdeployOnly -DskipTests=true %s %s",
                mavenParams, getAltDeploymentRepository())
        dsl.sh command
    }

    @Override
    void uploadMaven2Artifacts(ReleaseInfo releaseInfo) {
        deployMavenArtifactsToNexus();
    }

    def deployMavenArtifactsToNexus() {
        deployMavenArtifactsToNexus(maven.params)
    }

    // TODO verify in pom.xml if distributionManagement section exists
    String getAltDeploymentRepository() {
        if (isNotBlank(maven.repo_id) && isNotBlank(maven.repo_url)) {
            return "-DaltDeploymentRepository=${maven.repo_id}::${maven.layout}::${maven.repo_url}"
        }
        return ""
    }
}
