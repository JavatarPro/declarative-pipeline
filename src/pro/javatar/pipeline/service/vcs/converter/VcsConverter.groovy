package pro.javatar.pipeline.service.vcs.converter

import pro.javatar.pipeline.service.ServiceContextHolder
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.service.vcs.VcsRepositoryUrlResolver
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class VcsConverter {

    // static VcsRepositoryUrlResolver urlResolver = ServiceContextHolder.getService(VcsRepositoryUrlResolver.class)

    static VscCheckoutRequest toVscCheckoutRequest(VcsRepo vcsRepo, RevisionControlService revisionControlService) {
        dsl.echo "VcsConverter: toVscCheckoutRequest: vcsRepo: ${vcsRepo}"
        VcsRepositoryUrlResolver urlResolver = new VcsRepositoryUrlResolver(vcsRepo.getType(), vcsRepo.getSsh(),
                revisionControlService)
        String repoUrl = urlResolver.getRepoUrl(vcsRepo)
        return new VscCheckoutRequest()
                .withCredentialsId(vcsRepo.getCredentialsId())
                .withRepoUrl(repoUrl)
                .withBranch(vcsRepo.getBranch())
    }

}
