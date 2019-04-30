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
import pro.javatar.pipeline.service.UiBuildService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class CdnDeploymentService implements DeploymentService {

    String service
    UiBuildService buildService
    MavenBuildService mavenBuildService
    String cdnJobName = "common/cdn-deployment"

    CdnDeploymentService(String service, MavenBuildService mavenBuildService, UiBuildService buildService) {
        this.service = service
        this.mavenBuildService = mavenBuildService
        this.buildService = buildService
    }

    @Override
    void deployArtifact(Env environment, ReleaseInfo releaseInfo) {
        String version = releaseInfo.getReleaseVersion()
        Logger.info("CdnDeploymentService:deployArtifact to ${environment.getValue()} env " +
                "and version: ${version} started")
        // TODO amend if not available qa nexus repo or other, only in this case do no use promotion
        if (environment == Env.DEV) {
            mavenBuildService.deployFile(version, buildService.getArtifact())
        }
        String repoUrl = mavenBuildService.getMavenRepoUrl(version)
        deployUiArtifactsToCdn(service, version, repoUrl, environment.getValue())
        Logger.info("CdnDeploymentService:deployArtifact to ${environment.getValue()} env " +
                "and version: ${version} finished")
    }

    void deployUiArtifactsToCdn(String service, String version, String artifactUrl, String cdnEnv) {
        Logger.info("deployUiArtifactsToCdn started with service: ${service}, version: ${version}, " +
                "artifactUrl: ${artifactUrl}, " + "cdnEnv: ${cdnEnv}")
        dsl.build job: cdnJobName, parameters: [
                [$class: 'StringParameterValue', name: 'service', value: service],
                [$class: 'StringParameterValue', name: 'version', value: version],
                [$class: 'StringParameterValue', name: 'cdnEnv', value: cdnEnv],
                [$class: 'StringParameterValue', name: 'artifactUrl', value: artifactUrl]
        ]
        Logger.info("deployUiArtifactsToCdn finished")
    }

}
