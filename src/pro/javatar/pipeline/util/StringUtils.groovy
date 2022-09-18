/**
 * Copyright Javatar LLC 2018 ©
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

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonBuilder
import groovy.text.GStringTemplateEngine
import pro.javatar.pipeline.exception.ReplaceBindingMapException

/**
 * Author : Borys Zora
 * Date Created: 3/26/18 11:23
 */
class StringUtils {

    static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    static String addPrefixIfNotExists(String prefix, String source) {
        if (isBlank(prefix)) {
            return source
        }
        if (isBlank(source) || source.startsWith(prefix)) {
            return source
        }
        return "${prefix}-${source}"
    }

    public static String replaceVariables(String configuration, Map binding) throws ReplaceBindingMapException {
        try {
            Logger.debug("StringUtils:replaceVariables:configuration: " + configuration +
                    "\nreplaceVariables: " + binding)
            def engine = new GStringTemplateEngine()
            def template = engine.createTemplate(configuration).make(binding)
            String result = template.toString()
            Logger.debug("StringUtils:replaceVariables:result: " + result)
            return result
        } catch (Exception e) {
            Logger.warn("issue while replaceVariables" + e.getMessage() + "cause: " + e.getCause());
            e.printStackTrace();
            throw new ReplaceBindingMapException(e.getMessage());
        }

    }

    @NonCPS
    static String toString(Map map) {
        StringBuilder sb = new StringBuilder("{")
        map.each { key, value ->
            sb.append("key: " + key)
                    .append(", value: " + value)
        }
        sb.append("}")
        return sb.toString()
    }

    @NonCPS
    static String toString(def other) {
        if (other instanceof Map) {
            return toString((Map) other)
        }
        return other.toString()
    }

    @NonCPS
    static def renderTemplate(String template, Map binding) {
        def engine = new GStringTemplateEngine()
        def result = engine.createTemplate(template).make(binding)
        return result.toString()
    }

    @NonCPS
    static String toJson(def object) {
        if (object == null) {
            return null
        }
        return new JsonBuilder(object).toString()
    }

    @NonCPS
    static String toPrettyJson(def object) {
        if (object == null) {
            return null
        }
        return new JsonBuilder(object).toPrettyString()
    }

}
