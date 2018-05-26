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

package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.model.Env

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.Utils.isBlank
import static pro.javatar.pipeline.util.Utils.isNotBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerService implements Serializable {

    static final LATEST_LABEL = "latest"

    String devRepo
    String prodRepo
    String dockerCredentialsId
    DockerOrchestrationService orchestrationService

    DockerService(String devRepo, String prodRepo, DockerOrchestrationService orchestrationService) {
        this.devRepo = devRepo
        this.prodRepo = prodRepo
        this.orchestrationService = orchestrationService
    }

    def dockerBuildImage(String imageName, String imageVersion) {
        dsl.echo "dockerBuildImage for imageName: ${imageName} with imageVersion: ${imageVersion} started"
        dsl.sh 'docker -v'
        dsl.sh "docker build -t ${imageName}:${imageVersion} ."
        dsl.echo "dockerBuildImage for service: ${imageName} with releaseVersion: ${imageVersion} finished"
    }

    def dockerPublish(String imageName, String imageVersion, Env env) {
        if (env == Env.DEV) {
            dockerPushImageToRegistry(imageName, imageVersion, devRepo)
        }
        if (env == Env.QA) {
            if (devRepo.equalsIgnoreCase(prodRepo)) {
                dsl.echo "prodRepo: ${prodRepo} same as ${devRepo}, dockerPushImageToRegistry will be skipped"
                return
            }
            dockerPushImageToRegistry(imageName, imageVersion, prodRepo)
        }
    }

    def dockerPushImageToProdRegistry(String imageName, String imageVersion) {
        dockerPushImageToRegistry(imageName, imageVersion, prodRepo)
    }

    def dockerLoginAndPushImageToRegistry(String imageName, String imageVersion, String dockerRepositoryUrl) {
        dsl.echo "withDockerRegistry([credentialsId: ${dockerCredentialsId}, url: 'http://${dockerRepositoryUrl}'])"
        dsl.withDockerRegistry([credentialsId: dockerCredentialsId, url: 'http://${dockerRepositoryUrl}']) {
            dsl.sh "docker images"
            dsl.docker.image("${dockerRepositoryUrl}/${imageName}:${imageVersion}").push()
            dsl.docker.image("${dockerRepositoryUrl}/${imageName}:${LATEST_LABEL}").push()
        }
    }

    def dockerPushImageToRegistryWithoutLogin(String imageName, String imageVersion, String dockerRepositoryUrl) {
        dsl.sh "docker images"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRepositoryUrl}/${imageName}:${imageVersion}"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRepositoryUrl}/${imageName}:${LATEST_LABEL}"
        dsl.sh "docker push ${dockerRepositoryUrl}/${imageName}:${imageVersion}"
        dsl.sh "docker push ${dockerRepositoryUrl}/${imageName}:${LATEST_LABEL}"
    }

    def dockerPushImageToRegistry(String imageName, String imageVersion, String dockerRepositoryUrl) {
        dockerLogin(dockerRepositoryUrl)
        dockerPushImageToRegistryWithoutLogin(imageName, imageVersion, dockerRepositoryUrl)
//        if (isNotBlank(dockerCredentialsId)) {
//            dockerLoginAndPushImageToRegistry(imageName, imageVersion, dockerRepositoryUrl)
//        } else {
//            dockerPushImageToRegistryWithoutLogin(imageName, imageVersion, dockerRepositoryUrl)
//        }
    }

    def dockerLogin(String dockerRepositoryUrl) {
        if (isBlank(dockerCredentialsId)) {
            dsl.echo "WARN: dockerCredentialsId is blank (${dockerCredentialsId}), skip login"
            return
        }
        dsl.withCredentials([[$class: 'UsernamePasswordMultiBinding',
                              credentialsId: dockerCredentialsId,
                              usernameVariable: 'DOCKER_REGISTRY_USERNAME',
                              passwordVariable: 'DOCKER_REGISTRY_PASSWORD']]) {
            dsl.sh("echo ${dsl.env.DOCKER_REGISTRY_PASSWORD} | docker login ${dockerRepositoryUrl} -u ${dsl.env.DOCKER_REGISTRY_USERNAME} --password-stdin")
        }
    }

    def dockerDeployContainer(String imageName, String imageVersion, Env env) {
        dsl.echo "dockerDeployContainer(${imageName}, ${imageVersion}, ${env.getValue()})"
        if (env == Env.DEV) {
            orchestrationService.dockerDeployContainer(imageName, imageVersion, devRepo, env.getValue())
        } else {
            orchestrationService.dockerDeployContainer(imageName, imageVersion, prodRepo, env.getValue())
        }
    }

    void setDockerCredentialsId(String dockerCredentialsId) {
        this.dockerCredentialsId = dockerCredentialsId
    }

    @Override
    public String toString() {
        return "DockerService{" +
                "devRepo='" + devRepo + '\'' +
                ", prodRepo='" + prodRepo + '\'' +
                '}';
    }
}
