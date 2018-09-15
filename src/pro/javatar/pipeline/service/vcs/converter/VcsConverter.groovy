package pro.javatar.pipeline.service.vcs.converter

import pro.javatar.pipeline.service.ServiceContextHolder
import pro.javatar.pipeline.service.vcs.VcsRepositoryUrlResolver
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest

class VcsConverter {

    static VcsRepositoryUrlResolver urlResolver = ServiceContextHolder.getService(VcsRepositoryUrlResolver.class)

    static VscCheckoutRequest toVscCheckoutRequest(VcsRepo vcsRepo) {
        String repoUrl = urlResolver.getRepoUrl(vcsRepo)
        return new VscCheckoutRequest()
                .withCredentialsId(vcsRepo.getCredentialsId())
                .withRepoUrl(repoUrl)
                .withBranch(vcsRepo.getBranch())
    }

}
