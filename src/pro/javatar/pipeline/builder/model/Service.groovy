package pro.javatar.pipeline.builder.model

class Service {

    String name

    String buildType

    boolean useBuildNumberForVersion

    VcsRepo repo

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

    Service withUseBuildNumberForVersion(boolean useBuildNumberForVersion) {
        this.useBuildNumberForVersion = useBuildNumberForVersion
        return this
    }

    VcsRepo getRepo() {
        return repo
    }

    void setRepo(VcsRepo repo) {
        this.repo = repo
    }

    Service withRepo(VcsRepo repo) {
        this.repo = repo
        return this
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", buildType='" + buildType + '\'' +
                ", useBuildNumberForVersion=" + useBuildNumberForVersion +
                ", repo=" + repo +
                '}';
    }
}
