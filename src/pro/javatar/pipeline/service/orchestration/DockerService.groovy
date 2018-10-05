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
import pro.javatar.pipeline.model.ReleaseInfo

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.Utils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerService implements Serializable {

    static final String LATEST_LABEL = "latest"
    static final String DEFAULT_DOCKER_FILE = "Dockerfile"

    String devRepo
    String prodRepo
    String dockerDevCredentialsId
    String dockerProdCredentialsId
    DockerOrchestrationService orchestrationService
    String customDockerFileName

    DockerService(String devRepo, String prodRepo, DockerOrchestrationService orchestrationService) {
        this.devRepo = devRepo
        this.prodRepo = prodRepo
        this.orchestrationService = orchestrationService
    }

    def dockerBuildImage(ReleaseInfo releaseInfo) {
        if (releaseInfo.isMultiDockerBuild()) {
            releaseInfo.dockerImageNames.each {
                String image -> dockerBuildImage(image, releaseInfo.getDockerImageVersion(),
                        releaseInfo.getCustomDockerFileName(image))
            }
            return
        }
        dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
    }

    def dockerBuildImage(String imageName, String imageVersion, String customDockerFileName) {
        dsl.echo "dockerBuildImage for imageName: ${imageName} with imageVersion: ${imageVersion} started"
        dsl.sh 'docker -v'
        dsl.sh "docker build -t ${imageName}:${imageVersion} -f ${customDockerFileName}"
        dsl.echo "dockerBuildImage for service: ${imageName} with releaseVersion: ${imageVersion} finished"
    }

    def dockerBuildImage(String imageName, String imageVersion) {
        dsl.echo "dockerBuildImage for imageName: ${imageName} with imageVersion: ${imageVersion} started"
        dsl.sh 'docker -v'
        dsl.sh "docker build -t ${imageName}:${imageVersion} ${getCustomDockerFileInstruction()} ."
        dsl.echo "dockerBuildImage for service: ${imageName} with releaseVersion: ${imageVersion} finished"
    }

    def dockerPublish(String imageName, String imageVersion, Env env) {
        if (env == Env.DEV) {
            dockerPushImageToRegistry(imageName, imageVersion, devRepo, dockerDevCredentialsId)
            dockerPushImageWithBuildNumberToRegistryWithoutLogin(imageName, imageVersion, devRepo)
        }
        if (env == Env.QA) {
            if (devRepo.equalsIgnoreCase(prodRepo)) {
                dsl.echo "prodRepo: ${prodRepo} same as ${devRepo}, dockerPushImageToRegistry will be skipped"
                return
            }
            dockerPushImageToRegistry(imageName, imageVersion, prodRepo, dockerProdCredentialsId)
        }
    }

    def dockerPushImageToProdRegistry(String imageName, String imageVersion) {
        dockerPushImageToRegistry(imageName, imageVersion, prodRepo, dockerProdCredentialsId)
    }

    def dockerLoginAndPushImageToRegistry(String imageName, String imageVersion,
                                          String dockerRepositoryUrl, String credentialsId) {
        dsl.echo "withDockerRegistry([credentialsId: ${credentialsId}, url: 'http://${dockerRepositoryUrl}'])"
        dsl.withDockerRegistry([credentialsId: credentialsId, url: 'http://${dockerRepositoryUrl}']) {
            dsl.sh "docker images"
            dsl.docker.image("${dockerRepositoryUrl}/${imageName}:${imageVersion}").push()
        }
    }

    def dockerPushImageToRegistryWithoutLogin(String imageName, String imageVersion, String dockerRepositoryUrl) {
        dsl.sh "docker images"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRepositoryUrl}/${imageName}:${imageVersion}"
        dsl.sh "docker push ${dockerRepositoryUrl}/${imageName}:${imageVersion}"
    }

    def dockerPushImageWithBuildNumberToRegistryWithoutLogin(String imageName, String imageVersion,
                                                             String dockerRepositoryUrl) {
        dsl.sh "docker images"
        String versionWithBuildNumber = getImageVersionWithBuildNumber(imageVersion)
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRepositoryUrl}/${imageName}:${versionWithBuildNumber}"
        dsl.sh "docker push ${dockerRepositoryUrl}/${imageName}:${versionWithBuildNumber}"
    }

    def dockerPushLatestImageToRegistryWithoutLogin(String imageName, String imageVersion, String dockerRepositoryUrl) {
        dsl.sh "docker images"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRepositoryUrl}/${imageName}:${LATEST_LABEL}"
        dsl.sh "docker push ${dockerRepositoryUrl}/${imageName}:${LATEST_LABEL}"
    }

    def dockerPushImageToRegistry(String imageName, String imageVersion,
                                  String dockerRepositoryUrl, String credentialsId) {
        dockerLogin(dockerRepositoryUrl, credentialsId)
        dockerPushImageToRegistryWithoutLogin(imageName, imageVersion, dockerRepositoryUrl)
    }

    def dockerLogin(String dockerRepositoryUrl, String credentialsId) {
        if (isBlank(credentialsId)) {
            dsl.echo "WARN: credentialsId is blank (${credentialsId}), skip login"
            return
        }
        dsl.withCredentials([[$class: 'UsernamePasswordMultiBinding',
                              credentialsId: credentialsId,
                              usernameVariable: 'DOCKER_REGISTRY_USERNAME',
                              passwordVariable: 'DOCKER_REGISTRY_PASSWORD']]) {
            dsl.sh("echo ${dsl.env.DOCKER_REGISTRY_PASSWORD} | docker login ${dockerRepositoryUrl} -u ${dsl.env.DOCKER_REGISTRY_USERNAME} --password-stdin")
        }
    }

    def dockerDeployContainer(String imageName, String imageVersion, Env env) {
        dsl.echo "dockerDeployContainer(${imageName}, ${imageVersion}, ${env.getValue()})"
        if (env == Env.DEV) {
            orchestrationService.setup()
            String versionWithBuildNumber = getImageVersionWithBuildNumber(imageVersion)
            orchestrationService.dockerDeployContainer(imageName, versionWithBuildNumber, devRepo, env.getValue())
        } else {
            orchestrationService.dockerDeployContainer(imageName, imageVersion, prodRepo, env.getValue())
        }
    }

    void setDockerDevCredentialsId(String dockerCredentialsId) {
        this.dockerDevCredentialsId = dockerCredentialsId
    }

    void setDockerProdCredentialsId(String dockerProdCredentialsId) {
        this.dockerProdCredentialsId = dockerProdCredentialsId
    }

    String getCustomDockerFileInstruction() {
        if (isBlank(customDockerFileName)) {
            return ""
        }
        return "-f ${customDockerFileName}"
    }

    void setDockerCredentialsId(String dockerCredentialsId) {
        this.dockerCredentialsId = dockerCredentialsId
    }

    void setCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
    }

    String getImageVersionWithBuildNumber(String imageVersion) {
        return "${imageVersion}.${dsl.currentBuild.number}"
    }

    // TODO simplify
    def populateReleaseInfo(ReleaseInfo releaseInfo) {
        String output = dsl.sh returnStdout: true, script: 'ls -d */*'
        List<String> dockerFiles = output.split().findAll {it -> it.contains(DEFAULT_DOCKER_FILE)}
        if (dockerFiles == null) {
            dsl.echo "WARN: no Dockerfile has been found"
            return
        }
        String theDockerFile = null
        List<String> multipleDockerFiles = new ArrayList<>()
        for(String dockerfile: dockerFiles) {
            if (DEFAULT_DOCKER_FILE.equals(dockerfile)) {
                theDockerFile = dockerfile
            } else if (dockerfile != null || dockerfile.contains("/" + DEFAULT_DOCKER_FILE)) {
                multipleDockerFiles.add(dockerfile.trim())
            }
        }
        if (theDockerFile != null) {
            dsl.echo "INFO: main docker file exists no need populate release info"
            if (!multipleDockerFiles.isEmpty()) {
                dsl.echo "WARN: will be ignored next docker files: ${multipleDockerFiles}"
            }
            return
        }
        if (multipleDockerFiles.isEmpty()) {
            dsl.echo "ERROR: no docker file provided"
            return
        }
        for (String dockerfile: multipleDockerFiles) {
            String[] array = dockerfile.split("/")
            if (array.size() == 2) {
                String dockerImageName = array[0]
                releaseInfo.addDockerImageName(dockerImageName)
                releaseInfo.addCustomDockerFileName(dockerImageName, dockerfile)
            } else {
                dsl.echo "WARN: dockerfile: ${dockerfile} will be ignored"
            }
        }
    }

    @Override
    public String toString() {
        return "DockerService{" +
                "devRepo='" + devRepo + '\'' +
                ", prodRepo='" + prodRepo + '\'' +
                '}';
    }
}
