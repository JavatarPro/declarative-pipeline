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

    @NonCPS
    static String toString(Map map) {
        StringBuilder sb = new StringBuilder("{")
        map.each { key, value ->
            sb.append("key: ${key}")
                    .append(", value: ${value}")
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

}
