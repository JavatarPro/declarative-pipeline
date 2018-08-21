package pro.javatar.pipeline.builder.model

import static pro.javatar.pipeline.util.Utils.isBlank

class Vcs {

    String revisionControl

    Map<String, VcsRepo> repo

    Vcs populateRevisionControl() {
        repo.values().each { value ->
            if (isBlank(value.getRevisionControl())) {
                value.setRevisionControl(this.revisionControl)
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

    Map<String, VcsRepo> getRepo() {
        return repo
    }

    void setRepo(Map<String, VcsRepo> repo) {
        this.repo = repo
    }

    Vcs withRepo(Map<String, VcsRepo> repo) {
        this.repo = repo
        return this
    }

    @Override
    public String toString() {
        return "Vcs{" +
                "revisionControl='" + revisionControl + '\'' +
                ", repo=" + repo +
                '}';
    }
}
