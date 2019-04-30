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
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class UiAutoTestsService implements AutoTestsService {

    String uiSystemTestsJobName

    @Override
    void runAutoTests(String service, Env environment) throws PipelineException {
        String env = environment.getValue()
        Logger.info("runAutoTests with service: ${service}, env: ${env}")
        dsl.build job: uiSystemTestsJobName, parameters: [
                [$class: 'StringParameterValue', name: 'service', value: service],
                [$class: 'StringParameterValue', name: 'test_env', value: env]
        ]
    }

    UiAutoTestsService withUiSystemTestsJobName(String uiSystemTestsJobName) {
        this.uiSystemTestsJobName = uiSystemTestsJobName
        return this
    }

    @Override
    public String toString() {
        return "UiAutoTestsService{}";
    }
}
