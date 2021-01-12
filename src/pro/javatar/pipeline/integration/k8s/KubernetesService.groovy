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

    JenkinsDslService dslService

    KubernetesService(JenkinsDslService dslService) {
        this.dslService = dslService
    }

    @Override
    def setup() {
        dslService.executeShell("kubectl get pods")
        return null
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO req) {
        String image = "${req.getImageName()}:${req.getImageVersion()}"
        String deployment = req.service
        String kubectlCommand = getDeploymentCommand(deployment, image)
        String resp = dslService.getShellExecutionResponse(kubectlCommand)
        Logger.info("KubernetesService:dockerDeployContainer: execute command: ${kubectlCommand}\nresp: ${resp}\nrequest: ${req.toString()}")
        while (! isDeploymentReady(deployment)) {
            Logger.info("KubernetesService:dockerDeployContainer: await for 5 seconds");
            TimeUnit.SECONDS.sleep(5);
        }
        return null
    }

    boolean isDeploymentAlreadyExists(String deployment) {
        String defaultMessage = "K8sDeploymentNotFound"
        String resp = dslService.getShellExecutionResponse(deployment, defaultMessage)
        if (resp.contains(defaultMessage)) {
            return false
        }
        return true
    }

    // TODO use status:conditions:
    boolean isDeploymentReady(String deployment) {
        Logger.info("KubernetesService:isDeploymentReady:deployment: ${deployment}")
        String cmd = "kubectl get deployment ${deployment} -o json"
        String resp = dslService.getShellExecutionResponse(cmd)
        Logger.debug("KubernetesService:isDeploymentReady:resp: ${resp}")
        def depStatus = new JsonSlurper().parseText(resp)
        return (depStatus.status.availableReplicas == 1
                && depStatus.status.replicas == 1
                && depStatus.status.updatedReplicas == 1)
    }

    String getDeploymentCommand(String deployment, String image) {
        if (isDeploymentAlreadyExists(deployment)) {
            return "kubectl create deployment ${deployment} --image=${image}"
        }
        //  kubectl set image deployments,rc nginx=nginx:1.9.1 --all
        // kubectl set image deployment/nginx-deployment nginx=nginx:1.16.1 --record
        return "kubectl set image deployments ${deployment}=${image} --all"
    }
}
