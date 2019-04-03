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

/**
 * Author : Borys Zora
 * Date Created: 4/5/18 12:36
 */
class BackEndAutoTestsLibrary extends BackEndAutoTestsService {

    BackEndAutoTestsLibrary(BuildService buildService) {
        super(buildService)
        Logger.info("BackEndAutoTestsLibrary:constructor finished")
    }

    @Override
    void runAutoTests(String service, Env env) throws PipelineException {
        Logger.info("BackEndAutoTestsLibrary:runAutoTests with service: ${service}, env: ${env.getValue()}")
        buildService.runIntegrationTests()
        verifyCodeQuality(service)
    }
}
