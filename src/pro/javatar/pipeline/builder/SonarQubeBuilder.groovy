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

package pro.javatar.pipeline.builder;

import pro.javatar.pipeline.service.test.SonarQubeService

import static pro.javatar.pipeline.util.Utils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SonarQubeBuilder implements Serializable {

    private boolean enabled
    private boolean qualityGateEnabled
    private int qualityGateSleepInSeconds
    private String serverUrl
    private String params
    private String jenkinsSettingsName

    SonarQubeService build() {
        // TODO add validation if serverUrl is not provided fail build
        if (isBlank(params)) {
            params = getDefaultParams()
        }
        SonarQubeService sonarQubeService = new SonarQubeService(enabled, serverUrl, params, jenkinsSettingsName)
                .withQualityGateEnabled(qualityGateEnabled)
                .withQualityGateSleepInSeconds(qualityGateSleepInSeconds)
        return sonarQubeService
    }

    SonarQubeBuilder withEnabled(boolean enabled) {
        this.enabled = enabled
        return this
    }

    SonarQubeBuilder withServerUrl(String serverUrl) {
        this.serverUrl = serverUrl
        return this
    }

    SonarQubeBuilder withParams(String params) {
        this.params = params
        return this
    }

    SonarQubeBuilder withJenkinsSettingsName(String jenkinsSettingsName) {
        this.jenkinsSettingsName = jenkinsSettingsName
        return this
    }

    SonarQubeBuilder withQualityGateEnabled(boolean qualityGateEnabled) {
        this.qualityGateEnabled = qualityGateEnabled
        return this
    }

    SonarQubeBuilder withQualityGateSleepInSeconds(int qualityGateSleepInSeconds) {
        this.qualityGateSleepInSeconds = qualityGateSleepInSeconds
        return this
    }

    String getDefaultParams() {
        return "-Dsonar.jacoco.itReportPath=target/coverage-reports/jacoco-unit.exec " +
                "-Dsonar.scm.disabled=True " +
                "-Dsonar.exclusions=file:**/generated-sources/** " +
                "-Dsonar.coverage.exclusions=**/model/**,**/exceptions/**,**/entity/**,**/*TO.*,**/dto/**"
    }

    @Override
    public String toString() {
        return "SonarQubeBuilder{" +
                "enabled=" + enabled +
                ", qualityGateEnabled=" + qualityGateEnabled +
                ", qualityGateSleepInSeconds=" + qualityGateSleepInSeconds +
                ", serverUrl='" + serverUrl + '\'' +
                ", params='" + params + '\'' +
                ", jenkinsSettingsName='" + jenkinsSettingsName + '\'' +
                '}';
    }
}
