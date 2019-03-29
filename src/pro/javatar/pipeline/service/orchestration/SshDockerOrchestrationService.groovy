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
package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.service.infra.model.Infra
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 3/22/18 22:28
 */
class SshDockerOrchestrationService implements DockerOrchestrationService {

    @Override
    def setup() {
        throw new UnsupportedOperationException("SshDockerOrchestrationService does not supported yet")
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        dsl.echo "SshDockerOrchestrationService started dockerDeployContainer(imageName: ${imageName}, " +
                "imageVersion: ${imageVersion}, dockerRepositoryUrl: ${dockerRepositoryUrl}, environment: ${environment})"

        dsl.echo "SshDockerOrchestrationService finished dockerDeployContainer(imageName: ${imageName}, " +
                "imageVersion: ${imageVersion}, dockerRepositoryUrl: ${dockerRepositoryUrl}, environment: ${environment})"
        throw new UnsupportedOperationException("SshDockerOrchestrationService does not supported yet")
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO deploymentRequest) {
        throw new UnsupportedOperationException("SshDockerOrchestrationService does not supported yet")
    }

    @Override
    def deployInfraContainer(Infra infra) {
        throw new UnsupportedOperationException("SshDockerOrchestrationService does not supported yet")
    }
}
