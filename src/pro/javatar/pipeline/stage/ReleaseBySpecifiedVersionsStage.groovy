/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.domain.DockerImage
import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.integration.k8s.K8sVersionInfo
import pro.javatar.pipeline.integration.k8s.KubernetesService
import pro.javatar.pipeline.integration.slack.SlackChannelSender
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import pro.javatar.pipeline.service.vcs.RevisionControlService

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

    @Override
    void execute() throws PipelineException {
        K8sVersionInfo versionInfo = get(K8sVersionInfo.class)
        SlackChannelSender sender = get(SlackChannelSender.class)
        KubernetesService service = get(KubernetesService.class)
        RevisionControlService vcs = get(RevisionControlService.class)
        JenkinsDsl dsl = get(JenkinsDsl.class)

        vcs.checkoutIntoFolder("master")
        def releaseRequest = dsl.readJson("${vcs.folder}/${RELEASE_FILE}")
        def currentVersions = releaseRequest.currentVersions
        def proposedVersions = releaseRequest.proposedVersions
        def prodVersions = versionInfo.versionsCurrent()
        def updates = versionInfo.toUpdate(proposedVersions, prodVersions)
        updates.each {deployment, image ->
            service.createOrReplace(DockerImage.fromString(image))
        }
        def result = [
                devVersions: currentVersions,
                proposedVersions: proposedVersions,
                prodVersions: prodVersions,
                updates: updates
        ]
        String json = toPrettyJson(result)
        // TODO make slack template
        sender().send("Release in progress")
        sender().send("```\n${json}\n```")
        vcs.makeDir(RELEASE_FOLDER)
        String historyFile = RELEASE_HISTORY_FILE.replace("{date}", Instant.now().truncatedTo(ChronoUnit.MINUTES))
        vcs.moveFile(RELEASE_FILE, historyFile)
        vcs.commitChanges("Release completed, move ${RELEASE_FILE} to ${historyFile}")
        sender().send("Release completed")
    }

    @Override
    String name() {
        return "release"
    }
}
