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

package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.orchestration.DockerService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerMavenBuildService extends MavenBuildService {

    DockerService dockerService

    DockerMavenBuildService(Maven maven, DockerService dockerService) {
        dsl.echo "DockerMavenBuildService constructor with maven & dockerService"
        // does not work
        // dsl.echo "DockerMavenBuildService dockerService: ${dockerService.toString()}"
        // dsl.echo "DockerMavenBuildService dockerService: ${maven.toString()}"
        // dsl.echo "DockerMavenBuildService: maven: ${maven.toString()}, dockerService: ${dockerService.toString()}"
        this.dockerService = dockerService
        //populateMaven(maven)
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        // buildAndUnitTests(releaseInfo)
        dsl.echo "buildAndUnitTests started"
        dsl.sh "mvn clean package"
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        dsl.echo "buildAndUnitTests finished"
    }

    void populateMaven(Maven maven) {
        dsl.echo "setJava(maven.getJava())"
//        dsl.echo "populateMaven: ${maven.toString()}"
        setJava(maven.getJava())
        dsl.echo "maven = maven.getMaven()"
        setMaven(maven.getMaven())
        setMavenParams(maven.getMavenParams())
        setGroupId(maven.getGroupId())
        setArtifactId(maven.getArtifactId())
        setPackaging(maven.getPackaging())
        setRepositoryId(maven.getRepositoryId())
        setRepoUrl(maven.getRepoUrl())
        setLayout(maven.getLayout())
    }


}
