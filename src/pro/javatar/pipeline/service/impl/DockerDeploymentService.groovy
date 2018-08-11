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

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.DeploymentService
import pro.javatar.pipeline.service.orchestration.DockerService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerDeploymentService implements DeploymentService {

    ReleaseInfo releaseInfo
    DockerService dockerService

    DockerDeploymentService(ReleaseInfo releaseInfo, DockerService dockerService) {
        this.releaseInfo = releaseInfo
        this.dockerService = dockerService
    }

    @Override
    void deployArtifact(Env environment, ReleaseInfo releaseInfo) {
        String version = releaseInfo.getReleaseVersion()
        dsl.echo "DockerDeploymentService deployArtifact to ${environment.getValue()} env and version: ${version} started"
        // TODO move & split this method to do it on build and release stage
        if (environment == Env.DEV) {
            dockerService.dockerPublish(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion(), environment)
        }
        dockerService.dockerDeployContainer(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion(),
                environment)
        dsl.echo "DockerDeploymentService deployArtifact to ${environment.getValue()} env and version: ${version} finished"
    }

}
