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
package pro.javatar.pipeline.service

import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.jenkins.dsl.JenkinsDslServiceImpl
import pro.javatar.pipeline.util.Logger

/**
 * TODO move to pro.javatar.pipeline.jenkins package
 * This is jenkins dsl holder passing from jenkins to this library for integration purposes
 * @author Borys Zora
 * @since 2018-03-09
 */
public class PipelineDslHolder implements Serializable {

    public static def dsl

    static JenkinsDslService createDsl(def dslContext) {
        dsl = dslContext
        JenkinsDslService jenkinsDslService = new JenkinsDslServiceImpl(dsl)
        Logger.dslService = jenkinsDslService
        return jenkinsDslService
    }

    static def getProperty(String propertyName, def defaultValue) {
        try {
            return dsl.getProperty(propertyName)
        } catch (Exception e) {
            Logger.error("could not find property ${propertyName}, default will be used instead: ${defaultValue}")
            return defaultValue
        }
    }
}
