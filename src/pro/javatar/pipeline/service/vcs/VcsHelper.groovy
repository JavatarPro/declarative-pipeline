package pro.javatar.pipeline.service.vcs

import pro.javatar.pipeline.service.ContextHolder
import pro.javatar.pipeline.service.vcs.converter.VcsConverter
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 5/27/18 23:00
 */
class VcsHelper {

    def static checkoutRepo(String repo, String branch, String folder) {
        RevisionControlService revisionControlService = ContextHolder.get(RevisionControlService.class)
        String repoOwner = revisionControlService.getRepoOwner()
        Logger.info("VcsHelper: repoOwner: " + repoOwner + " will be used")
        return checkoutRepo(repoOwner, repo, branch, folder)
    }

    def static checkoutRepo(String repoOwner, String repo, String branch, String folder) {
        RevisionControlService revisionControlService = ContextHolder.get(RevisionControlService.class)
        Logger.info("VcsHelper: revisionControlService: " + revisionControlService + " will be used")
        dsl.dir(folder) {
            revisionControlService.checkoutRepo(repoOwner, repo, branch)
        }
    }

    def static checkoutRepo(VcsRepo vcsRepo, String folder) {
        Logger.info("VcsHelper: checkoutRepo: vcsRepo: " + vcsRepo + ", folder: " + folder)
        // TODO vcsRepo.revisionControlType ignored, used only common RevisionControlService
        RevisionControlService revisionControlService = ContextHolder.get(RevisionControlService.class)
        Logger.info("VcsHelper: checkoutRepo: revisionControlService: " + revisionControlService + " will be used")
        VscCheckoutRequest request = VcsConverter.toVscCheckoutRequest(vcsRepo, revisionControlService)
        dsl.dir(folder) {
            Logger.info("before call revisionControlService.checkoutRepo(request: " + request.toString())
            revisionControlService.checkoutRepo(request)
        }
    }

}
