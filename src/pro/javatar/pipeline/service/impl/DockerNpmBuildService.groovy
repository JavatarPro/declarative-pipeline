/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2018-07-10
 */
@Deprecated
class DockerNpmBuildService extends NpmBuildService {

    DockerService dockerService

    DockerNpmBuildService(DockerService dockerService, JenkinsDsl dsl) {
        this.dockerService = dockerService
        dslService = dsl
    }

    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('DockerNpmBuildService buildAndUnitTests start')
        dslService.executeShell("pwd; ls -la")
        if (!skipUnitTests) dslService.executeShell("npm run test")
        dslService.executeShell("npm run build")
        dslService.executeShell("pwd; ls -la")
        dockerService.dockerBuildImage(releaseInfo)
        Logger.info('DockerNpmBuildService buildAndUnitTests end')
    }
}
