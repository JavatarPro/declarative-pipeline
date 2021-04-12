/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.integration.k8s

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import pro.javatar.pipeline.release.SetupVersionAware
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-04-11
 */
class K8sJsonSetupVersion implements SetupVersionAware, Serializable {

    String deployJsonConfig
    String containerName

    K8sJsonSetupVersion(String deployJsonConfig, String containerName) {
        this.deployJsonConfig = deployJsonConfig
        this.containerName = containerName
    }

    @Override
    def setupVersion(String version) {
        Logger.debug("K8sJsonSetupVersion:setupVersion: stared")
        def jsonSlurper = new JsonSlurper()
        def config = jsonSlurper.parseText(deployJsonConfig)
        String image = config.spec.template.spec.containers[0].image
        Logger.debug("K8sJsonSetupVersion:setupVersion: image=${image}")
        String newImage = image.split(":")[0] + ":" + version
        Logger.debug("K8sJsonSetupVersion:setupVersion: newImage=${newImage}")
        config.spec.template.spec.containers[0].image = newImage
        return JsonOutput.toJson(config)
    }

}
