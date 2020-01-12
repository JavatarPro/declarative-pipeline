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
package pro.javatar.pipeline.builder

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.model.PipelineStagesSuit
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.test.BackEndAutoTestsLibrary
import pro.javatar.pipeline.service.test.BackEndAutoTestsService
import pro.javatar.pipeline.service.test.SonarQubeService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.isBlank

/**
 * Author : Borys Zora
 * Date Created: 3/29/18 14:06
 */
class BackEndAutoTestsServiceBuilder implements Serializable {

    private AutoTestConfig config;

    BuildService buildService

    SonarQubeService sonarQubeService

    String jobName

    Boolean skipSystemTests

    Boolean skipCodeQualityVerification

    Integer sleepInSeconds = -1

    PipelineStagesSuit suit

    BackEndAutoTestsServiceBuilder(AutoTestConfig config) {
        Logger.debug("BackEndAutoTestsServiceBuilder:default constructor")
        this.config = config;
    }

    BackEndAutoTestsService build() {
        Logger.info("Current suit is $suit")
        BackEndAutoTestsService autoTestsService
        if (suit == PipelineStagesSuit.SERVICE) {
            autoTestsService = new BackEndAutoTestsService(buildService)
        } else if (suit == PipelineStagesSuit.SERVICE_WITH_DB) {
            autoTestsService = new BackEndAutoTestsService(buildService)
        } else if (suit == PipelineStagesSuit.LIBRARY) {
            autoTestsService = new BackEndAutoTestsLibrary(buildService)
        }
        return autoTestsService.withSkipSystemTests(!config.enabled())
                .withSleepInSeconds(sleepInSeconds)
                .withSonarQubeService(sonarQubeService)
                .withJobName(jobName)
    }

    BackEndAutoTestsServiceBuilder withBuildService(BuildService buildService) {
        this.buildService = buildService
        return this
    }

    BackEndAutoTestsServiceBuilder withJobName(String jobName) {
        this.jobName = jobName
        return this
    }

    BackEndAutoTestsServiceBuilder withSkipSystemTests(Boolean skipSystemTests) {
        this.skipSystemTests = skipSystemTests
        return this
    }

    BackEndAutoTestsServiceBuilder withSkipSystemTests(String skipSystemTests) {
        this.skipSystemTests = Boolean.valueOf(skipSystemTests)
        return this
    }

    BackEndAutoTestsServiceBuilder withSkipCodeQualityVerification(Boolean skipCodeQualityVerification) {
        this.skipCodeQualityVerification = skipCodeQualityVerification
        return this
    }

    BackEndAutoTestsServiceBuilder withSkipCodeQualityVerification(String skipCodeQualityVerification) {
        this.skipCodeQualityVerification = Boolean.valueOf(skipCodeQualityVerification)
        return this
    }

    BackEndAutoTestsServiceBuilder withSleepInSeconds(Integer sleepInSeconds) {
        if (sleepInSeconds == null) {
            return this
        }
        this.sleepInSeconds = sleepInSeconds
        return this
    }

    BackEndAutoTestsServiceBuilder withSleepInSeconds(String sleepInSeconds) {
        if (isBlank(sleepInSeconds)) {
            return this
        }
        this.sleepInSeconds = Integer.parseInt(sleepInSeconds)
        return this
    }

    BackEndAutoTestsServiceBuilder withSonarQubeService(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService
        return this
    }

    BackEndAutoTestsServiceBuilder withSonarQubeService(SonarQubeBuilder sonarQubeBuilder) {
        this.sonarQubeService = sonarQubeBuilder.build()
        return this
    }

    BackEndAutoTestsServiceBuilder withSuit(PipelineStagesSuit suit) {
        this.suit = suit
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "BackEndAutoTestsServiceBuilder{" +
                "sonarQubeService=" + sonarQubeService +
                ", jobName='" + jobName + '\'' +
                ", skipSystemTests=" + skipSystemTests +
                ", skipCodeQualityVerification=" + skipCodeQualityVerification +
                ", sleepInSeconds=" + sleepInSeconds +
                '}';
    }
}
