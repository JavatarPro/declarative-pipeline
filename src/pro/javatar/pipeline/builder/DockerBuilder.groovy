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
package pro.javatar.pipeline.builder

import pro.javatar.pipeline.model.DockerOrchestrationServiceType
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.service.orchestration.KubernetesService
import pro.javatar.pipeline.service.orchestration.MesosService
import pro.javatar.pipeline.service.orchestration.SshDockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.model.DockerRegistryBO

import static pro.javatar.pipeline.model.DockerOrchestrationServiceType.KUBERNETES
import static pro.javatar.pipeline.model.DockerOrchestrationServiceType.MESOS
import static pro.javatar.pipeline.model.DockerOrchestrationServiceType.SSH
import static pro.javatar.pipeline.model.DockerOrchestrationServiceType.fromString

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerBuilder implements Serializable {

    Map<String, DockerRegistryBO> dockerRegistries = new HashMap<>()

    private String customDockerFileName = ""
    private DockerOrchestrationService orchestrationService

    DockerService build() {
        DockerService dockerService = new DockerService(dockerRegistries, orchestrationService)
        dockerService.setCustomDockerFileName(customDockerFileName)
        return dockerService
    }

    DockerBuilder withDockerRegistry(String env, String credentialsId, String registry) {
        dockerRegistries.put(env, new DockerRegistryBO()
                .withCredentialsId(credentialsId)
                .withRegistry(registry))
        return this
    }

    DockerBuilder withOrchestrationServiceType(String dockerOrchestrationServiceType) {
        DockerOrchestrationServiceType type = fromString(dockerOrchestrationServiceType)
        if (type == KUBERNETES) {
            this.orchestrationService = new KubernetesService()
        } else if (type == MESOS) {
            this.orchestrationService = new MesosService()
        } else if (type == SSH) {
            this.orchestrationService = new SshDockerOrchestrationService();
        }
        return this
    }

    DockerBuilder withOrchestrationService(DockerOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService
        return this
    }

    DockerOrchestrationService getOrchestrationService() {
        return orchestrationService
    }

    void setOrchestrationService(DockerOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService
    }

    DockerBuilder withCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
        return this
    }

    String getCustomDockerFileName() {
        return customDockerFileName
    }

    void setCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
    }

    @Override
    public String toString() {
        return "DockerBuilder{" +
                "dockerRegistries=" + dockerRegistries +
                ", customDockerFileName='" + customDockerFileName + '\'' +
                ", orchestrationService=" + orchestrationService +
                '}';
    }

}
