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
        if (JenkinsBuildParams.hasKey(param.key)){
            properties.put(param.value, param.value)
        } else {
            replaceVariable(param, properties)
        }
    }

    void replaceVariable(def param, def properties) {
        String toBeReplaced = "\${${param.key}}"
        properties.each{ key, value ->
            if(value.toString().trim().equalsIgnoreCase(toBeReplaced)) {
                properties.put(key, param.value)
            }
        }
//        properties.each { prop ->
//            if(prop.value.trim().equalsIgnoreCase(toBeReplaced)) {
//                dsl.echo "${prop.key} will be replaced with ${prop.key}"
//                prop.value.replace(toBeReplaced, param.value)
//                dsl.echo "prop: ${prop}"
//            }
//        }
    }

}
