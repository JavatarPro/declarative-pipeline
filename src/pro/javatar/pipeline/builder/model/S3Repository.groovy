package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

@Deprecated
class S3Repository implements Serializable {

    String region

    String credentialsId

    String bucket

    boolean envFolder = true

    S3Repository() {
        Logger.debug("S3Repository:default constructor")
    }

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

    @NonCPS
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
