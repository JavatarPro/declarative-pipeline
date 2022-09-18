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
package pro.javatar.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.config.AutoTestConfig
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.jenkins.api.JenkinsExecutor
import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.test.AutoTestsService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class AutoTestsStage extends Stage {

    AutoTestsService autoTestsService;

    JenkinsDsl jenkinsDslService;

    AutoTestConfig autoTestConfig;

    AutoTestsStage(AutoTestsService autoTestsService,
                   JenkinsDsl jenkinsDslService,
                   AutoTestConfig autoTestConfig) {
        this.autoTestsService = autoTestsService
        this.jenkinsDslService = jenkinsDslService
        this.autoTestConfig = autoTestConfig
    }

    @Override
    void execute() throws PipelineException {
        Logger.info("AutoTestsStage execute started: " + toString())
        // TODO read timeout from config
        jenkinsDslService.executeWithinTimeoutInSpecifiedDirectory(autoTestConfig.timeout(),
                releaseInfo().getRepoFolder(), getJenkinsExecutor());
        Logger.info("AutoTestsStage execute finished")
    }

    private JenkinsExecutor getJenkinsExecutor() {
        new JenkinsExecutor() {
            @Override
            void execute() {
                autoTestsService.runAutoTests(releaseInfo().getServiceName(), Env.DEV)
            }
        }
    }

    @Override
    boolean shouldSkip() {
        return !autoTestConfig.enabled();
    }

    @Override
    String name() {
        return "auto tests"
    }

    @NonCPS
    @Override
    public String toString() {
        return "AutoTestsStage{" +
                ", autoTestsService=" + autoTestsService +
                '}';
    }
}
