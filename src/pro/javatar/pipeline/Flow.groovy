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
package pro.javatar.pipeline

import pro.javatar.pipeline.model.ReleaseInfo;
import pro.javatar.pipeline.stage.Stage;
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class Flow implements Serializable {

    private List<Stage> stages = new ArrayList<>()
    ReleaseInfo releaseInfo = new ReleaseInfo()

    Flow(ReleaseInfo releaseInfo) {
        this.releaseInfo = releaseInfo
    }

    void addStage(Stage stage) {
        stages.add(stage);
    }

    void execute() {
        for (Stage stage: stages) {
            stage.setReleaseInfo(releaseInfo)
            execute(stage)
            if (stage.exitFromPipeline) {
                dsl.echo "pipeline will be stopped due to exitFromPipeline is ${stage.exitFromPipeline}"
                return
            }
        }
    }

    void execute(Stage stage) {
        dsl.stage(stage.getName()) {
            if (stage.shouldSkip()) {
                dsl.echo "stage will be skipped"
                return
            }
            stage.execute();
        }
    }

}
