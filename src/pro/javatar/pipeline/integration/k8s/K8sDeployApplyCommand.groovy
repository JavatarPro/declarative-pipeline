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
class K8sDeployApplyCommand implements Serializable {

    public static String DEFAULT_MESSAGE = "K8sDeployApplyCommand.K8sApplyCommandFailure"

    private static String APPLY_FILE = "K8S-apply-deployment-file.json"
    private static String APPLY_COMMAND = "kubectl apply -f " + APPLY_FILE

    String config
    JenkinsDslService dsl
    String applyResponse

    K8sDeployApplyCommand(String config, JenkinsDslService dsl) {
        this.config = config
        this.dsl = dsl
    }

    def apply() {
        Logger.info("K8sDeployApplyCommand:apply started")
        File tmpConfigFile = new File(APPLY_FILE)
        Logger.info("K8sDeployApplyCommand:apply:tmpConfigFile:${tmpConfigFile.getAbsolutePath()}")
        tmpConfigFile.write(config)
        Logger.debug("K8sDeployApplyCommand:apply:config:${config}")
        applyResponse = dsl.getShellExecutionResponse(APPLY_COMMAND, DEFAULT_MESSAGE)
        Logger.debug("K8sDeployApplyCommand:apply:applyResponse:${applyResponse}")
        tmpConfigFile.delete()
        Logger.debug("K8sDeployApplyCommand:apply:tmpConfigFile:deleted")
        if (isApplyCommandFailed(applyResponse)) {
            Logger.error("K8sDeployApplyCommand:apply: failed to apply config: ${config}")
            // TODO throw exception
        }
        Logger.info("K8sDeployApplyCommand:apply completed")
    }

    boolean isApplyCommandFailed(String applyResponse) {
        if (applyResponse.contains(DEFAULT_MESSAGE)) {
            return true
        }
        return false
    }
}
