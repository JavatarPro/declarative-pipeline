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

import pro.javatar.pipeline.jenkins.api.JenkinsDslService

import static pro.javatar.pipeline.util.LogLevel.*
import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonOutput

/**
 * TODO append all logs to file and in console only that specified
 * @author Borys Zora
 * @version 2019-03-29
 */
class Logger implements Serializable {

    static LogLevel LEVEL = INFO
    static JenkinsDslService dslService

    @NonCPS
    static void fatal(def message) {
        print(message, FATAL)
    }

    @NonCPS
    static void error(def message) {
        print(message, ERROR)
    }

    @NonCPS
    static void warn(def message) {
        print(message, WARN)
    }

    @NonCPS
    static void info(def message) {
        print(message, INFO)
    }

    @NonCPS
    static void info(String message, Map value) {
        String msg = message.replace("{}", StringUtils.toString(value))
        print(msg, INFO)
    }

    @NonCPS
    static void debug(def message) {
        print(message, DEBUG)
    }

    @NonCPS
    static void trace(def message) {
        print(message, TRACE)
    }

    @NonCPS
    private static print(def message, LogLevel loggerLevel) {
        try {
            if (LEVEL.ordinal() >= loggerLevel.ordinal()) {
                String msg = JsonOutput.toJson(message)
                dslService.echo(loggerLevel.name() + ": " + msg)
            }
        } catch (Exception e) {
            dslService.echo("Logger:print:Exception " + e.getClass())
            dslService.echo(e.getMessage())
            dslService.echo(e.printStackTrace())
        }
    }
}
