package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.model.BuildServiceType

enum JenkinsBuildParams {

    // expected always to be overridden

    REPO(["repo", "vcs.repo.service.name", "service.name"], ""),

    // other, expected to be in .yml file

    VERSION(["version"], "1.0"),

    SERVICE_NAME(["service.name"], ""),
    SERVICE_BUILD_TYPE(["service.buildType"], BuildServiceType.MAVEN),
    SERVICE_BUILD_NUMBER_FOR_VERSION(["service.useBuildNumberForVersion"], false),
    SERVICE_REPO(["service.vcs.repo"], false)

    def defaultValue

    List<String> keys = new ArrayList<>()

    JenkinsBuildParams(List<String> keys, def defaultValue){
        this.keys = keys
        this.defaultValue = defaultValue
    }

}
