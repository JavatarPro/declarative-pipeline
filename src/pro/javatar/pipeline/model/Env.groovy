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

import pro.javatar.pipeline.exception.UnrecognizedEnvException
import pro.javatar.pipeline.exception.UnrecognizedRevisionControlTypeException

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum Env implements Serializable {
    DEV("dev"),
    QA("qa"),
    STAGING("uat"),
    PROD("prod")

    private final String value

    Env(String value) {
        this.value = value
    }

    String getValue() {
        return this.@value
    }

    static Env fromString(String env) {
        if (env == null) {
            throw new UnrecognizedRevisionControlTypeException("type is null")
        }
        if("develop".equalsIgnoreCase(env) || "dev".equalsIgnoreCase(env)) {
            return DEV
        }
        if ("qa".equalsIgnoreCase(env)) {
            return QA
        }
        if ("staging".equalsIgnoreCase(env) || "uat".equalsIgnoreCase(env)) {
            return STAGING
        }
        if ("prod".equalsIgnoreCase(env) || "production".equalsIgnoreCase(env)) {
            return PROD
        }
        throw new UnrecognizedEnvException("env ${env} is not recognized")
    }
}