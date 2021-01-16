/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.marathon

import groovy.json.JsonSlurper
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService
import pro.javatar.pipeline.service.orchestration.model.DeploymentRequestBO
import pro.javatar.pipeline.service.orchestration.model.DeploymentResponseBO

/**
 * @author Borys Zora
 * @version 2020-08-02
 */
class MarathonService implements DockerOrchestrationService {

    MarathonConfig config;
    JenkinsDslService dslService;

    MarathonService(MarathonConfig config, JenkinsDslService dslService) {
        this.config = config
        this.dslService = dslService
    }

    String getCurrentServiceVersion(String env, String service) {
        String url = config.getUrl();
        String resp = dslService.getShellExecutionResponse("curl ${url}/v2/apps/${service}")
        return new JsonSlurper().parseText(resp).app.container.docker.image.split(":")[1]
    }

    @Override
    def setup() {
        return null
    }

    @Override
    DeploymentResponseBO dockerDeployContainer(DeploymentRequestBO deploymentRequest) {
        return null
    }
}
