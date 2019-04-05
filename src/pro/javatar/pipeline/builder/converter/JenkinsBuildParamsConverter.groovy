package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.JenkinsBuildParams
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class JenkinsBuildParamsConverter {

    void populateWithJenkinsBuildParams(def properties) {
        dsl.params.each{param -> setProperty(param, properties)}
    }

    void setProperty(def param, def properties) {
        Logger.debug("param.key: ${param.key}, param.value: ${param.value}")
        if (!JenkinsBuildParams.hasKey(param.key)) {
            Logger.debug("param.key: ${param.key} IS NOT a valid property")
            return
        }
        Logger.debug("param.key: ${param.key} is valid properity")
        if (JenkinsBuildParams.PROFILES.getKey().equalsIgnoreCase(param.key)) {
            amendAccordingToProfile(param.value, properties)
        } else {
            Logger.debug("properties.put(param.key: ${param.key}, param.value: ${param.value}")
            properties.put(param.key, param.value)
        }
    }

    void amendAccordingToProfile(String profileName, def properties) {
        Logger.info("detected profile change: '${profileName}' profile will be applied")
        def profile = properties.profile[profileName]
        Logger.info("profile variables: ${profile}")
        Logger.trace("properties before apply: ${properties}")
        profile.each {key, value ->
            String[] keys = key.split("\\.")
            def target = properties
            for(int i = 0; i < keys.length -1; i++) {
                target = target[keys[i]]
            }
            target.put(keys[keys.length - 1], value)
        }
        Logger.trace("properties after apply: ${properties}")
    }

}
