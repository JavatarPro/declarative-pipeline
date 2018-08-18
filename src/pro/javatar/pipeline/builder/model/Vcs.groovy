package pro.javatar.pipeline.builder.model

class Vcs {

    String revisionControl

    Map<String, Repo> repo

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

    Map<String, Repo> getRepo() {
        return repo
    }

    void setRepo(Map<String, Repo> repo) {
        this.repo = repo
    }

    Vcs withRepo(Map<String, Repo> repo) {
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
