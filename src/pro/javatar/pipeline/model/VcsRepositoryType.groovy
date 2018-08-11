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

package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedRevisionControlTypeException
import pro.javatar.pipeline.exception.UnrecognizedVcsRepositoryTypeException

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum VcsRepositoryType implements Serializable {

    BITBUCKET,
    GITHUB,
    GITLAB

    static VcsRepositoryType fromString(String type) {
        if (type == null) {
            throw new UnrecognizedRevisionControlTypeException("type is null")
        }
        if("bitbucket".equalsIgnoreCase(type)) {
            return BITBUCKET
        }
        if ("gitlab".equalsIgnoreCase(type)) {
            return GITLAB
        }
        if ("github".equalsIgnoreCase(type)) {
            return GITHUB
        }
        throw new UnrecognizedVcsRepositoryTypeException("type ${type} is not recognized")
    }

}