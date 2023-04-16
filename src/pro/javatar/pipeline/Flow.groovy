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

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.init.FlowBuilder
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.stage.StageAware;
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class Flow implements Serializable {

    public static final DEFAULT_CONFIG_FILE = "declarative-pipeline.yml"

    private List<StageAware> stages = new ArrayList<>();
    private ReleaseInfo releaseInfo = new ReleaseInfo();
    private JenkinsDsl dsl;

    Flow(ReleaseInfo releaseInfo, JenkinsDsl dsl) {
        this.releaseInfo = releaseInfo;
        this.dsl = dsl;
    }

    static Flow of(def dsl) {
        return of(dsl, DEFAULT_CONFIG_FILE)
    }

    static Flow of(def dsl, String config) {
        return FlowBuilder.build(dsl, config)
    }

    void execute() {
        for (StageAware stage: stages) {
            executeStage(stage)
            if (stage.isFiredExitFromPipeline()) {
                Logger.warn("pipeline will be stopped due to exitFromPipeline is triggered")
                return
            }
        }
    }

    void executeStage(StageAware stage) {
        stage.propagateReleaseInfo(releaseInfo)
        if (stage.shouldSkip()) {
            Logger.warn("Stage: ${stage.name()} will be skipped due to configuration settings")
            return;
        }
        dsl.executeStage(stage);
    }

    Flow addStage(StageAware stage) {
        stages.add(stage);
        return this;
    }

    Flow addStages(List<StageAware> list) {
        stages.addAll(list);
        return this;
    }

    List<String> getStageNames() {
        stages.collect {it.name()}
    }
}
