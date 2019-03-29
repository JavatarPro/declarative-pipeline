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

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.test.AutoTestsService
import pro.javatar.pipeline.service.vcs.RevisionControlService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class AutoTestsStage extends Stage {

    AutoTestsService autoTestsService

    RevisionControlService revisionControl

    AutoTestsStage(AutoTestsService autoTestsService,
                   RevisionControlService revisionControlService) {
        this.autoTestsService = autoTestsService
        this.revisionControl = revisionControlService
    }

    @Override
    void execute() throws Exception {
        dsl.echo "AutoTestsStage execute started: ${toString()}"
        dsl.timeout(time: 10, unit: 'MINUTES') {
            dsl.dir(revisionControl.folder) {
                autoTestsService.runAutoTests(releaseInfo.getServiceName(), Env.DEV)
            }
        }
        dsl.echo "AutoTestsStage execute finished"
    }

    @Override
    String getName() {
        return "auto tests"
    }

    @Override
    public String toString() {
        return "AutoTestsStage{" +
                ", autoTestsService=" + autoTestsService +
                '}';
    }
}
