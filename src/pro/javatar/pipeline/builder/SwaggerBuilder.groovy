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

package pro.javatar.pipeline.builder

import pro.javatar.pipeline.util.Logger;

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SwaggerBuilder implements Serializable {

    private boolean enabled;
    private String parentPageId;
    private String spaceKey;
    private String confluenceUrl;
    private String apiGatewayUrl;

    SwaggerBuilder() {
        Logger.debug("SwaggerBuilder:default constructor")
    }

    public SwaggerBuilder withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SwaggerBuilder withParentPageId(String parentPageId) {
        this.parentPageId = parentPageId;
        return this;
    }

    public SwaggerBuilder withSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
        return this;
    }

    public SwaggerBuilder withConfluenceUrl(String confluenceUrl) {
        this.confluenceUrl = confluenceUrl;
        return this;
    }

    public SwaggerBuilder withApiGatewayUrl(String apiGatewayUrl) {
        this.apiGatewayUrl = apiGatewayUrl;
        return this;
    }
}
