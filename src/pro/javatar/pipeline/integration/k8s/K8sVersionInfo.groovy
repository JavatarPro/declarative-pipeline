/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import pro.javatar.pipeline.domain.DockerImage
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

    Map<String, String> versionsNext() {

    }

    String allVersions() {
        return null
    }

}
