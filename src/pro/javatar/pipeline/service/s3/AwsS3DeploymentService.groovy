package pro.javatar.pipeline.service.s3

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.DeploymentService
import pro.javatar.pipeline.service.s3.model.S3Repo
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 4/12/18 22:58
 */
class AwsS3DeploymentService implements DeploymentService {

    Map<String, S3Repo> s3RepoMap = new HashMap<>()

    AwsS3DeploymentService(Map<String, S3Repo> s3RepoMap) {
        this.s3RepoMap = s3RepoMap
    }

    @Override
    void deployArtifact(Env environment, ReleaseInfo releaseInfo) {
        if (environment != Env.PROD) {
            deployToS3Bucket(releaseInfo, environment)
            return
        }
        deployToS3Bucket(releaseInfo, environment)
    }

    def deployToS3Bucket(ReleaseInfo releaseInfo, Env environment) {
        Logger.info("AwsS3DeploymentService:deployToS3Bucket:started")
        S3Repo s3Repo = s3RepoMap.get(environment.getValue())
        String region = s3Repo.getRegion()
        String credentials = s3Repo.getCredentialsId()
        String bucket = s3Repo.getBucket()
        String s3Path = getS3Path(releaseInfo, environment, s3Repo)
        String s3LatestPath = getS3LatestPath(releaseInfo, environment, s3Repo)
        dsl.withAWS(region:region, credentials: credentials) {
            dsl.s3Delete bucket: bucket, path: s3Path
            dsl.s3Upload bucket: bucket, file: "${releaseInfo.getUiDistributionFolder()}/",  path: s3Path
            dsl.s3Delete bucket: bucket, path: s3LatestPath
            dsl.s3Upload bucket: bucket, file: "${releaseInfo.getUiDistributionFolder()}/",  path: s3LatestPath
            def files = dsl.s3FindFiles bucket: bucket, path: s3Path, glob: "**", onlyFiles: true
            Logger.info("AwsS3DeploymentService:deployToS3Bucket:files: " + files.toString())
        }
        Logger.info("AwsS3DeploymentService:deployToS3Bucket:finished")
    }

    def getS3Path(ReleaseInfo releaseInfo, Env env, S3Repo s3Repo) {
        if (s3Repo.hasEnvFolder()) {
            return "${env.getValue()}/${releaseInfo.getServiceName()}/${releaseInfo.releaseVersion()}/"
        }
        return "${releaseInfo.getServiceName()}/${releaseInfo.releaseVersion()}/"
    }

    def getS3LatestPath(ReleaseInfo releaseInfo, Env env, S3Repo s3Repo) {
        if (s3Repo.hasEnvFolder()) {
            return "${env.getValue()}/${releaseInfo.getServiceName()}/latest/"
        }
        return "${releaseInfo.getServiceName()}/latest/"
    }

}
