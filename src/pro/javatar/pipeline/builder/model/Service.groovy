package pro.javatar.pipeline.builder.model

class Service {

    String name

    String buildType

    boolean useBuildNumberForVersion

    VcsRepoTO repo

    String vcsRepoId

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Service withName(String name) {
        this.name = name
        return this
    }

    String getBuildType() {
        return buildType
    }

    void setBuildType(String buildType) {
        this.buildType = buildType
    }

    Service withBuildType(String buildType) {
        this.buildType = buildType
        return this
    }

    boolean getUseBuildNumberForVersion() {
        return useBuildNumberForVersion
    }

    void setUseBuildNumberForVersion(boolean useBuildNumberForVersion) {
        this.useBuildNumberForVersion = useBuildNumberForVersion
    }

    Service withUseBuildNumberForVersion(Boolean useBuildNumberForVersion) {
        if (useBuildNumberForVersion == null) {
            this.useBuildNumberForVersion = false
        } else {
            this.useBuildNumberForVersion = useBuildNumberForVersion
        }
        return this
    }

    VcsRepoTO getRepo() {
        return repo
    }

    void setRepo(VcsRepoTO repo) {
        this.repo = repo
    }

    Service withRepo(VcsRepoTO repo) {
        this.repo = repo
        return this
    }

    String getVcsRepoId() {
        return vcsRepoId
    }

    void setVcsRepoId(String vcsRepoId) {
        this.vcsRepoId = vcsRepoId
    }

    Service withVcsRepoId(String vcsRepoId) {
        this.vcsRepoId = vcsRepoId
        return this
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", buildType='" + buildType + '\'' +
                ", useBuildNumberForVersion=" + useBuildNumberForVersion +
                ", repo=" + repo +
                ", vcsRepoId='" + vcsRepoId + '\'' +
                '}';
    }
}
