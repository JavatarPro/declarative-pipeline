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

import pro.javatar.pipeline.model.VcsRepositoryType
import pro.javatar.pipeline.service.vcs.model.VcsRepo

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
        String repoName = revisionControlService.getRepoName()
        String repoOwner = revisionControlService.getRepoOwner()

        return getRepoUrl(repoOwner, repoName)
    }

    String getRepoUrl(String repoOwner, String repoName) {
        VcsRepo vcsRepo = new VcsRepo()
                .withOwner(repoOwner)
                .withName(repoName)
                .withType(vcsRepositoryType)
                .withSsh(useSsh)
                .withDomain(revisionControlService.getDomain())

        return getRepoUrl(vcsRepo)
    }

    String getRepoUrl(VcsRepo vcsRepo) {
        String userName = revisionControlService.getUserName()

        if (vcsRepositoryType == VcsRepositoryType.GITLAB) {
            if (useSsh) {
                return "ssh://git@${vcsRepo.getDomain()}:${vcsRepo.getOwner()}/${vcsRepo.getName()}.git"
            }
            throw new UnsupportedOperationException("gitlab https is not yet implemented")
        }
        if (vcsRepositoryType == VcsRepositoryType.GITHUB) {
            if (useSsh) {
                return "git@${vcsRepo.getDomain()}:${vcsRepo.getOwner()}/${vcsRepo.getName()}.git"
            }
            throw new UnsupportedOperationException("github https is not yet implemented")
        }
        if (vcsRepositoryType == VcsRepositoryType.BITBUCKET) {
            if (useSsh) {
                return "ssh://git@${vcsRepo.getDomain()}/${vcsRepo.getOwner()}/${vcsRepo.getName()}.git"
            }
            return "https://${userName}@bitbucket.org/${vcsRepo.getOwner()}/${vcsRepo.getName()}.git/"
        }
    }
}
