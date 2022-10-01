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
package pro.javatar.pipeline.service.orchestration

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.domain.Docker
import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.ContextHolder
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DockerRegistryBO
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl
import static pro.javatar.pipeline.util.StringUtils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class DockerService implements Serializable {

    static final String LATEST_LABEL = "latest"

    static final String DEFAULT_DOCKER_FILE = "Dockerfile"

    Map<String, Docker> dockerRegistries = new HashMap<>()

    DockerOrchestrationService orchestrationService

    String customDockerFileName

    DockerService(Map<String, DockerRegistryBO> map,
                  DockerOrchestrationService orchestration) {
        map.each { k, v ->
            Docker docker = new Docker()
            docker.name = k
            docker.url = v.registry
            docker.cred = v.credentialsId
            this.dockerRegistries.put(docker.name, docker)
        }
        this.orchestrationService = orchestration
    }

    DockerService(List<Docker> dockers) {
        this.orchestrationService = ContextHolder.get(DockerOrchestrationService.class)
        dockers.each { d -> dockerRegistries.put(d.name, d) }
    }

    def dockerBuildImage(ReleaseInfo releaseInfo) {
        if (releaseInfo.isMultiDockerBuild()) {
            releaseInfo.dockerImageNames.each {
                String image ->
                    dockerBuildImage(image, releaseInfo.getDockerImageVersion(),
                            releaseInfo.getCustomDockerFileName(image))
            }
            return
        }
        if (releaseInfo.getIsUi() && releaseInfo.getOptimizeDockerContext()) {
            dockerBuildImageWithContextOptimizationForUI(releaseInfo)
        } else {
            dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        }
    }

    def dockerBuildImage(String imageName, String imageVersion, String customDockerFileName) {
        Logger.debug("dockerBuildImage for imageName: ${imageName} with imageVersion: ${imageVersion} started")
        dsl.sh 'docker -v'
        dsl.sh "docker build -t ${imageName}:${imageVersion} -f ${customDockerFileName} ."
        Logger.debug("dockerBuildImage for service: ${imageName} with releaseVersion: ${imageVersion} finished")
    }

    def dockerBuildImage(String imageName, String imageVersion) {
        Logger.debug("dockerBuildImage for imageName: ${imageName} with imageVersion: ${imageVersion} started")
        dsl.sh 'docker -v'
        dsl.sh "docker build -t ${imageName}:${imageVersion} ${getCustomDockerFileInstruction()} ."
        Logger.debug("dockerBuildImage for service: ${imageName} with releaseVersion: ${imageVersion} finished")
    }

    def dockerBuildImageWithContextOptimizationForUI(ReleaseInfo releaseInfo) {
        Logger.debug("dockerBuildImageWithContextOptimizationForUI started")
        dsl.sh "mv ${releaseInfo.getUiDistributionFolder()} ${releaseInfo.getBuildDockerFromFolder()}"
        dsl.dir(releaseInfo.getBuildDockerFromFolder()) {
            dsl.sh "pwd && ls -la && ls -la .."
            dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        }
        Logger.debug("dockerBuildImageWithContextOptimizationForUI finished")
    }

    def dockerPublish(String imageName, String imageVersion, Env env) {
        Logger.info("DockerService:dockerPublish: ${imageName}:${imageVersion} to env: ${env}")
        Docker dockerRegistry = dockerRegistries.get(env.getValue())
        Logger.info("DockerService:dockerPublish:dockerRegistry: ${dockerRegistry}")
        String registry = dockerRegistry.url
        String credentials = dockerRegistry.cred
        if (DockerHolder.isImageAlreadyPublished(imageName, imageVersion, registry)) {
            Logger.info("image: ${imageName}:${imageVersion} has already been published into registry: ${registry}, " +
                    "will skip publishing")
            return
        }
        dockerPushImageToRegistry(imageName, imageVersion, registry, credentials)
        if (env == Env.DEV) { // TODO docker push could be done in build & unit test stage
            dockerPushImageWithBuildNumberToRegistryWithoutLogin(imageName, imageVersion, registry)
        }
        Logger.info("DockerService:dockerPublish: completed")
    }

    def dockerPushImageToProdRegistry(String imageName, String imageVersion) {
        dockerPublish(imageName, imageVersion, Env.PROD)
    }

    def dockerLoginAndPushImageToRegistry(String imageName, String imageVersion,
                                          String dockerRegistryUrl, String credentialsId) {
        Logger.debug("withDockerRegistry([credentialsId: ${credentialsId}, url: 'http://${dockerRegistryUrl}'])")
        dsl.withDockerRegistry([credentialsId: credentialsId, url: 'http://${dockerRegistryUrl}']) {
            dsl.sh "docker images"
            dsl.docker.image("${dockerRegistryUrl}/${imageName}:${imageVersion}").push()
        }
    }

    def dockerPushImageToRegistryWithoutLogin(String imageName, String imageVersion, String dockerRegistryUrl) {
        Logger.info("DockerService:dockerPushImageToRegistryWithoutLogin (imageName: ${imageName}, " +
                "imageVersion: ${imageVersion}, dockerRegistryUrl: ${dockerRegistryUrl})")
        dsl.sh "docker images"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRegistryUrl}/${imageName}:${imageVersion}"
        dsl.sh "docker push ${dockerRegistryUrl}/${imageName}:${imageVersion}"
        DockerHolder.addToAlreadyPublished(imageName, imageVersion, dockerRegistryUrl)
        Logger.info("DockerService:dockerPushImageToRegistryWithoutLogin completed")
    }

    def dockerPushImageWithBuildNumberToRegistryWithoutLogin(String imageName, String imageVersion,
                                                             String dockerRegistryUrl) {
        Logger.info("DockerService:dockerPushImageWithBuildNumberToRegistryWithoutLogin (${imageName}, " +
                "${imageVersion}, ${dockerRegistryUrl})")
        dsl.sh "docker images"
        String versionWithBuildNumber = getImageVersionWithBuildNumber(imageVersion)
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRegistryUrl}/${imageName}:b${versionWithBuildNumber}"
        dsl.sh "docker push ${dockerRegistryUrl}/${imageName}:b${versionWithBuildNumber}"
        DockerHolder.addToAlreadyPublished(imageName, versionWithBuildNumber, dockerRegistryUrl)
        Logger.info("DockerService:dockerPushImageWithBuildNumberToRegistryWithoutLogin completed")
    }

    def dockerPushLatestImageToRegistryWithoutLogin(String imageName, String imageVersion, String dockerRegistryUrl) {
        Logger.info("DockerService:dockerPushLatestImageToRegistryWithoutLogin (${imageName}, ${imageVersion}, " +
                "${dockerRegistryUrl})")
        dsl.sh "docker images"
        dsl.sh "docker tag ${imageName}:${imageVersion} ${dockerRegistryUrl}/${imageName}:${LATEST_LABEL}"
        dsl.sh "docker push ${dockerRegistryUrl}/${imageName}:${LATEST_LABEL}"
        DockerHolder.addToAlreadyPublished(imageName, LATEST_LABEL, dockerRegistryUrl)
    }

    def dockerPushImageToRegistry(String imageName, String imageVersion,
                                  String dockerRegistryUrl, String credentialsId) {
        Logger.debug("DockerService:dockerPushImageToRegistry: imageName: ${imageName}, imageVersion: ${imageVersion}"
                + ", dockerRegistryUrl: ${dockerRegistryUrl}, credentialsId: ${credentialsId}")
        dockerLogin(dockerRegistryUrl, credentialsId)
        dockerPushImageToRegistryWithoutLogin(imageName, imageVersion, dockerRegistryUrl)
    }

    // TODO security issue after --password-stdin stopped working
    def dockerLogin(String dockerRegistryUrl, String credentialsId) {
        Logger.debug("DockerService:dockerLogin: dockerRegistryUrl: ${dockerRegistryUrl}, " +
                "credentialsId: ${credentialsId}")
        if (isBlank(credentialsId)) {
            Logger.warn("DockerService:dockerLogin: credentialsId is blank (${credentialsId}), skip login")
            return
        }
        // TODO known issues if user not added to docker group or added but jenkins node not restarted
        dsl.sh "whoami"
        dsl.withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                              credentialsId   : credentialsId,
                              usernameVariable: 'DOCKER_REGISTRY_USERNAME',
                              passwordVariable: 'DOCKER_REGISTRY_PASSWORD']]) {
            dsl.sh("docker login ${dockerRegistryUrl} -u ${dsl.env.DOCKER_REGISTRY_USERNAME} -p${dsl.env.DOCKER_REGISTRY_PASSWORD}")
        }
    }

    // TODO user object instead of many args
    // TODO move to deployment service
    def dockerDeployContainer(String imageName, String imageVersion, Env env) {
        Logger.debug("DockerService:dockerDeployContainer(${imageName}, ${imageVersion}, ${env.getValue()})")
        def dockerRegistry = dockerRegistries.get(env.getValue())
        def request = new DeploymentRequestBO()
                .withImageName(imageName)
                .withImageVersion(imageVersion)
                .withDockerRegistry(dockerRegistry)
                .withEnvironment(env)
                .withService(imageName)
                .withBuildNumber(dsl.currentBuild.number)
        // TODO remove low level details from this level of abstraction
        Logger.debug("DockerService:dockerDeployContainer: orchestrationService.setup")
        orchestrationService.setup()
        Logger.debug("DockerService:dockerDeployContainer: orchestrationService.dockerDeployContainer")
        orchestrationService.dockerDeployContainer(request)
        Logger.debug("DockerService:dockerDeployContainer:finish")
    }

    String getCustomDockerFileInstruction() {
        if (isBlank(customDockerFileName)) {
            return ""
        }
        return "-f ${customDockerFileName}"
    }

    void setCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
    }

    @Deprecated
    String getImageVersionWithBuildNumber(String imageVersion) {
        return "${imageVersion}.${dsl.currentBuild.number}"
    }

    // TODO simplify
    def populateReleaseInfo(ReleaseInfo releaseInfo) {
        Logger.debug("DockerService:populateReleaseInfo:started with releaseInfo: " + releaseInfo.toString());
        String output = dsl.sh returnStdout: true, script: 'ls -d */*'
        List<String> dockerFiles = output.split().findAll { it -> it.contains(DEFAULT_DOCKER_FILE) }
        if (dockerFiles == null) {
            Logger.warn("DockerService:populateReleaseInfo: No Dockerfile has been found")
            return
        }
        String theDockerFile = null
        List<String> multipleDockerFiles = new ArrayList<>()
        for (String dockerfile : dockerFiles) {
            if (DEFAULT_DOCKER_FILE.equals(dockerfile)) {
                theDockerFile = dockerfile
            } else if (dockerfile != null || dockerfile.contains("/" + DEFAULT_DOCKER_FILE)) {
                multipleDockerFiles.add(dockerfile.trim())
            }
        }
        if (theDockerFile != null) {
            Logger.info("DockerService:populateReleaseInfo: main docker file exists no need populate release info")
            if (!multipleDockerFiles.isEmpty()) {
                Logger.warn("DockerService:populateReleaseInfo: will be ignored next docker files: ${multipleDockerFiles}")
            }
            return
        }
        if (multipleDockerFiles.isEmpty()) {
            Logger.error("DockerService:populateReleaseInfo: No docker file provided")
            return
        }
        for (String dockerfile : multipleDockerFiles) {
            String[] array = dockerfile.split("/")
            if (array.size() == 2) {
                String dockerImageName = array[0]
                releaseInfo.addDockerImageName(dockerImageName)
                releaseInfo.addCustomDockerFileName(dockerImageName, dockerfile)
            } else {
                Logger.warn("DockerService:populateReleaseInfo: dockerfile: ${dockerfile} will be ignored")
            }
        }
        Logger.debug("DockerService:populateReleaseInfo:finished")
    }

    @NonCPS
    @Override
    public String toString() {
        return "DockerService{" +
                "dockerRegistries=" + dockerRegistries +
                ", orchestrationService=" + orchestrationService +
                ", customDockerFileName='" + customDockerFileName + '\'' +
                '}';
    }
}
