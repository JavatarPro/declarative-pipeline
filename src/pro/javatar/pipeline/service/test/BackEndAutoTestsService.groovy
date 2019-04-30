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

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class BackEndAutoTestsService implements AutoTestsService {

    protected BuildService buildService
    SonarQubeService sonarQubeService
    String jobName = 'common/system-tests'

    boolean skipSystemTests = false
    boolean skipCodeQualityVerification = false
    int sleepInSeconds = 60

    BackEndAutoTestsService(BuildService buildService) {
        if (buildService == null) {
            String msg = "BackEndAutoTestsService:constructor:buildService must be provided but was null"
            Logger.error(msg)
            throw new PipelineException(msg)
        }
        Logger.debug("BackEndAutoTestsService:constructor ${buildService.getClass().getCanonicalName()}")
        this.buildService = buildService
    }

    @Override
    void runAutoTests(String service, Env env) throws PipelineException {
        Logger.info("BackEndAutoTestsService:runAutoTests with service: ${service}, env: ${env.getValue()}")

        dsl.parallel 'Integration tests': {
            buildService.runIntegrationTests()
        }, 'Systests': {
            systests(service, env.value)
        }

        verifyCodeQuality(service)
    }

    def systests(String service, String env) {
        Logger.info("BackEndAutoTestsService:systests backend job: ${jobName} started")
        if (skipSystemTests) {
            Logger.info("BackEndAutoTestsService:systests: will be skipped, skipSystemTests: ${skipSystemTests}")
            return
        }
        dsl.sleep(sleepInSeconds)  // Sleeping for 60 sec. Wait until service is registered on eureka.
        dsl.build job: jobName, parameters: [
                [$class: 'StringParameterValue', name: 'service', value: service],
                [$class: 'StringParameterValue', name: 'test_env', value: env]
        ]
        Logger.info("BackEndAutoTestsService:systests backend job: ${jobName} finished")
    }

    def verifyCodeQuality(String service) {
        Logger.info("BackEndAutoTestsService:verifyCodeQuality: start verifyCodeQuality for service: ${service}")
        // sonarqube validation should be run after integration tests were finished
        // and after jacoco coverage report was generated
        if (!skipCodeQualityVerification) {
            sonarQubeService.sonar()
        } else {
            Logger.warn("BackEndAutoTestsService:verifyCodeQuality: for service: ${service} will be skipped")
        }
        Logger.info("BackEndAutoTestsService:verifyCodeQuality:finished verifyCodeQuality for service: ${service}")
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
                "buildService=" + buildService +
                ", sonarQubeService=" + sonarQubeService +
                ", jobName='" + jobName + '\'' +
                ", skipSystemTests=" + skipSystemTests +
                ", skipCodeQualityVerification=" + skipCodeQualityVerification +
                ", sleepInSeconds=" + sleepInSeconds +
                '}';
    }
}
