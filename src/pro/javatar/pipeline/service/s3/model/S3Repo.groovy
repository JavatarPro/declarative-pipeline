/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.s3.model

/**
 * @author Borys Zora
 * @version 2018-10-15
 */
class S3Repo {

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

    S3Repo withRegion(String region) {
        this.region = region
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    S3Repo withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getBucket() {
        return bucket
    }

    void setBucket(String bucket) {
        this.bucket = bucket
    }

    S3Repo withBucket(String bucket) {
        this.bucket = bucket
        return this
    }

    boolean hasEnvFolder() {
        return envFolder
    }

    void setEnvFolder(boolean envFolder) {
        this.envFolder = envFolder
    }

    S3Repo withEnvFolder(boolean envFolder) {
        this.envFolder = envFolder
        return this
    }

    @Override
    String toString() {
        return "S3Repo{" +
                "region='" + region + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", bucket='" + bucket + '\'' +
                ", envFolder='" + envFolder + '\'' +
                '}'
    }

}
