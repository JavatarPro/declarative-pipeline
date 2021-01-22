/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2018-07-10
 */
class DockerNpmBuildService extends NpmBuildService {

    DockerService dockerService

    DockerNpmBuildService(DockerService dockerService,
                          Npm npm,
                          JenkinsDslService dsl) {
        this.dockerService = dockerService
        dslService = dsl
        type = npm.getType()
        npmVersion = npm.npmVersion
    }

    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('DockerNpmBuildService buildAndUnitTests start')
        dslService.executeShell("pwd; ls -la")
        if (!skipUnitTests) dslService.executeShell("npm run test")
        dslService.executeShell("npm run build")
        dslService.executeShell("pwd; ls -la")
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        Logger.info('DockerNpmBuildService buildAndUnitTests end')
    }
}
