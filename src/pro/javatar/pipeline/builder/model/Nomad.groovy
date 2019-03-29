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

import pro.javatar.pipeline.util.Logger

import java.time.Period

/**
 * @author Borys Zora
 * @version 2019-03-29
 */
class Nomad implements Serializable {

    private Map<Environment, NomadItem> nomadConfig = new HashMap<>()

    Nomad() {
        Logger.debug("Nomad:default constructor")
    }

    Map<Environment, NomadItem> getNomadConfig() {
        return nomadConfig
    }

    void setNomadConfig(Map<Environment, NomadItem> nomadConfig) {
        this.nomadConfig = nomadConfig
    }

    Nomad withNomadConfig(Map<Environment, NomadItem> nomadConfig) {
        setNomadConfig(nomadConfig)
        return this
    }

    Nomad addNomadItem(Environment env, NomadItem nomadItem) {
        nomadConfig.put(env, nomadItem)
        return this
    }

    @Override
    public String toString() {
        return "Nomad{" +
                "nomadConfig=" + nomadConfig +
                '}';
    }

    static class NomadItem {

        String url

        String vcsConfig

        Period period

        String getUrl() {
            return url
        }

        void setUrl(String url) {
            this.url = url
        }

        NomadItem withUrl(String url) {
            setUrl(url)
            return this
        }

        String getVcsConfig() {
            return vcsConfig
        }

        void setVcsConfig(String vcsConfig) {
            this.vcsConfig = vcsConfig
        }

        NomadItem withVcsConfig(String vcsConfig) {
            setVcsConfig(vcsConfig)
            return this
        }

        Period getPeriod() {
            return period
        }

        void setPeriod(Period period) {
            this.period = period
        }

        NomadItem withPeriod(Period period) {
            setPeriod(period)
            return this
        }

        @Override
        public String toString() {
            return "NomadItem{" +
                    "url='" + url + '\'' +
                    ", vcsConfig='" + vcsConfig + '\'' +
                    ", period=" + period +
                    '}';
        }
    }
}
