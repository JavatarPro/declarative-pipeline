package pro.javatar.pipeline.builder

import pro.javatar.pipeline.service.impl.AwsS3DeploymentService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 4/13/18 12:14
 */
class S3Builder implements Serializable {

    String region

    String credentials

    String bucketDev

    String bucketProd

    S3Builder() {
        dsl.echo "S3Builder default constructor"
    }

    AwsS3DeploymentService build() {
        return new AwsS3DeploymentService(region, credentials, bucketDev, bucketProd)
    }

    String getRegion() {
        return region
    }

    void setRegion(String region) {
        this.region = region
    }

    S3Builder withRegion(String region) {
        this.region = region
        return this
    }

    String getCredentials() {
        return credentials
    }

    void setCredentials(String credentials) {
        this.credentials = credentials
    }

    S3Builder withCredentials(String credentials) {
        this.credentials = credentials
        return this
    }

    String getBucketDev() {
        return bucketDev
    }

    void setBucketDev(String bucketDev) {
        this.bucketDev = bucketDev
    }

    S3Builder withBucketDev(String bucketDev) {
        this.bucketDev = bucketDev
        return this
    }

    String getBucketProd() {
        return bucketProd
    }

    void setBucketProd(String bucketProd) {
        this.bucketProd = bucketProd
    }

    S3Builder withBucketProd(String bucketProd) {
        this.bucketProd = bucketProd
        return this
    }

    @Override
    public String toString() {
        return "S3Builder{" +
                "region='" + region + '\'' +
                ", credentials='" + credentials + '\'' +
                ", bucketDev='" + bucketDev + '\'' +
                ", bucketProd='" + bucketProd + '\'' +
                '}';
    }
}
