/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.domain.DockerImage
import pro.javatar.pipeline.domain.K8sVersions
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.integration.k8s.K8sVersionInfo
import pro.javatar.pipeline.integration.k8s.KubernetesService
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger

import java.time.Instant
import java.time.temporal.ChronoUnit

import static pro.javatar.pipeline.service.ContextHolder.get
import static pro.javatar.pipeline.util.StringUtils.toPrettyJson

/**
 * @author Borys Zora
 * @version 2022-10-03
 */
class ReleaseBySpecifiedVersionsStage extends Stage {

    static final String RELEASE_FOLDER = "releases"
    static final String RELEASE_FILE = "release.json"
    static final String RELEASE_HISTORY_FILE = "${RELEASE_FOLDER}/release-{date}.json"

    K8sVersionInfo versionInfo
    SlackChannelSender sender
    KubernetesService service
    RevisionControlService vcs
    JenkinsDsl dsl

    @Override
    void execute() throws PipelineException {
        fillInServices()
        vcs.checkoutIntoFolder("master")
        def releaseRequest = dsl.readJson("${vcs.folder}/${RELEASE_FILE}")
        Logger.trace("releaseRequest: \n${releaseRequest}")
        def proposedVersions = releaseRequest.get(K8sVersions.PROPOSED_VERSIONS)
        def prodVersions = versionInfo.versionsCurrent()
        Logger.trace("prodVersions: \n${prodVersions}")
        def updates = versionInfo.toUpdate(proposedVersions, prodVersions)
        updates.each {deployment, image ->
            service.createOrReplace(DockerImage.fromString(image))
        }
        def result = [
                "${K8sVersions.DEV_VERSIONS}": releaseRequest.get(K8sVersions.DEV_VERSIONS),
                "${K8sVersions.PROPOSED_VERSIONS}": proposedVersions,
                "${K8sVersions.PROD_VERSIONS}": prodVersions,
                "${K8sVersions.PROD_VERSION_UPDATES}": updates,
        ]
        String json = toPrettyJson(result)
        // TODO make slack template
        sender.send("Release in progress")
        sender.send("```\n${json}\n```")
        auditRelease()
        sender.send("Release completed")
    }

    @Override
    String name() {
        return "release"
    }

    // helper methods

    def auditRelease() {
        Logger.debug("ReleaseBySpecifiedVersionsStage#auditRelease started")
        vcs.makeDir(RELEASE_FOLDER)
        String historyFile = RELEASE_HISTORY_FILE.replace("{date}",
                Instant.now().truncatedTo(ChronoUnit.MINUTES).toString())
        vcs.moveFile(RELEASE_FILE, historyFile)
        vcs.commit("Release completed, move ${RELEASE_FILE} to ${historyFile}")
        vcs.push()
        Logger.debug("ReleaseBySpecifiedVersionsStage#auditRelease completed")
    }

    def fillInServices() {
        Logger.debug("ReleaseBySpecifiedVersionsStage#fillInServices started")
        versionInfo = get(K8sVersionInfo.class)
        Logger.debug("versionInfo available: ${versionInfo != null}")
        sender = get(SlackChannelSender.class)
        Logger.debug("sender available: ${sender != null}")
        service = get(KubernetesService.class)
        Logger.debug("service available: ${service != null}")
        vcs = get(RevisionControlService.class)
        Logger.debug("vcs available: ${vcs != null}")
        dsl = get(JenkinsDsl.class)
        Logger.debug("dsl available: ${dsl != null}")
        Logger.debug("ReleaseBySpecifiedVersionsStage#fillInServices completed")
    }
}
