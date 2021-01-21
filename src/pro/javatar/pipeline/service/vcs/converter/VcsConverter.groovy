package pro.javatar.pipeline.service.vcs.converter


import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.service.vcs.VcsRepositoryUrlResolver
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest
import pro.javatar.pipeline.util.Logger

class VcsConverter {

    // static VcsRepositoryUrlResolver urlResolver = ServiceContextHolder.getService(VcsRepositoryUrlResolver.class)

    static VscCheckoutRequest toVscCheckoutRequest(VcsRepo vcsRepo, RevisionControlService revisionControlService) {
        Logger.info("VcsConverter: toVscCheckoutRequest: vcsRepo: ${vcsRepo}")
        VcsRepositoryUrlResolver urlResolver = new VcsRepositoryUrlResolver(vcsRepo.getType(), vcsRepo.getSsh(),
                revisionControlService)
        String repoUrl = urlResolver.getRepoUrl(vcsRepo)
        return new VscCheckoutRequest()
                .withCredentialsId(vcsRepo.getCredentialsId())
                .withRepoUrl(repoUrl)
                .withBranch(vcsRepo.getBranch())
    }

}
