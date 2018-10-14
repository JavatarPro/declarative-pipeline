package pro.javatar.pipeline.builder.model

class AutoTest {

    String jobName

    boolean skipSystemTests

    boolean skipCodeQualityVerification

    int sleepInSeconds = -1

    String getJobName() {
        return jobName
    }

    void setJobName(String jobName) {
        this.jobName = jobName
    }

    AutoTest withJobName(String jobName) {
        this.jobName = jobName
        return this
    }

    boolean getSkipSystemTests() {
        return skipSystemTests
    }

    void setSkipSystemTests(boolean skipSystemTests) {
        this.skipSystemTests = skipSystemTests
    }

    AutoTest withSkipSystemTests(boolean skipSystemTests) {
        this.skipSystemTests = skipSystemTests
        return this
    }

    AutoTest withSkipSystemTests(String skipSystemTests) {
        this.skipSystemTests = Boolean.valueOf(skipSystemTests)
        return this
    }

    boolean getSkipCodeQualityVerification() {
        return skipCodeQualityVerification
    }

    void setSkipCodeQualityVerification(boolean skipCodeQualityVerification) {
        this.skipCodeQualityVerification = skipCodeQualityVerification
    }

    AutoTest withSkipCodeQualityVerification(boolean skipCodeQualityVerification) {
        this.skipCodeQualityVerification = skipCodeQualityVerification
        return this
    }

    AutoTest withSkipCodeQualityVerification(String skipCodeQualityVerification) {
        this.skipCodeQualityVerification = Boolean.valueOf(skipCodeQualityVerification)
        return this
    }

    int getSleepInSeconds() {
        return sleepInSeconds
    }

    void setSleepInSeconds(int sleepInSeconds) {
        this.sleepInSeconds = sleepInSeconds
    }

    AutoTest withSleepInSeconds(int sleepInSeconds) {
        this.sleepInSeconds = sleepInSeconds
        return this
    }

    @Override
    public String toString() {
        return "AutoTest{" +
                "jobName='" + jobName + '\'' +
                ", skipSystemTests=" + skipSystemTests +
                ", skipCodeQualityVerification=" + skipCodeQualityVerification +
                ", sleepInSeconds=" + sleepInSeconds +
                '}';
    }
}
