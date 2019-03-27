package pro.javatar.pipeline.builder

import pro.javatar.pipeline.service.s3.AwsS3DeploymentService
import pro.javatar.pipeline.service.s3.model.S3Repo

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 4/13/18 12:14
 */
class S3Builder implements Serializable {

    Map<String, S3Repo> S3Repos = new HashMap<>()

    S3Builder() {
        dsl.echo "S3Builder default constructor"
    }

    AwsS3DeploymentService build() {
        return new AwsS3DeploymentService(S3Repos)
    }

    S3Builder addS3Repository(String env, S3Repo s3Repo) {
        S3Repos.put(env, s3Repo)
        return this
    }

    S3Builder addS3Repository(String env, String region, String credentialsId, String bucket, boolean envFolder) {
        S3Repos.put(env, new S3Repo()
                .withRegion(region)
                .withCredentialsId(credentialsId)
                .withBucket(bucket)
                .withEnvFolder(envFolder))
        return this
    }

    @Override
    public String toString() {
        return "S3Builder{" +
                "S3Repos=" + S3Repos +
                '}';
    }
}
