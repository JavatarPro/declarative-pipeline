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

package pro.javatar.pipeline.service.vcs

import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-18
 */
class GitFlowService extends AbstractFlowService<GitService> {

    GitFlowService(GitService gitService) {
        super(gitService)
    }

    @Override
    def initDefaultFlow() {
        dsl.sh 'git flow init -df'
    }

    @Override
    def initFlowWithPrefix(String flowPrefix) {
        String initGitFlowCommand = "echo '${flowPrefix}-master/\\n${flowPrefix}-develop/\\n${flowPrefix}-feature/\\n" +
                "${flowPrefix}-bugfix/\\n${flowPrefix}-release/\\n${flowPrefix}-hotfix/\\n${flowPrefix}-support/" +
                "\\n${flowPrefix}-' | git flow init -f"
        Logger.info(initGitFlowCommand)
        dsl.sh initGitFlowCommand
        revService.showConfigFile()
    }

}
