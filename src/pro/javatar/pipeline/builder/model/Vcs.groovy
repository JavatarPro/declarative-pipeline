package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.isBlank

class Vcs implements Serializable {

    String revisionControl

    Map<String, VcsRepoTO> repo

    Vcs() {
        Logger.debug("Vcs:default constructor")
    }

    Vcs populateRevisionControl() {
        for (VcsRepoTO vcsRepo: repo.values()) {
            if (isBlank(vcsRepo.getRevisionControl())) {
                vcsRepo.setRevisionControl(revisionControl)
            }
        }
        return this
    }

    String getRevisionControl() {
        return revisionControl
    }

    void setRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
    }

    Vcs withRevisionControl(String revisionControl) {
        this.revisionControl = revisionControl
        return this
    }

    Map<String, Vcs> getRepo() {
        return repo
    }

    void setRepo(Map<String, VcsRepoTO> repo) {
        this.repo = repo
    }

    Vcs withRepo(Map<String, VcsRepoTO> repo) {
        this.repo = repo
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "Vcs{" +
                "revisionControl='" + revisionControl + '\'' +
                ", repo=" + repo +
                '}';
    }
}
