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

/**
 * Author : Borys Zora
 * Date Created: 5/27/18 23:05
 */
enum RevisionControlType {

    GIT,
    MERCURIAL

    static RevisionControlType fromString(String type) {
        if (type == null) {
            throw new UnrecognizedRevisionControlTypeException("type is null")
        }
        if("mercurial".equalsIgnoreCase(type) || "hg".equalsIgnoreCase(type)) {
            return MERCURIAL
        }
        if ("git".equalsIgnoreCase(type)) {
            return GIT
        }
        throw new UnrecognizedRevisionControlTypeException("type ${type} is not recognized")
    }

}
