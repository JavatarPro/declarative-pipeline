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

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.service.ContextHolder
import pro.javatar.pipeline.service.ReleaseService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class ReleaseArtifactsStage extends Stage {

    ReleaseArtifactsStage() {
        Logger.debug("ReleaseArtifactsStage:constructor")
    }

    @Override
    void execute() throws PipelineException {
        Logger.info("ReleaseArtifactsStage execute started")
        ReleaseService releaseService = ContextHolder.get(ReleaseService.class)
        dsl.dir(releaseInfo().getRepoFolder()) {
            releaseService.release(releaseInfo())
            dsl.currentBuild.description = releaseInfo().getServiceName() + ":" + releaseInfo().releaseVersion();
        }
        Logger.info("ReleaseArtifactsStage execute finished")
    }

    @Override
    String name() {
        return "release"
    }
}
