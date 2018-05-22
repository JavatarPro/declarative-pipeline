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

package pro.javatar.pipeline.service.vcs

import pro.javatar.pipeline.model.ReleaseInfo

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-18
 */
abstract class AbstractFlowService<VCS extends RevisionControlService> {

    VCS revService

    AbstractFlowService(VCS revService) {
        this.revService = revService
    }

    abstract def initDefaultFlow()

    abstract def initFlowWithPrefix(String flowPrefix)

    def initFlow() {
        if (revService.hasFlowPrefix()) {
            dsl.echo "started AbstractFlowService initFlow with prefix"
            createBranchesIfNotExists(revService.getFlowPrefix())
            initFlowWithPrefix(revService.getFlowPrefix())
        } else {
            dsl.echo "started AbstractFlowService initFlow without prefix"
            createBranchesIfNotExists(null)
            initDefaultFlow()
        }
        dsl.echo "finished AbstractFlowService initFlow"
    }

    def createBranchesIfNotExists(String prefix) {
        dsl.echo "started createBranchesIfNotExists prefix: ${prefix}"
        String prodBranch = revService.getProdBranch()
        String prodBranchWithPrefix = revService.getProdBranchWithPrefix(prefix)
        String developWithPrefix = revService.getDevBranchWithPrefix(prefix)
        if (!revService.isBranchExists(prodBranchWithPrefix)) {
            dsl.echo "createBranchesIfNotExists: missing branch prodBranchWithPrefix: ${prodBranchWithPrefix}"
            revService.switchToBranch(prodBranch)
            revService.createAndPushBranch(prodBranchWithPrefix)
        }
        if (!revService.isBranchExists(developWithPrefix)) {
            dsl.echo "createBranchesIfNotExists: missing branch developWithPrefix: ${developWithPrefix}"
            revService.switchToBranch(prodBranchWithPrefix)
            revService.createAndPushBranch(developWithPrefix)
        }
        revService.switchToBranch(developWithPrefix)
        dsl.echo "finished createBranchesIfNotExists prefix: ${prefix}"
    }

    def getReleaseBranchFolder() {
        if (revService.hasFlowPrefix()) {
            return "${revService.getFlowPrefix()}-release"
        }
        return "release"
    }

    String getReleaseBranch(ReleaseInfo releaseInfo) {
        return "${getReleaseBranchFolder()}/${releaseInfo.getPrefixedReleaseVersion()}"
    }

}
