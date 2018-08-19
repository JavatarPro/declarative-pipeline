package pro.javatar.pipeline.builder.converter

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class JenkinsBuildParamsConverter {


    def getProperty(String propertyName, def defaultValue) {
        dsl.echo "DEBUG: getProperty for propertyName: ${propertyName} with default: ${defaultValue}"
        try {
            return dsl.getProperty(propertyName)
        } catch (MissingPropertyException e) {
            dsl.echo "DEBUG: getProperty for propertyName: ${propertyName} not found, " +
                    "default: ${defaultValue} will be appied"
            return defaultValue
        }
    }
}
