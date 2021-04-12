/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-04-11
 */
class K8sDeployApplyCommand implements Serializable {

    public static String DEFAULT_MESSAGE = "K8sDeployApplyCommand.K8sApplyCommandFailure"

    private static String APPLY_FILE = "K8S-apply-deployment-file.json"

    String config
    JenkinsDslService dsl
    String applyResponse

    K8sDeployApplyCommand(String config, JenkinsDslService dsl) {
        this.config = config
        this.dsl = dsl
    }

    def apply() {
        Logger.info("K8sDeployApplyCommand:apply started")
        // File tmpConfigFile = createJsonConfigFile()
        // String cmd = "kubectl apply -f " + tmpConfigFile.getAbsolutePath()
        dsl.writeFile(APPLY_FILE, config)
        dsl.executeShell("pwd && ls -lh && cat ${APPLY_FILE}")
        String cmd = "kubectl apply -f " + APPLY_FILE
        applyResponse = dsl.getShellExecutionResponse(cmd, DEFAULT_MESSAGE)
        Logger.debug("K8sDeployApplyCommand:apply:applyResponse:${applyResponse}")
        // deleteJsonConfigFile(tmpConfigFile)
        dsl.executeShell("rm ${APPLY_FILE}")
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

    @NonCPS
    File createJsonConfigFile() {
        String currentDirectory = dsl.getShellExecutionResponse("pwd").trim()
        File tmpConfigFile = new File(currentDirectory, APPLY_FILE)
        Logger.info("K8sDeployApplyCommand:apply:tmpConfigFile:${tmpConfigFile.getAbsolutePath()}")
        tmpConfigFile.write(config)
        Logger.debug("K8sDeployApplyCommand:apply:config:${config}")
        return tmpConfigFile
    }

    @NonCPS
    void deleteJsonConfigFile(File tmpConfigFile) {
        tmpConfigFile.delete()
        Logger.debug("K8sDeployApplyCommand:apply:tmpConfigFile:deleted")
    }

}
