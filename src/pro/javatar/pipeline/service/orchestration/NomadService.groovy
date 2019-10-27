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

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.service.infra.model.Infra
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO
import pro.javatar.pipeline.service.orchestration.model.NomadBO
import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest
import pro.javatar.pipeline.service.orchestration.template.JsonTemplatesRequestProvider
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.isNotBlank

/**
 * HashiCorp Nomad - docker orchestration implementation
 * Nomad has different deployment drivers, we cover only docker driver
 * @see "https://www.nomadproject.io/api/index.html"
 * @author Borys Zora
 * @version 2019-03-28
 */
class NomadService implements DockerOrchestrationService {

    private Map<String, NomadBO> nomadConfig

    private OrchestrationRequestProvider requestProvider

    NomadService(Map<String, NomadBO> nomadConfig) {
        this(nomadConfig, new JsonTemplatesRequestProvider())
        Logger.debug("NomadService:constructor with nomadConfig only")
    }

    NomadService(Map<String, NomadBO> nomadConfig, OrchestrationRequestProvider requestProvider) {
        Logger.debug("NomadService:constructor with nomadConfig and requestProvider")
        this.nomadConfig = nomadConfig
        this.requestProvider = requestProvider
    }

    @Override
    def setup() {
        Logger.info("NomadService:setup: " + toString())
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        return dockerDeployContainer(new DeploymentRequestBO()
                .withImageName(imageName)
                .withService(imageName)
                .withImageVersion(imageVersion)
                .withDockerRepositoryUrl(dockerRepositoryUrl)
                .withEnvironment(environment))
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO deploymentRequest) {
        Logger.info("NomadService:dockerDeployContainer:deploymentRequest ${deploymentRequest}")
        def request = toOrchestrationRequest(deploymentRequest)
        String requestBody = requestProvider.createRequest(request)
        createOrReplaceDockerService(deploymentRequest, requestBody)
        throw new PipelineException("fail fast, for testing purposes")
    }

    def createOrReplaceDockerService(DeploymentRequestBO req, String requestBody) {
        NomadBO nomad = nomadConfig.get(req.getEnvironment().getValue())
        String nomadUrl = nomad.getUrl()
        String currentVersion = getCurrentVersion(nomadUrl)
        String versionToDeploy = req.getImageVersion()
        if (isNotBlank(currentVersion)) {
            replaceDockerService(requestBody)
        } else {
            createDockerService(requestBody)
        }
    }

    def createDockerService(String s) {

    }

    def replaceDockerService(String s) {
        null
    }


    String getCurrentVersion(String nomadUrl) {
        null // TODO
    }

    @Override
    def deployInfraContainer(Infra infra) {
        return null
    }

    OrchestrationRequest toOrchestrationRequest(DeploymentRequestBO req) {
        NomadBO nomad = nomadConfig.get(req.getEnvironment().getValue())
        OrchestrationRequest result = new OrchestrationRequest()
                .withEnv(req.getEnvironment().getValue())
                .withDockerRegistry(req.dockerRegistry.registry)
                .withDockerImage("${req.getImageName()}:${req.getImageVersion()}")
                .withService(req.getService())
                .withTemplateFolder(nomad.getNomadFolder())
                .withBuildNumber(req.getBuildNumber())
        return result
    }

    @NonCPS
    @Override
    public String toString() {
        return "NomadService{" +
                "nomadConfig=" + nomadConfig.toString() +
                '}';
    }
}
