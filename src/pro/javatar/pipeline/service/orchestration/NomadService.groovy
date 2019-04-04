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
package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.builder.model.Environment
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.service.infra.model.Infra
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO
import pro.javatar.pipeline.service.orchestration.model.NomadBO
import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest
import pro.javatar.pipeline.service.orchestration.template.JsonTemplatesRequestProvider
import pro.javatar.pipeline.util.Logger

/**
 * HashiCorp Nomad - docker orchestration implementation
 * Nomad has different deployment drivers, we cover only docker driver
 * @see "https://www.nomadproject.io/api/index.html"
 * @author Borys Zora
 * @version 2019-03-28
 */
class NomadService implements DockerOrchestrationService {

    private Map<Environment, NomadBO> nomadConfig

    private OrchestrationRequestProvider requestProvider = new JsonTemplatesRequestProvider()

    NomadService(Map<Environment, NomadBO> nomadConfig) {
        Logger.debug("NomadService:constructor: nomadConfig: ${nomadConfig.size()}")
        this.nomadConfig = nomadConfig
    }

    @Override
    def setup() {
        Logger.info("NomadService:setup: ${toString()}")
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        return dockerDeployContainer(new DeploymentRequestBO()
                .withImageName(imageName)
                .withImageVersion(imageVersion)
                .withDockerRepositoryUrl(dockerRepositoryUrl)
                .withEnvironment(environment))
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO deploymentRequest) {
        Logger.info("NomadService:dockerDeployContainer:deploymentRequest ${deploymentRequest}")
        NomadBO nomad = nomadConfig.get(deploymentRequest.getEnvironment())
        OrchestrationRequest request = new OrchestrationRequest()
                .withService(deploymentRequest.getImageName())
                .withEnv(deploymentRequest.getEnvironment().getValue())
                .withTemplateFolder("../${nomad.getVcsRepo().getName()}")
        requestProvider.createRequest(request)
        throw new PipelineException("fail fast, for testing purposes")
    }

    @Override
    def deployInfraContainer(Infra infra) {
        return null
    }

    @Override
    public String toString() {
        return "NomadService{" +
                "nomadConfig=" + nomadConfig.toString() +
                '}';
    }
}
