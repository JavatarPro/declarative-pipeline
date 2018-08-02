package pro.javatar.pipeline.service.impl

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.DeploymentService
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 4/12/18 22:58
 */
class AwsS3DeploymentService implements DeploymentService {

    String region
    String credentials
    String bucketDev
    String bucketProd

    AwsS3DeploymentService(String region, String credentials, String bucketDev, String bucketProd) {
        this.region = region
        this.credentials = credentials
        this.bucketDev = bucketDev
        this.bucketProd = bucketProd
    }

    @Override
    void deployArtifact(Env environment, ReleaseInfo releaseInfo) {
        if (environment != Env.PROD) {
            deployToS3Bucket(releaseInfo, bucketDev, environment)
            return
        }
        deployToS3Bucket(releaseInfo, bucketProd, environment)
    }

    def deployToS3Bucket(ReleaseInfo releaseInfo, String bucket, Env environment) {
        String s3Path = getS3Path(releaseInfo, environment)
        dsl.withAWS(region:region, credentials: credentials) {
            dsl.s3Upload bucket: bucket, file: "${releaseInfo.getUiDistributionFolder()}/",  path: s3Path
            def files = dsl.s3FindFiles bucket: bucket, path: s3Path, glob: "**", onlyFiles: true
            dsl.echo "${files.toString()}"
        }
    }

    def getS3Path(ReleaseInfo releaseInfo, Env env) {
        return "${env.getValue()}/${releaseInfo.getServiceName()}/v/${releaseInfo.getReleaseVersion()}/"
    }

}
