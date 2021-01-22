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
package pro.javatar.pipeline.service.orchestration.template

import groovy.json.JsonSlurper

import static pro.javatar.pipeline.util.StringUtils.toJson

/**
 * @author Borys Zora
 * @version 2019-03-29
 */
class JsonConfigMerger {

    String merge(List<String> jsonFiles) {
        if (jsonFiles == null || jsonFiles.isEmpty()) {
            return null
        }
        if (jsonFiles.size() == 1) {
            return jsonFiles.get(0)
        }
        return mergeMultipleJsons(jsonFiles)
    }

    protected String mergeMultipleJsons(List<String> jsonFiles) {
        def mainJson = new JsonSlurper().parseText(jsonFiles.get(0))
        for(int i = 1; i < jsonFiles.size(); i++) {
            def json = new JsonSlurper().parseText(jsonFiles.get(i))
            merge(mainJson, json)
        }
        return toJson(mainJson)
    }

    protected void merge(def mainJson, def jsonToMerge) {
        if (jsonToMerge instanceof Map) {
            mergeMap(mainJson, jsonToMerge)
        } else if (jsonToMerge instanceof List) {
            mergeList(mainJson, jsonToMerge)
        }
    }

    protected void mergeMap(def mainJson, def jsonToMerge) {
        jsonToMerge.each { key, value ->
            if (mainJson.containsKey(key)) {
                
            } else {
                mainJson.put(key, value)
            }
        }
    }

    protected void mergeList(def mainJson, def jsonToMerge) {
        jsonToMerge.each { item ->
            if (mainJson.contains(item)) {

            } else {
                mainJson.add(item)
            }
        }
    }


}
