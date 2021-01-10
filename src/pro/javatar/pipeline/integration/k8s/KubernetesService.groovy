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
        String kubectlCommand = "kubectl create deployment ${deployment} --image=${image}"
        String resp = dslService.getShellExecutionResponse(kubectlCommand)
        Logger.info("execute command: ${kubectlCommand}\nresp: ${resp}\nrequest: ${req.toString()}")
        while (! isDeploymentReady(deployment)) {
            TimeUnit.SECONDS.sleep(5);
        }
        return null
    }

    boolean isDeploymentReady(String deployment) {
        String cmd = "get deployment ${deployment} -o json"
        String resp = dslService.getShellExecutionResponse(cmd)
        def depStatus = new JsonSlurper().parseText(resp)
        return (depStatus.status.availableReplicas == 1
                && depStatus.status.replicas == 1
                && depStatus.status.updatedReplicas == 1)
    }

}
