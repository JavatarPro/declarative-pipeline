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

package pro.javatar.pipeline.stage.deploy

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.DeploymentService
import pro.javatar.pipeline.stage.Stage

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class DeployToEnvStage extends Stage {

    protected DeploymentService deploymentService

    @Override
    void execute() throws PipelineException {
        dsl.echo "DeployToEnvStage to ${getEnv().getValue()} execute started: ${toString()}"
        dsl.timeout(time: 10, unit: 'MINUTES') {
            dsl.dir(releaseInfo.repoFolder) {
                deploymentService.deployArtifact(getEnv(), releaseInfo.releaseVersion)
            }
        }
        dsl.echo "DeployToEnvStage to ${getEnv().getValue()} execute finished"
    }

    abstract Env getEnv()

    @Override
    String getName() {
        return "${getEnv().getValue().toLowerCase()} env"
    }

    @Override
    public String toString() {
        return "DeployToQAEnvStage{" +
                "env=" + getEnv().getValue() +
                ", deploymentService=" + deploymentService +
                ", skipStage=" + skipStage +
                ", exitFromPipeline=" + exitFromPipeline +
                ", releaseInfo=" + releaseInfo +
                '}';
    }
}
