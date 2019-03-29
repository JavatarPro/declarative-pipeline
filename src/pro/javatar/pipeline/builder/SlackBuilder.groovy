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

package pro.javatar.pipeline.builder;

import pro.javatar.pipeline.service.SlackService
import pro.javatar.pipeline.util.Logger;

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SlackBuilder implements Serializable {

    private String channel;
    private String webHookUrl;
    private boolean enabled;

    SlackBuilder() {
        Logger.debug("SlackBuilder:default constructor")
    }

    public SlackBuilder withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public SlackBuilder withWebHookUrl(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        return this;
    }

    public SlackBuilder withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SlackService build() {
        return null;
    }
}
