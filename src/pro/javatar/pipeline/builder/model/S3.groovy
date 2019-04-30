package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.util.Logger

class S3 implements Serializable {

    Map<String, S3Repository> S3Repositories = new HashMap<>()

    S3() {
        Logger.debug("S3:default constructor")
    }

    Map<String, S3Repository> getS3Repositories() {
        return S3Repositories
    }

    void setS3Repositories(Map<String, S3Repository> s3Repositories) {
        S3Repositories = s3Repositories
    }

    S3 withS3Repositories(Map<String, S3Repository> s3Repositories) {
        S3Repositories = s3Repositories
        return this
    }

    S3 addS3Repository(String env, S3Repository s3Repository) {
        S3Repositories.put(env, s3Repository)
        return this
    }

    @Override
    public String toString() {
        return "S3{" +
                "S3Repositories=" + S3Repositories +
                '}';
    }
}
