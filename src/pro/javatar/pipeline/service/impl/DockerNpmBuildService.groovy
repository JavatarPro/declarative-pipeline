/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @version 2018-07-10
 */
class DockerNpmBuildService extends NpmBuildService {

    DockerService dockerService

    DockerNpmBuildService(DockerService dockerService, Npm npm) {
        this.dockerService = dockerService
        type = npm.getNpmType()
        npmVersion = npm.getNpmVersion()
    }

    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('DockerNpmBuildService buildAndUnitTests start')
        dsl.sh "pwd; ls -la"
        if (!skipUnitTests) dsl.sh 'npm run test'
        dsl.sh 'npm run build'
        dsl.sh "pwd; ls -la"
        dockerService.dockerBuildImage(releaseInfo.getDockerImageName(), releaseInfo.getDockerImageVersion())
        Logger.info('DockerNpmBuildService buildAndUnitTests end')
    }
}
