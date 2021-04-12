/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.docker

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.release.CurrentVersionAware
import pro.javatar.pipeline.release.SetupVersionAware
import pro.javatar.pipeline.service.BuildService
import pro.javatar.pipeline.service.orchestration.DockerService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
class DockerOnlyBuildService extends BuildService {

    DockerService dockerService
    SetupVersionAware setupVersionAware
    CurrentVersionAware currentVersionAware

    DockerOnlyBuildService(DockerService dockerService,
                           SetupVersionAware setupVersionAware,
                           CurrentVersionAware currentVersionAware) {
        this.dockerService = dockerService
        this.setupVersionAware = setupVersionAware
        this.currentVersionAware = currentVersionAware
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.debug("DockerOnlyBuildService: buildAndUnitTests started")
        dockerService.dockerBuildImage(releaseInfo)
        Logger.debug("DockerOnlyBuildService: buildAndUnitTests finished")
    }

    @Override
    void setUp() {
        Logger.debug("DockerOnlyBuildService: setUp()")
    }

    // TODO remove later from BuildService
    @Override
    String getCurrentVersion() {
        return currentVersionAware.getCurrentVersion()
    }

    // TODO remove later from BuildService
    @Override
    def setupReleaseVersion(String releaseVersion) {
        return setupVersionAware.setupVersion(releaseVersion)
    }

    // TODO remove later from BuildService
    @Override
    def setupVersion(String version) {
        return setupVersionAware.setupVersion(version)
    }
}
