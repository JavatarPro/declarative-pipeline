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

    String getBuildType() {
        return buildType
    }

    void setBuildType(String buildType) {
        this.buildType = buildType
    }

    boolean getUseBuildNumberForVersion() {
        return useBuildNumberForVersion
    }

    void setUseBuildNumberForVersion(boolean useBuildNumberForVersion) {
        this.useBuildNumberForVersion = useBuildNumberForVersion
    }

    VcsRepo getRepo() {
        return repo
    }

    void setRepo(VcsRepo repo) {
        this.repo = repo
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
