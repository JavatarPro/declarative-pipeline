package pro.javatar.pipeline.builder.converter

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class JenkinsBuildParamsConverter {

    void populateWithJenkinsBuildParams(def properties) {
        def params = dsl.params
        params.each{param -> replaceVariablesOrSetProperty(param, properties)}
    }

    void replaceVariablesOrSetProperty(def param, def properties) {
        dsl.echo "param.key: ${param.key}, param.value: ${param.value}"

    }

}
