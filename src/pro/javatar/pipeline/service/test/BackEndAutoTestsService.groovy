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

package pro.javatar.pipeline.service.test

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.model.Env
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class BackEndAutoTestsService implements AutoTestsService {

    SonarQubeService sonarQubeService
    String mavenParams = ""
    String jobName = 'common/system-tests'
    boolean skipSystemTests = false
    boolean skipCodeQualityVerification = false
    int sleepInSeconds = 60

    @Override
    void runAutoTests(String service, Env environment) throws PipelineException {
        dsl.echo "runAutoTests with service: ${service}, environment: ${environment.getValue()}"

        dsl.parallel 'Integration tests': {
            integrationTests(mavenParams)
        }, 'Systests': {
            systests(service, environment.value)
        }

        verifyCodeQuality(service)
    }

    // TODO remove hardcode "repo" folder
    def integrationTests(String mavenParams) {
        dsl.echo "integrationTests with mavenParams: ${mavenParams}"
        dsl.sh "mvn -B verify ${mavenParams} -DskipITs=false"
        //dsl.sh "mvn failsafe:integration-test -DskipITs=false ${mavenParams}"
    }

    def systests(String service, String env) {
        dsl.echo "backend systests started"
        if (skipSystemTests) {
            dsl.echo "backend systests will be skipped because skipSystemTests: ${skipSystemTests}"
            return
        }
        dsl.sleep(sleepInSeconds)  // Sleeping for 60 sec. Wait until service is registered on eureka.
        dsl.build job: jobName, parameters: [
                [$class: 'StringParameterValue', name: 'service', value: service],
                [$class: 'StringParameterValue', name: 'test_env', value: env]
        ]
    }

    def verifyCodeQuality(String service) {
        dsl.echo "start verifyCodeQuality for service: ${service}"
        // sonarqube validation should be run after integration tests were finished
        // and after jacoco coverage report was generated
        if (!skipCodeQualityVerification) {
            sonarQubeService.sonar()
        } else {
            dsl.echo "WARN: verifyCodeQuality for service: ${service} will be skipped"
        }
        dsl.echo "finished verifyCodeQuality for service: ${service}"
    }

    BackEndAutoTestsService withSkipSystemTests(boolean skipSystemTests) {
        if (isBlank(jobName)) return this
        this.skipSystemTests = skipSystemTests
        return this
    }

    BackEndAutoTestsService withJobName(String jobName) {
        if (isBlank(jobName)) return this
        this.jobName = jobName
        return this
    }

    BackEndAutoTestsService withMavenParams(String mavenParams) {
        if (isBlank(jobName)) return this
        this.mavenParams = mavenParams
        return this
    }

    BackEndAutoTestsService withSleepInSeconds(int sleepInSeconds) {
        if (sleepInSeconds < 0) return this
        this.sleepInSeconds = sleepInSeconds
        return this
    }

    BackEndAutoTestsService withSonarQubeService(SonarQubeService sonarQubeService) {
        if (sonarQubeService == null) {
            skipCodeQualityVerification = true
            return this
        }
        this.sonarQubeService = sonarQubeService
        return this
    }

    @Override
    public String toString() {
        return "BackEndAutoTestsService{" +
                "sonarQubeService=" + sonarQubeService +
                ", mavenParams='" + mavenParams + '\'' +
                ", jobName='" + jobName + '\'' +
                ", skipSystemTests=" + skipSystemTests +
                ", skipCodeQualityVerification=" + skipCodeQualityVerification +
                ", sleepInSeconds=" + sleepInSeconds +
                '}';
    }
}
