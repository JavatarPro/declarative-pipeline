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

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isBlank
import static pro.javatar.pipeline.util.StringUtils.isNotBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class MavenBuildService extends BuildService {

    protected String java
    protected String maven
    protected String mavenParams = ""
    protected String groupId
    protected String artifactId
    protected String packaging
    protected String repositoryId
    protected String layout
    protected String repoUrl

    MavenBuildService() {
        Logger.info("MavenBuildService default constructor")
    }

    @Override
    void setUp() {
        Logger.info("MavenBuildService: setUp started")
        dsl.env.M2_HOME="${dsl.tool maven}"
        dsl.env.JAVA_HOME="${dsl.tool java}"
        dsl.env.PATH="${dsl.env.JAVA_HOME}/bin:${dsl.env.M2_HOME}/bin:${dsl.env.PATH}"
        dsl.sh 'java -version'
        dsl.sh 'mvn -version'
        Logger.info("MavenBuildService: setUp finished")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info("MavenBuildService buildAndUnitTests started")
        dsl.sh "mvn clean package " + mavenParams
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
        Logger.info("MavenBuildService:integrationTests with mavenParams: ${mavenParams} started")
        dsl.sh "mvn -B verify ${mavenParams} -DskipITs=false"
        //dsl.sh "mvn failsafe:integration-test -DskipITs=false ${mavenParams}"
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
        getMavenRepoUrl(repoUrl, groupId, artifactId, version, packaging)
    }

    String getMavenRepoUrl(String repoUrl, String groupId, String artifactId, String version, String packaging) {
        String artifactName = "${artifactName(artifactId, version, packaging)}"
        return "${repoUrl}/${groupIdToUrl(groupId)}/${artifactId}/${version}/${artifactName}"
    }

    String artifactName(String version) {
        return artifactName(artifactId, version, packaging)
    }

    String artifactName(String artifactId, String version, String packaging) {
        return "${artifactId}-${version}.${packaging}"
    }

    String groupIdToUrl(String groupId) {
        return groupId.replace(".", "/")
    }

    def deployMavenArtifactsToNexus(mavenParams) {
        dsl.sh "mvn deploy -DdeployOnly -DskipTests=true ${mavenParams} ${getAltDeploymentRepository()}"
    }

    def deployMavenArtifactsToNexus() {
        deployMavenArtifactsToNexus(mavenParams)
    }

    // TODO verify in pom.xml if distributionManagement section exists
    String getAltDeploymentRepository() {
        if (isNotBlank(repositoryId) && isNotBlank(repoUrl)) {
            return "-DaltDeploymentRepository=${repositoryId}::${layout}::${repoUrl}"
        }
        return ""
    }

    void setJava(String java) {
        this.java = java
    }

    void setMaven(String maven) {
        this.maven = maven
    }

    protected void setMavenParams(String mavenParams) {
        if(isBlank(mavenParams)) {
            return
        }
        this.mavenParams = mavenParams
    }

    String getMavenParams() {
        if (isBlank(mavenParams)) {
            return ""
        }
        return mavenParams
    }

    void setGroupId(String groupId) {
        this.groupId = groupId
    }

    void setArtifactId(String artifactId) {
        this.artifactId = artifactId
    }

    void setPackaging(String packaging) {
        this.packaging = packaging
    }

    void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
    }

    void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl
    }

    void setLayout(String layout) {
        this.layout = layout
    }

    @NonCPS
    @Override
    public String toString() {
        return "MavenBuildService{" +
                "java='" + java + '\'' +
                ", maven='" + maven + '\'' +
                ", mavenParams='" + mavenParams + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", packaging='" + packaging + '\'' +
                ", repositoryId='" + repositoryId + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                '}';
    }
}
