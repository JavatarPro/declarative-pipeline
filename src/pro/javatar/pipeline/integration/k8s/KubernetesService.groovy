/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import groovy.json.JsonSlurper
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO
import pro.javatar.pipeline.util.Logger
import java.util.concurrent.TimeUnit

/**
 * @author Borys Zora
 * @version 2020-08-02
 */
class KubernetesService implements DockerOrchestrationService {

    JenkinsDslService dsl

    KubernetesService(JenkinsDslService dsl) {
        this.dsl = dsl
    }

    @Override
    def setup() {
        dsl.executeShell("kubectl get pods")
        return null
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO req) {
        Logger.info("KubernetesService:dockerDeployContainer: started")
        String deploy = req.service
        String image = req.getImage()
        String version = req.getImageVersion()
        Logger.debug("KubernetesService:dockerDeployContainer: deploy:${deploy}, image:${image}, version:${version}")

        def oldDeploy = new K8sGetJsonDeployCommand(deploy, dsl)
        if (oldDeploy.isDeploymentAlreadyExists()) {
            incrementVersion(version, oldDeploy.getConfig(), deploy)
        } else {
            createDeployment(deploy, image)
        }

        validateDeployment(deploy, version)
        Logger.info("KubernetesService:dockerDeployContainer: completed")
        return null
    }

    def incrementVersion(String version, String config, String deploy) {
        Logger.info("KubernetesService:incrementVersion: version:${version}, deploy:${deploy}")
        Logger.debug("KubernetesService:incrementVersion: config:${config}")
        String newDeploy = new K8sJsonSetupVersion(config, deploy)
                .setupVersion(version)
        new K8sDeployApplyCommand(newDeploy, dsl).apply()
    }

    void createDeployment(String deploy, String image) {
        Logger.info("KubernetesService:createDeployment: deploy:${deploy}, image:${image}")
        new K8sCreateDeployCommand(deploy, image, dsl).apply()
    }

    def validateDeployment(String deploy, String version) {
        Logger.info("KubernetesService:validateDeployment: deploy:${deploy}, image:${version}")
        new K8sDeployVerifier(deploy, version, dsl).validate()
    }

}
