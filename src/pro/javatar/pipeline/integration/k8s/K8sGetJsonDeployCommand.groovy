/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s


import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-04-11
 */
class K8sGetJsonDeployCommand implements Serializable {

    public static String DEFAULT_MESSAGE = "K8sGetJsonDeployCommand.K8sDeploymentNotFound"
    String deploy
    JenkinsDsl dsl
    String config = null

    K8sGetJsonDeployCommand(String deploy, JenkinsDsl dsl) {
        this.deploy = deploy
        this.dsl = dsl
    }

    boolean isDeploymentAlreadyExists() {
        if (config == null) {
            config = getK8sConfigResponse(deploy)
        }
        if (config.contains(DEFAULT_MESSAGE)) {
            return false
        }
        return true
    }

    String getConfig() {
        if (isDeploymentAlreadyExists()) {
            return config
        }
        return null
    }

    protected String getK8sConfigResponse(String deploy) {
        String cmd = "kubectl get deployment ${deploy} -o json"
        Logger.info("K8sGetJsonDeployCommand:getK8sConfigResponse:cmd: ${cmd}")
        return dsl.getShellExecutionResponse(cmd, DEFAULT_MESSAGE)
    }
}
