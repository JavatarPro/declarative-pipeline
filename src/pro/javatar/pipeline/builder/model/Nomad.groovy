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
package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

import java.time.Period

/**
 * @author Borys Zora
 * @version 2019-03-29
 */
class Nomad implements Serializable {

    private Map<String, NomadItem> nomadConfig = new HashMap<>()

    Nomad() {
        Logger.debug("Nomad:default constructor")
    }

    Map<String, NomadItem> getNomadConfig() {
        return nomadConfig
    }

    void setNomadConfig(Map<String, NomadItem> nomadConfig) {
        if (nomadConfig == null) {
            return
        }
        this.nomadConfig = nomadConfig
    }

    Nomad withNomadConfig(Map<String, NomadItem> nomadConfig) {
        setNomadConfig(nomadConfig)
        return this
    }

    Nomad addNomadItem(String env, NomadItem nomadItem) {
        Logger.trace("Nomad:addNomadItem: env: " + env.toString() + ", nomadItem: " + nomadItem.toString())
        // does not work if map Map<Environment, NomadItem>
        nomadConfig.put(env, nomadItem)
        Logger.trace("Nomad:addNomadItem: this.toString(): " + toString())
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "Nomad{" +
                "nomadConfig=" + nomadConfig.size() +
                '}';
    }
}
