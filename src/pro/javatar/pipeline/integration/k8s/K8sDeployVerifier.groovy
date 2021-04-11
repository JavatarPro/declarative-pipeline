/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import groovy.json.JsonSlurper
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.util.Logger

import java.util.concurrent.TimeUnit

/**
 * @author Borys Zora
 * @version 2021-04-11
 */
class K8sDeployVerifier {

    String deploy
    String version
    JenkinsDslService dsl
    Status rolloutStatus = Status.IN_PROGRESS
    Status deployStatus = Status.IN_PROGRESS

    K8sDeployVerifier(String deploy, String version, JenkinsDslService dsl) {
        this.deploy = deploy
        this.version = version
        this.dsl = dsl
    }

    def validate() {
        while (! isValidationCompleted(deploy)) {
            Logger.debug("KubernetesService:dockerDeployContainer: await for 5 seconds")
            TimeUnit.SECONDS.sleep(5)
        }
    }

    boolean isValidationCompleted(String deploy) {
        rolloutStatus = getDeploymentReadyByRollout(deploy)
        if (rolloutStatus == Status.IN_PROGRESS) {
            return false
        }
        return true
    }

    Status getDeploymentReadyByRollout(String deploy) {
        String cmd = "kubectl rollout status deploy/${deploy}"
        String resp = dsl.getShellExecutionResponse(cmd)
        if (resp.contains("error: deployment")) {
            return Status.FAILED
        } else if (resp.contains("successfully rolled out")) {
            return Status.SUCCEEDED
        } else {
            return Status.IN_PROGRESS
        }
    }

    // TODO use status:conditions:
    boolean isDeploymentReadyByStatus(String deploy) {
        Logger.info("KubernetesService:isDeploymentReady:deployment: ${deploy}")
        String resp = new K8sGetJsonDeployCommand(deploy, dsl).getConfig()
        Logger.trace("KubernetesService:isDeploymentReady:resp: ${resp}")
        def depStatus = new JsonSlurper().parseText(resp)
        return (depStatus.status.availableReplicas == 1
                && depStatus.status.replicas == 1
                && depStatus.status.updatedReplicas == 1)
    }

    enum Status {
        SUCCEEDED,
        FAILED,
        IN_PROGRESS
    }

}
