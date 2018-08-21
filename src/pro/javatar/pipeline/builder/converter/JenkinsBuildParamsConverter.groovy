package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.JenkinsBuildParams
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class JenkinsBuildParamsConverter {

    void populateWithJenkinsBuildParams(def properties) {
        def params = dsl.params
        params.each{param -> replaceVariablesOrSetProperty(param, properties)}
    }

    void replaceVariablesOrSetProperty(def param, def properties) {
        dsl.echo "param.key: ${param.key}, param.value: ${param.value}"
        if (JenkinsBuildParams.hasKey(param.key)) {
            dsl.echo "param.key: ${param.key} is valid properity"
            if (JenkinsBuildParams.PROFILES.getKey().equalsIgnoreCase(param.key)) {
                amendAccordingToProfile(param.value, properties)
            } else {
                dsl.echo "properties.put(param.key: ${param.key}, param.value: ${param.value}"
                properties.put(param.key, param.value)
            }
        } else {
            // TODO remove, does not need
            dsl.echo "properties before replaceVariable(${param}: ${properties})"
            replaceVariable(param, properties)
            dsl.echo "properties after replaceVariable(${param}: ${properties})"
        }
    }

    void amendAccordingToProfile(String profileName, def properties) {
        dsl.echo "detected profile change: '${profileName}' profile will be applied"
        def profile = properties.profile[profileName]
        dsl.echo "profile variables: ${profile}"
        dsl.echo "properties before apply: ${properties}"
        profile.each {key, value ->
            String[] keys = key.split("\\.")
            def target = properties
            for(int i = 0; i < keys.length -1; i++) {
                target = target[keys[i]]
            }
            target.put(keys[keys.length - 1], value)
        }
        dsl.echo "properties after apply: ${properties}"
    }

    void replaceVariable(def param, def properties) {
        dsl.echo "replaceVariable param: ${param}"
        String toBeReplaced = "\${${param.key}}"
        properties.each{ key, value ->
            if(value.toString().trim().equalsIgnoreCase(toBeReplaced)) {
                properties.put(key, param.value)
            }
        }
    }

}
