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

package pro.javatar.pipeline.stage.deploy

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.DeploymentService
import pro.javatar.pipeline.stage.Stage
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class DeployToEnvStage extends Stage {

    protected DeploymentService deploymentService // = ServiceContextHolder.getService(DeploymentService.class)

    @Override
    void execute() throws PipelineException {
        Logger.info("DeployToEnvStage to " + getEnv().getValue() + " execute started: " + toString())
        // TODO replace hardcode with configuration
        dsl.timeout(time: 10, unit: 'MINUTES') {
            dsl.dir(releaseInfo().getRepoFolder()) {
                deploymentService.deployArtifact(getEnv(), releaseInfo())
            }
        }
        Logger.info("DeployToEnvStage to ${getEnv().getValue()} execute finished")
    }

    abstract Env getEnv()

    @Override
    String getName() {
        return "${getEnv().getValue().toLowerCase()} env"
    }

    DeployToEnvStage withDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "DeployToQAEnvStage{" +
                "env=" + getEnv().getValue() +
                ", deploymentService=" + deploymentService +
                ", skipStage=" + skipStage +
                ", exitFromPipeline=" + exitFromPipeline +
                ", releaseInfo=" + releaseInfo() +
                '}';
    }
}
