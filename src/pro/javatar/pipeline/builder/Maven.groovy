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

package pro.javatar.pipeline.builder

import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class Maven implements Serializable {

    String java
    String maven
    String mavenParams
    String groupId
    String artifactId
    String packaging
    String repositoryId
    String layout = "default"
    String repoUrl

    Maven() {
        Logger.debug("Maven:default constructor")
    }

    String getJava() {
        return java
    }

    void setJava(String java) {
        this.java = java
    }

    String getMaven() {
        return maven
    }

    void setMaven(String maven) {
        this.maven = maven
    }

    String getMavenParams() {
        return mavenParams
    }

    void setMavenParams(String mavenParams) {
        this.mavenParams = mavenParams
    }

    String getGroupId() {
        return groupId
    }

    void setGroupId(String groupId) {
        this.groupId = groupId
    }

    String getArtifactId() {
        return artifactId
    }

    void setArtifactId(String artifactId) {
        this.artifactId = artifactId
    }

    String getPackaging() {
        return packaging
    }

    void setPackaging(String packaging) {
        this.packaging = packaging
    }

    String getRepositoryId() {
        return repositoryId
    }

    void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
    }

    void setLayout(String layout) {
        this.layout = layout
    }

    String getRepoUrl() {
        return repoUrl
    }

    void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl
    }

    Maven withMaven(String maven) {
        this.maven = maven
        return this
    }

    Maven withMavenParams(String mavenParams) {
        this.mavenParams = mavenParams
        return this
    }

    Maven withJava(String java) {
        this.java = java
        return this
    }

    Maven withGroupId(String groupId) {
        this.groupId = groupId
        return this
    }

    Maven withArtifactId(String artifactId) {
        this.artifactId = artifactId
        return this
    }

    Maven withPackaging(String packaging) {
        this.packaging = packaging
        return this
    }

    Maven withRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId
        return this
    }

    Maven withRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl
        return this
    }

    String getLayout() {
        return layout
    }

    Maven withLayout(String layout) {
        this.layout = layout
        return this
    }

    @Override
    public String toString() {
        return "Maven{" +
                "java='" + java + '\'' +
                ", maven='" + maven + '\'' +
                ", mavenParams='" + mavenParams + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", packaging='" + packaging + '\'' +
                ", repositoryId='" + repositoryId + '\'' +
                ", layout='" + layout + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                '}';
    }
}
