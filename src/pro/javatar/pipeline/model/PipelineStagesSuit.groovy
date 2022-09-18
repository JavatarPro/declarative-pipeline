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
package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedPipelineStagesSuitException
import pro.javatar.pipeline.stage.StageAware
import pro.javatar.pipeline.stage.VersionInfoStage

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum PipelineStagesSuit {

    SERVICE,
    SERVICE_WITH_DB,
    SERVICE_SIMPLE,
    LIBRARY,
    ANALYSE_SERVICE_VERSIONS(
            new VersionInfoStage()
    ),
    CUSTOM

    List<StageAware> stages = new ArrayList<>()

    PipelineStagesSuit(StageAware... stages) {
        this.stages.addAll(stages.toList())
    }

    List<StageAware> getStages() {
        return stages
    }

    static PipelineStagesSuit fromString(String suit) {
        if (suit == null) {
            throw new UnrecognizedPipelineStagesSuitException("suit is null")
        }
        return valueOf(suit.toUpperCase().replaceAll("-", "_"))
    }
}
