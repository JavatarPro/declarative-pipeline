/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-04-11
 */
class K8sCreateDeployCommand {

    public static String DEFAULT_MESSAGE = "K8sCreateDeployCommand.K8sCreateDeployCommandFailure"

    String deploy
    String image
    JenkinsDslService dsl
    String applyResponse

    K8sCreateDeployCommand(String deploy, String image, JenkinsDslService dsl) {
        this.dsl = dsl
        this.deploy = deploy
        this.image = image
    }

    def apply() {
        Logger.info("K8sCreateDeployCommand:apply: started")
        String cmd = "kubectl create deployment ${deploy} --image=${image}"
        Logger.info("K8sCreateDeployCommand:apply:cmd: ${cmd}")
        applyResponse = dsl.getShellExecutionResponse(cmd, DEFAULT_MESSAGE)
        if (isApplyCommandFailed(applyResponse)) {
            Logger.error("K8sCreateDeployCommand:apply: failed to create deployment=${deploy} with image=${image}")
            // TODO throw exception
        }
        Logger.info("K8sCreateDeployCommand:apply: completed")
    }

    boolean isApplyCommandFailed(String applyResponse) {
        if (applyResponse.contains(DEFAULT_MESSAGE)) {
            return true
        }
        return false
    }
}
