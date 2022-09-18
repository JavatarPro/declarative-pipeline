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
package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedDockerOrchestrationTypeException
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum DockerOrchestrationServiceType implements Serializable {

    NOMAD,
    K8S,
    SSH

    static DockerOrchestrationServiceType fromString(String type) {
        if (type == null) {
            Logger.info("orchestration type is null, by default 'k8s' will be used")
            return K8S
        }
        if("nomad".equalsIgnoreCase(type)) {
            return NOMAD
        }
        if ("k8s".equalsIgnoreCase(type) || "kubernetes".equalsIgnoreCase(type)) {
            return K8S
        }
        if ("ssh".equalsIgnoreCase(type) || "bash".equalsIgnoreCase(type) || "sh".equalsIgnoreCase(type)) {
            return SSH
        }
        throw new UnrecognizedDockerOrchestrationTypeException("type ${type} is not recognized")
    }

}