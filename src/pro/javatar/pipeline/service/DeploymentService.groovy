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
package pro.javatar.pipeline.service

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
interface DeploymentService extends Serializable {

    void deployArtifact(Env environment, ReleaseInfo releaseInfo)

}