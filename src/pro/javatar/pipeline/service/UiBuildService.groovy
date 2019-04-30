package pro.javatar.pipeline.service

abstract class UiBuildService extends BuildService {

    protected String distributionFolder = "dist"

    String getArtifact() {
        return "${distributionFolder}.zip"
    }

}
