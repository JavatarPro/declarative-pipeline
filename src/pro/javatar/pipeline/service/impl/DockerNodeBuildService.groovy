/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.domain.Npm
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-03-27
 */
class DockerNodeBuildService extends DockerNpmBuildService {

    DockerNodeBuildService(DockerService dockerService, Npm npm, JenkinsDsl dslService) {
        super(dockerService, npm, dslService)
    }

    @Override
    void setUp() {
        Logger.debug("DockerNodeBuildService setUp")
        Logger.debug("dsl.tool([name: ${npm.version}, type: ${npm.type}])")
        dslService.addToPath(npm.version, npm.type)
        dslService.executeShell("node --version")
        dslService.executeShell("npm -version")
        dslService.executeShell("docker -version")
    }

}
