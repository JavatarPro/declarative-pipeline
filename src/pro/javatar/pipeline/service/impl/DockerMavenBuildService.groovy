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

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.orchestration.DockerService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerMavenBuildService extends MavenBuildService {

    MavenBuildService mavenBuildService
    DockerService dockerService

    DockerMavenBuildService(MavenBuildService mavenBuildService, DockerService dockerService) {
        dsl.echo "DockerMavenBuildService constructor with mavenBuildService & dockerService started"
        this.dockerService = dockerService
        this.mavenBuildService = mavenBuildService
        dsl.echo "DockerMavenBuildService constructor with mavenBuildService & dockerService finished"
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        dsl.echo "DockerMavenBuildService buildAndUnitTests started"
        mavenBuildService.buildAndUnitTests(releaseInfo)
        dockerService.dockerBuildImage(releaseInfo)
        dsl.echo "DockerMavenBuildService buildAndUnitTests finished"
    }

    @Override
    def populateReleaseInfo(ReleaseInfo releaseInfo) {
        mavenBuildService.populateReleaseInfo(releaseInfo)
        dockerService.populateReleaseInfo(releaseInfo)
    }

}
