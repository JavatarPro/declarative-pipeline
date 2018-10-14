/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/pipeline-utils/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.javatar.pipeline.service

import java.util.concurrent.ConcurrentHashMap
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 5/27/18 23:26
 */
class ServiceContextHolder {

    final static Map<Class, Object> serviceHolder = new ConcurrentHashMap<>()

    static def addService(Object service) {
        if (service == null) {
            return
        }
        return serviceHolder.put(service.getClass(), service)
    }

    static def addService(Class key, Object service) {
        if (service == null) {
            return
        }
        return serviceHolder.put(key, service)
    }

    static def getService(Class service) {
        dsl.echo "ServiceContextHolder: getService: ${service}"
        if (service == null) {
            return
        }
        def result = serviceHolder.get(service)
        dsl.echo "ServiceContextHolder: getService: result: ${result}"
        return result
    }

    static def removeService(Class service) {
        if (service == null) {
            return
        }
        return serviceHolder.remove(service)
    }

}
