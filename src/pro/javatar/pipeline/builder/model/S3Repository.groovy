package pro.javatar.pipeline.builder.model

class S3Repository {

    String region

    String credentialsId

    String bucket

    boolean envFolder = true

    String getRegion() {
        return region
    }

    void setRegion(String region) {
        this.region = region
    }

    S3Repository withRegion(String region) {
        this.region = region
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    S3Repository withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getBucket() {
        return bucket
    }

    void setBucket(String bucket) {
        this.bucket = bucket
    }

    S3Repository withBucket(String bucket) {
        this.bucket = bucket
        return this
    }

    boolean hasEnvFolder() {
        return envFolder
    }

    void setEnvFolder(boolean envFolder) {
        this.envFolder = envFolder
    }

    S3Repository withEnvFolder(boolean envFolder) {
        this.envFolder = envFolder
        return this
    }

    @Override
    String toString() {
        return "S3Repository {" +
                "region='" + region + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", bucket='" + bucket + '\'' +
                ", envFolder='" + envFolder + '\'' +
                '}'
    }
}
