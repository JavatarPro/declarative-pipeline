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

import com.cloudbees.groovy.cps.NonCPS;

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SlackService implements Serializable {

    String serviceName
    boolean slackEnabled
    String slackChannel
    String slackWebhookUrl
    // dsl.env.JOB_URL

    SlackService( String serviceName, boolean slackEnabled, String slackChannel, String slackWebhookUrl) {
        this.serviceName = serviceName
        this.slackEnabled = slackEnabled
        this.slackChannel = slackChannel
        this.slackWebhookUrl = slackWebhookUrl
    }

    @NonCPS
    @Override
    public String toString() {
        return "SlackService{" +
                "serviceName='" + serviceName + '\'' +
                ", slackEnabled=" + slackEnabled +
                ", slackChannel='" + slackChannel + '\'' +
                ", slackWebhookUrl='" + slackWebhookUrl + '\'' +
                '}';
    }
}
