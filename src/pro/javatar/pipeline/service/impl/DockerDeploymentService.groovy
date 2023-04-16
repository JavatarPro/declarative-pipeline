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
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.DeploymentService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerDeploymentService implements DeploymentService {

    ReleaseInfo releaseInfo
    DockerService dockerService

    DockerDeploymentService(ReleaseInfo releaseInfo,
                            DockerService dockerService) {
        this.releaseInfo = releaseInfo
        this.dockerService = dockerService
    }

    @Override
    void deployArtifact(Env environment, ReleaseInfo releaseInfo) {
        Logger.info("DockerDeploymentService: deployArtifact started")
        if (releaseInfo.isMultiDockerBuild()) {
            deployMultipleArtifacts(environment, releaseInfo)
        } else {
            deploySingleArtifact(environment, releaseInfo)
        }
        Logger.info("DockerDeploymentService: deployArtifact finished")
    }

    void deploySingleArtifact(Env environment, ReleaseInfo releaseInfo) {
        String version = releaseInfo.getDockerImageVersion()
        if (environment == Env.DEV) {
            // TODO improve lost prefix in this case
            version = releaseInfo.releaseVersionWithBuildSuffix()
            Logger.debug("customizing version for dev env: ${version}")
            // TODO move & split this method to do it on build and release stage
            dockerService.dockerPublish(releaseInfo.getDockerImageName(), releaseInfo.releaseVersion(), environment)
        }
        Logger.debug("DockerDeploymentService deploySingleArtifact to ${environment.getValue()} " +
                "env and version: ${version} started")
        dockerService.dockerDeployContainer(releaseInfo.getDockerImageName(), version, environment)
        Logger.debug("DockerDeploymentService deploySingleArtifact to ${environment.getValue()} " +
                "env and version: ${version} finished")
    }

    void deploySingleArtifactForMultiDocker(Env environment, String dockerImageName, ReleaseInfo releaseInfo) {
        String version = releaseInfo.releaseVersion()
        Logger.debug("DockerDeploymentService deploySingleArtifactForMultiDocker to ${environment.getValue()}, " +
                "dockerImageName: ${dockerImageName}, env and version: ${version} started")
        // TODO move & split this method to do it on build and release stage
        if (environment == Env.DEV) {
            dockerService.dockerPublish(dockerImageName, releaseInfo.getDockerImageVersion(), environment)
        }
        dockerService.dockerDeployContainer(dockerImageName, releaseInfo.getDockerImageVersion(), environment)
        Logger.debug("DockerDeploymentService deploySingleArtifactForMultiDocker to ${environment.getValue()}, " +
                "dockerImageName: ${dockerImageName}, env and version: ${version} finished")
    }

    // deployment with prefix not yet supported
    void deployMultipleArtifacts(Env environment, ReleaseInfo releaseInfo) {
        Logger.debug("DockerDeploymentService deployMultipleArtifacts with environment: ${environment} " +
                "& releaseInfo: ${releaseInfo} started")
        def stepsForParallel = [:]
        releaseInfo.getCustomDockerFileNames().each {
            key, value -> stepsForParallel[key] = {
                deploySingleArtifactForMultiDocker(environment, key, releaseInfo)
            }
        }
        dsl.parallel stepsForParallel
        Logger.debug("DockerDeploymentService deployMultipleArtifacts with environment: ${environment} " +
                "& releaseInfo: ${releaseInfo} finished")
    }

}