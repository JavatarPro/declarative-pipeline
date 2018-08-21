package pro.javatar.pipeline.builder.model

class S3 {

    String region

    String credentialsId

    String bucket

    List<String> env = new ArrayList<>()

    String getRegion() {
        return region
    }

    void setRegion(String region) {
        this.region = region
    }

    S3 withRegion(String region) {
        this.region = region
        return this
    }

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    S3 withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getBucket() {
        return bucket
    }

    void setBucket(String bucket) {
        this.bucket = bucket
    }

    S3 withBucket(String bucket) {
        this.bucket = bucket
        return this
    }

    List<String> getEnv() {
        return env
    }

    void setEnv(List<String> env) {
        this.env = env
    }

    S3 withEnv(List<String> env) {
        this.env = env
        return this
    }
    
    @Override
    public String toString() {
        return "S3{" +
                "region='" + region + '\'' +
                ", credentialsId='" + credentialsId + '\'' +
                ", bucket='" + bucket + '\'' +
                ", env=" + env +
                '}';
    }
}
