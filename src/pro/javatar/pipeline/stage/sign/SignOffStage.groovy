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

package pro.javatar.pipeline.stage.sign

import pro.javatar.pipeline.exception.PipelineException
import pro.javatar.pipeline.model.ReleaseApprovalStatus
import pro.javatar.pipeline.service.SlackService
import pro.javatar.pipeline.stage.Stage

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
abstract class SignOffStage extends Stage {

    protected SlackService slackService

    @Override
    void execute() throws PipelineException {
        dsl.echo "SignOffStage execute started: ${toString()}"
        def releaseApproval = getReleaseApprovedStatus()
        dsl.echo "releaseApproval: ${releaseApproval}"

        if (ReleaseApprovalStatus.RELEASE != releaseApproval) {
            dsl.echo "Technical commit without release"
            exitFromPipeline = true
        }
        dsl.echo "SignOffStage execute finished"
    }

    def getReleaseApprovedStatus() {
        dsl.echo "release approval status by developer"
        try {
            dsl.timeout(time: 30, unit: 'MINUTES') {
                return dsl.input(
                        id: 'userInput',
                        ok:'Proceed',
                        message: 'Do you want to release this build?',
                        parameters: [
                                [
                                        $class: 'ChoiceParameterDefinition',
                                        choices: "${ReleaseApprovalStatus.RELEASE}\n${ReleaseApprovalStatus.NOT_RELEASE}",
                                        defaultValue: "${ReleaseApprovalStatus.NOT_RELEASE}",
                                        name: 'Take your pick',
                                        description: 'Choose Release or not'
                                ]
                        ]
                )
            }
        } catch (Exception e) {
            dsl.echo "some exception occured in getReleaseApprovedStatus: ${e.getMessage()}"
            return "${ReleaseApprovalStatus.NOT_RELEASE}"
        }
    }

    abstract String getApprovePersonType();

    @Override
    String getName() {
        return "${getApprovePersonType()} sign off"
    }

    @Override
    public String toString() {
        return "DeveloperSignOffStage{" +
                "slackService=" + slackService +
                '}';
    }
}
