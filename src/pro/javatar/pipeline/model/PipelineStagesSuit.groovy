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
package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedPipelineStagesSuitException

import static pro.javatar.pipeline.util.StringUtils.isBlank

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum PipelineStagesSuit {

    SERVICE,
    SERVICE_WITH_DB,
    SERVICE_SIMPLE,
    LIBRARY,
    CUSTOM

    static PipelineStagesSuit fromString(String suit) {
        if (suit == null) {
            throw new UnrecognizedPipelineStagesSuitException("suit is null")
        }
        if("service".equalsIgnoreCase(suit) || "µService".equalsIgnoreCase(suit)
                || "ui".equalsIgnoreCase(suit)) {
            return SERVICE
        }
        if("library".equalsIgnoreCase(suit) || "lib".equalsIgnoreCase(suit)
                || "component".equalsIgnoreCase(suit)
                || "ui-component".equalsIgnoreCase(suit)) {
            return LIBRARY
        }
        if("custom".equalsIgnoreCase(suit) || isBlank(suit)) {
            return CUSTOM
        }
        if("service-with-db".equalsIgnoreCase(suit) || isBlank(suit)) {
            return SERVICE_WITH_DB
        }
        if("service-simple".equalsIgnoreCase(suit) || isBlank(suit)) {
            return SERVICE_SIMPLE
        }
        throw new UnrecognizedPipelineStagesSuitException("suit ${suit} is not recognized")
    }
}
