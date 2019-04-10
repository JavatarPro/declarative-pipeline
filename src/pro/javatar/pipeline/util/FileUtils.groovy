/**
 * Copyright Javatar LLC 2019 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/declarative-pipeline/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.javatar.pipeline.util

import java.nio.file.Files
import java.nio.file.Paths

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class FileUtils {

    static def replace(String currentValue, String value, String fileName) {
        Logger.info("${fileName} before change currentValue: ${currentValue} to value: ${value}")
        dsl.sh "cat ${fileName} | grep ${currentValue}"

        dsl.sh "sed -i.bak s/${currentValue}/${value}/g ${fileName}"
        dsl.sh "rm ${fileName}.bak"

        Logger.info("${fileName} after change currentValue: ${currentValue} to value: ${value}")
        dsl.sh "cat ${fileName} | grep ${value}"
    }

    static String readFile(String path) {
        if (new File(path).exists()) {
            Logger.debug("FileUtils:readFile: file: ${path} exists")
            return new String(Files.readAllBytes(Paths.get(path)))
        }
        Logger.warn("FileUtils:readFile: file: ${path} does not exists")
        return null
    }

    static Map<String, Object> propertyFileToMap(String path) {
        File file = new File(path)
        if (!file.exists()) {
            Logger.info("FileUtils:propertyFileToMap: file: ${path} does not exists. No variable overrides needed")
            return
        }
        Properties properties = new Properties()
        properties.load(file.newInputStream())
        return new HashMap<String, Object>(properties)
    }
}
