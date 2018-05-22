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

package pro.javatar.pipeline.builder

import pro.javatar.pipeline.service.test.BackEndAutoTestsLibrary
import pro.javatar.pipeline.service.test.BackEndAutoTestsService
import pro.javatar.pipeline.service.test.SonarQubeService

import static pro.javatar.pipeline.util.Utils.isBlank

/**
 * Author : Borys Zora
 * Date Created: 3/29/18 14:06
 */
class BackEndAutoTestsServiceBuilder {

    SonarQubeService sonarQubeService

    String jobName

    boolean skipSystemTests

    boolean skipCodeQualityVerification

    int sleepInSeconds = -1

    BackEndAutoTestsService build(SonarQubeBuilder sonarQubeBuilder) {
        if (sonarQubeBuilder != null) {
            this.sonarQubeService = sonarQubeBuilder.build()
        }
        return build()
    }

    BackEndAutoTestsService buildLibrary(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService
        return buildLibrary()
    }

    BackEndAutoTestsService buildLibrary(SonarQubeBuilder sonarQubeBuilder) {
        if (sonarQubeBuilder != null) {
            this.sonarQubeService = sonarQubeBuilder.build()
        }
        return buildLibrary()
    }

    BackEndAutoTestsService build(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService
        return build()
    }

    BackEndAutoTestsService build() {
        return new BackEndAutoTestsService()
                .withSkipSystemTests(skipSystemTests)
                .withSleepInSeconds(sleepInSeconds)
                .withSonarQubeService(sonarQubeService)
                .withJobName(jobName)
    }

    BackEndAutoTestsService buildLibrary() {
        return new BackEndAutoTestsLibrary()
                .withSkipSystemTests(skipSystemTests)
                .withSleepInSeconds(sleepInSeconds)
                .withSonarQubeService(sonarQubeService)
                .withJobName(jobName)
    }

    String getJobName() {
        return jobName
    }

    BackEndAutoTestsServiceBuilder withJobName(String jobName) {
        this.jobName = jobName
        return this
    }

    boolean getSkipSystemTests() {
        return skipSystemTests
    }

    BackEndAutoTestsServiceBuilder withSkipSystemTests(String skipSystemTests) {
        this.skipSystemTests = Boolean.valueOf(skipSystemTests)
        return this
    }

    boolean getSkipCodeQualityVerification() {
        return skipCodeQualityVerification
    }

    BackEndAutoTestsServiceBuilder withSkipCodeQualityVerification(String skipCodeQualityVerification) {
        this.skipCodeQualityVerification = Boolean.valueOf(skipCodeQualityVerification)
        return this
    }

    int getSleepInSeconds() {
        return sleepInSeconds
    }

    BackEndAutoTestsServiceBuilder withSleepInSeconds(String sleepInSeconds) {
        if (isBlank(sleepInSeconds)) return this
        this.sleepInSeconds = Integer.parseInt(sleepInSeconds)
        return this
    }

    SonarQubeService getSonarQubeService() {
        return sonarQubeService
    }

    BackEndAutoTestsServiceBuilder withSonarQubeService(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService
        return this
    }

    BackEndAutoTestsServiceBuilder withSonarQubeService(SonarQubeBuilder sonarQubeBuilder) {
        this.sonarQubeService = sonarQubeBuilder.build()
        return this
    }

    void setSonarQubeService(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService
    }

    void setJobName(String jobName) {
        this.jobName = jobName
    }

    void setSkipSystemTests(boolean skipSystemTests) {
        this.skipSystemTests = skipSystemTests
    }

    void setSkipCodeQualityVerification(boolean skipCodeQualityVerification) {
        this.skipCodeQualityVerification = skipCodeQualityVerification
    }

    void setSleepInSeconds(int sleepInSeconds) {
        this.sleepInSeconds = sleepInSeconds
    }

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
