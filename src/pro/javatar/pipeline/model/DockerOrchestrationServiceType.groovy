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
import pro.javatar.pipeline.exception.UnrecognizedRevisionControlTypeException

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum DockerOrchestrationServiceType implements Serializable {

    MESOS,
    NOMAD,
    KUBERNETES,
    SSH

    static DockerOrchestrationServiceType fromString(String type) {
        if (type == null) {
            throw new UnrecognizedRevisionControlTypeException("type is null")
        }
        if("mesos".equalsIgnoreCase(type) || "mesosphere".equalsIgnoreCase(type)
                || "marathon".equalsIgnoreCase(type)) {
            return MESOS
        }
        if("nomad".equalsIgnoreCase(type) || "HashiCorp".equalsIgnoreCase(type)) {
            return NOMAD
        }
        if ("kub".equalsIgnoreCase(type) || "kubernetes".equalsIgnoreCase(type)) {
            return KUBERNETES
        }
        if ("ssh".equalsIgnoreCase(type) || "bash".equalsIgnoreCase(type) || "sh".equalsIgnoreCase(type)) {
            return SSH
        }
        throw new UnrecognizedDockerOrchestrationTypeException("type ${type} is not recognized")
    }

}