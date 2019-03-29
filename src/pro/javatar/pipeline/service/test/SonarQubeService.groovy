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

package pro.javatar.pipeline.service.test

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SonarQubeService implements Serializable {

    boolean sonarQubeEnabled
    boolean qualityGateEnabled
    int qualityGateSleepInSeconds
    String sonarQubeServerUrl
    String sonarQubeParams
    String sonarQubeJenkinsSettingsName

    SonarQubeService(boolean sonarQubeEnabled,  String sonarQubeServerUrl, String sonarQubeParams,
                     String sonarQubeJenkinsSettingsName) {
        this.sonarQubeEnabled = sonarQubeEnabled
        this.sonarQubeServerUrl = sonarQubeServerUrl
        this.sonarQubeParams = sonarQubeParams
        this.sonarQubeJenkinsSettingsName = sonarQubeJenkinsSettingsName
    }

    def sonar() {
        if (!sonarQubeEnabled) {
            dsl.echo "sonar is disabled"
        }
        dsl.timeout(time: 5, unit: 'MINUTES') {
            verifyQuality()
            checkStatusOfQualityGateVerifier()
        }
    }

    def verifyQuality() {
        String sonarMvnCmd = "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar " +
                "-Dsonar.host.url=${sonarQubeServerUrl} ${sonarQubeParams}"

        try {
            // tries to use defined settings of "SonarQube Scanner for Jenkins" plugin
            // if the plugin is installed and configured then runs the SonarQube
            // tests and shows information banner in the job
            // otherwise, just runs the SonarQube tests
            dsl.withSonarQubeEnv(sonarQubeJenkinsSettingsName) {
                dsl.sh sonarMvnCmd
            }
        } catch (java.lang.NoSuchMethodError | java.lang.IllegalArgumentException e) {
            dsl.echo "\nWarning: Jenkins plugin \"SonarQube Scanner for Jenkins\" not installed " +
                    "or settings name \"${sonarQubeJenkinsSettingsName}\" not found.\n"
            dsl.sh sonarMvnCmd
        }
    }

    def checkStatusOfQualityGateVerifier() {
        if (!qualityGateEnabled) {
            dsl.echo "qualityGateEnabled: ${qualityGateEnabled} is disabled"
            return
        }
        dsl.echo "wait for sonar check: ${qualityGateSleepInSeconds} seconds"
        dsl.sleep(qualityGateSleepInSeconds)
        def qualityGate = dsl.waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
        if (qualityGate.status != 'OK') {
            dsl.error "Pipeline aborted due to quality gate failure: ${qualityGate.status}"
        }
    }

    SonarQubeService withQualityGateEnabled(boolean qualityGateEnabled) {
        this.qualityGateEnabled = qualityGateEnabled
        return this
    }

    SonarQubeService withQualityGateSleepInSeconds(int qualityGateSleepInSeconds) {
        this.qualityGateSleepInSeconds = qualityGateSleepInSeconds
        return this
    }

    @Override
    public String toString() {
        return "SonarQubeService{" +
                "sonarQubeEnabled=" + sonarQubeEnabled +
                ", qualityGateEnabled=" + qualityGateEnabled +
                ", qualityGateSleepInSeconds=" + qualityGateSleepInSeconds +
                ", sonarQubeServerUrl='" + sonarQubeServerUrl + '\'' +
                ", sonarQubeParams='" + sonarQubeParams + '\'' +
                ", sonarQubeJenkinsSettingsName='" + sonarQubeJenkinsSettingsName + '\'' +
                '}';
    }
}
