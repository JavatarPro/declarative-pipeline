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

import pro.javatar.pipeline.model.VcsRepositoryType

/**
 * Author : Borys Zora
 * Date Created: 3/22/18 23:49
 */
class VcsRepositoryUrlResolver {

    VcsRepositoryType vcsRepositoryType

    boolean useSsh = true

    RevisionControlService revisionControlService

    VcsRepositoryUrlResolver(VcsRepositoryType vcsRepositoryType,
                             boolean useSsh,
                             RevisionControlService revisionControlService) {
        this.vcsRepositoryType = vcsRepositoryType
        this.useSsh = useSsh
        this.revisionControlService = revisionControlService
    }

    VcsRepositoryUrlResolver(VcsRepositoryType vcsRepositoryType) {
        this.vcsRepositoryType = vcsRepositoryType
    }

    String getRepoUrl() {
        String userName = revisionControlService.getUserName()
        String repoName = revisionControlService.getRepoName()
        String repoOwner = revisionControlService.getRepoOwner()
        String domain = revisionControlService.getDomain()

        if (vcsRepositoryType == VcsRepositoryType.GITLAB) {
            if (useSsh) {
                return "git@${domain}:${repoOwner}/${repoName}.git"
            }
            throw new UnsupportedOperationException("gitlab https is not yet implemented")
        }
        if (vcsRepositoryType == VcsRepositoryType.BITBUCKET) {
            if (useSsh) {
                return "ssh://git@${domain}/${repoOwner}/${repoName}.git"
            }
            return "https://${userName}@bitbucket.org/${repoOwner}/${repoName}.git/"
        }
    }
}
