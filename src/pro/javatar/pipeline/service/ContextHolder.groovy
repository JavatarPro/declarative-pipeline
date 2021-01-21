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

package pro.javatar.pipeline.service

import pro.javatar.pipeline.util.Logger

import java.util.concurrent.ConcurrentHashMap

/**
 * Author : Borys Zora
 * Date Created: 5/27/18 23:26
 */
class ContextHolder {

    final static Map<String, Object> serviceHolder = new ConcurrentHashMap<>()

    static def add(Object service) {
        if (service == null) {
            return
        }
        return serviceHolder.put(service.getClass().getCanonicalName(), service)
    }

    static def add(Class key, Object service) {
        if (service == null) {
            return
        }
        return serviceHolder.put(key.getCanonicalName(), service)
    }

    static def get(Class service) {
        Logger.trace("ServiceContextHolder: getService: ${service}")
        if (service == null) {
            return
        }
        def result = serviceHolder.get(service)
        Logger.trace("ServiceContextHolder: getService: result: ${result}")
        return result
    }

    static def remove(String key) {
        if (key == null) {
            return
        }
        return serviceHolder.remove(key)
    }

    static def remove(Class service) {
        remove(service.getCanonicalName())
    }

}
