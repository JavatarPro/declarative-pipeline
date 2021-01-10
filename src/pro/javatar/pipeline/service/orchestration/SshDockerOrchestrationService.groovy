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

import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO
import pro.javatar.pipeline.util.Logger

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
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO req) {
        Logger.info("SshDockerOrchestrationService started dockerDeployContainer(imageName: ${req.imageName}, " +
                "imageVersion: ${req.imageVersion}, dockerRepositoryUrl: ${req.dockerRegistry.getRegistry()}, environment: ${req.env.value})")

        Logger.info("SshDockerOrchestrationService finished dockerDeployContainer(imageName: ${req.imageName}, " +
                "imageVersion: ${req.imageVersion}, dockerRepositoryUrl: ${req.dockerRegistry.getRegistry()}, environment: ${req.env.value})")

        throw new UnsupportedOperationException("SshDockerOrchestrationService does not supported yet")
    }

}
