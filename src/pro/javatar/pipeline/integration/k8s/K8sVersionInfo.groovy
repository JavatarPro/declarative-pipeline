/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.domain.DockerImage
import pro.javatar.pipeline.domain.Version
import pro.javatar.pipeline.jenkins.api.JenkinsDsl
import groovy.json.JsonSlurper

/**
 * @author Borys Zora
 * @version 2022-09-12
 */
class K8sVersionInfo implements Serializable {

    JenkinsDsl dsl
    String dockerUrlCurrent
    String dockerUrlNext

    K8sVersionInfo(JenkinsDsl dsl,
                   dockerUrlCurrent,
                   dockerUrlNext) {
        this.dsl = dsl
        this.dockerUrlCurrent = dockerUrlCurrent
        this.dockerUrlNext = dockerUrlNext
    }

    @NonCPS
    // versions from dev environment, used dockerUrlCurrent
    Map<String, String> versionsCurrent() {
        String json = dsl.getShellExecutionResponse("kubectl get deploy -o json")
        def parser = new JsonSlurper()
        def deployments = parser.parseText(json).items
        Map<String, String> result = new HashMap<>()
        deployments.each {d ->
            String image = d.spec.template.spec.containers[0].image
            if (image.startsWith(dockerUrlCurrent)) {
                DockerImage dockerImage = DockerImage.fromString(image)
                result.put(dockerImage.deployment, image)
            }
        }
        return result
    }

    @NonCPS
    // versions to be proposed for next release to some env, used dockerUrlNext
    Map<String, String> versionsNext(Map<String, String> versions) {
        Map<String, String> result = new HashMap<>()
        versions.each { service, image ->
            DockerImage di = DockerImage.fromString(image)
            Version v = Version.fromString(di.version)
            di.version = v.toStringWithoutBuild()
            di.dockerUrl = dockerUrlNext
            result.put(service, di.toString())
        }
        return result
    }

    // comparing proposed versions with existing on env to where we are going to promote
    Map<String, String> toUpdate(Map<String, String> proposedVersions, Map<String, String> existingVersions) {
        Map<String, String> result = new HashMap<>()
        proposedVersions.each { service, image ->
            String existingImage = existingVersions.get(service)
            if (existingImage == null || !existingImage.equals(image)) {
                result.put(service, image)
            }
        }
        return result
    }

}
