package pro.javatar.pipeline.service.vcs

import pro.javatar.pipeline.service.ServiceContextHolder

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 5/27/18 23:00
 */
class VcsHelper {

    def static checkoutRepo(String repo, String branch, String folder) {
        RevisionControlService revisionControlService = ServiceContextHolder.getService(RevisionControlService.class)
        String repoOwner = revisionControlService.getRepoOwner()
        dsl.echo "VcsHelper: repoOwner: ${repoOwner} will be used"
        return checkoutRepo(repoOwner, repo, branch, folder)
    }

    def static checkoutRepo(String repoOwner, String repo, String branch, String folder) {
        RevisionControlService revisionControlService = ServiceContextHolder.getService(RevisionControlService.class)
        dsl.echo "VcsHelper: revisionControlService: ${revisionControlService} will be used"
        dsl.dir(folder) {
            revisionControlService.checkoutRepo(repoOwner, repo, branch)
        }
    }

}
