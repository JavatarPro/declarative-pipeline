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

package pro.javatar.pipeline.builder

import pro.javatar.pipeline.exception.UnrecognizedRevisionControlTypeException
import pro.javatar.pipeline.model.RevisionControlType
import pro.javatar.pipeline.model.VcsRepositoryType
import pro.javatar.pipeline.service.vcs.HgService
import pro.javatar.pipeline.service.vcs.GitService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.service.vcs.VcsRepositoryUrlResolver

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class RevisionControlBuilder implements Serializable {

    String branch
    String repo
    String repoOwner
    String flowPrefix
    String credentialsId
    String domain
    RevisionControlType type
    VcsRepositoryType vcsRepositoryType

    RevisionControlService build() {
        dsl.echo "RevisionControlService.build() started"
        RevisionControlService result
        if (type == RevisionControlType.MERCURIAL) {
            result = new HgService(repo, credentialsId, repoOwner, flowPrefix)
        } else if (type == RevisionControlType.GIT) {
            result = new GitService(repo, credentialsId, repoOwner, flowPrefix)
        }
        result.setUrlResolver(new VcsRepositoryUrlResolver(vcsRepositoryType, true, result))
        result.setDomain(domain)
        dsl.echo "RevisionControlService.build() finished"
        return result
    }

    RevisionControlBuilder withRepo(String repo) {
        this.repo = repo
        return this
    }

    // TODO refactor if used appropriate type use its domain (e.g. github)
    RevisionControlBuilder withDomain(String domain) {
        this.domain = domain
        return this
    }

    RevisionControlBuilder withRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner
        return this
    }

    RevisionControlBuilder withFlowPrefix(String flowPrefix) {
        this.flowPrefix = flowPrefix
        return this
    }

    RevisionControlBuilder withRevisionControlType(String type) {
        this.type = RevisionControlType.fromString(type)
        return this
    }

    RevisionControlBuilder withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    RevisionControlBuilder withBranch(String branch) {
        this.branch = branch
        return this
    }

    RevisionControlBuilder withVcsRepositoryType(String vcsRepositoryType) {
        this.vcsRepositoryType = VcsRepositoryType.fromString(vcsRepositoryType)
        return this
    }

    String getPrefixedDevelopBranch() {
        if (flowPrefix != null) {
            return "${flowPrefix}-${branch}"
        }
        return branch
    }

}
