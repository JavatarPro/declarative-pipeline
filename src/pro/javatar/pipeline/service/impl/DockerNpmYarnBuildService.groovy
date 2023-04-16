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
class DockerNpmYarnBuildService extends DockerNpmBuildService {

    DockerNpmYarnBuildService(DockerService dockerService, Npm npm, JenkinsDsl dslService) {
        super(dockerService, dslService)
    }

    @Override
    void setUp() {
        Logger.debug("NpmBuildService setUp")
        Logger.debug("dsl.tool([name: ${npm.version}, type: ${npm.type}])")
        def node = dslService.addToPath(npm.version, npm.type)
        dslService.executeShell("node --version")
        dslService.executeShell("npm -version")
        dslService.executeShell("yarn -version")
        dslService.executeShell("yarn install")
    }

}
