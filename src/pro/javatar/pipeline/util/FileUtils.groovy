package pro.javatar.pipeline.util

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class FileUtils {

    static def replace(String currentValue, String value, String fileName) {
        dsl.echo "${fileName} before change currentValue: ${currentValue} to value: ${value}"
        dsl.sh "cat ${fileName} | grep ${currentValue}"

        dsl.sh "sed -i.bak s/${currentVersion}/${value}/g ${fileName}"
        dsl.sh "rm ${fileName}.bak"

        dsl.echo "${fileName} after change currentValue: ${currentValue} to value: ${value}"
        dsl.sh "cat ${fileName} | grep ${value}"
    }

}
