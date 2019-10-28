package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

class AutoTest implements Serializable {

    String jenkinsJobName

    Boolean shouldSkipSystemTests

    Boolean shouldSkipCodeQualityVerification

    Integer sleepInSecondsAmount = -1

    AutoTest() {
        Logger.debug("AutoTest:default constructor")
    }

    String getJobName() {
        return jenkinsJobName
    }

    void setJobName(String jobName) {
        this.jenkinsJobName = jobName
    }

    AutoTest withJobName(String jobName) {
        setJobName(jobName)
        return this
    }

    Boolean getSkipSystemTests() {
        return shouldSkipSystemTests;
    }

    void setSkipSystemTests(Boolean skipSystemTests) {
        this.shouldSkipSystemTests = skipSystemTests
    }

    AutoTest withSkipSystemTests(Boolean skipSystemTests) {
        setSkipSystemTests(skipSystemTests)
        return this
    }

    AutoTest withSkipSystemTests(String skipSystemTests) {
        setSkipSystemTests(Boolean.valueOf(skipSystemTests))
        return this
    }

    Boolean getSkipCodeQualityVerification() {
        return shouldSkipCodeQualityVerification
    }

    void setSkipCodeQualityVerification(Boolean skipCodeQualityVerification) {
        this.shouldSkipCodeQualityVerification = skipCodeQualityVerification
    }

    AutoTest withSkipCodeQualityVerification(Boolean skipCodeQualityVerification) {
        setSkipCodeQualityVerification(skipCodeQualityVerification);
        return this
    }

    AutoTest withSkipCodeQualityVerification(String skipCodeQualityVerification) {
        setSkipCodeQualityVerification(Boolean.valueOf(skipCodeQualityVerification));
        return this
    }

    Integer getSleepInSeconds() {
        return sleepInSecondsAmount
    }

    void setSleepInSeconds(Integer sleepInSeconds) {
        this.sleepInSecondsAmount = sleepInSeconds
    }

    AutoTest withSleepInSeconds(Integer sleepInSeconds) {
        setSleepInSeconds(sleepInSeconds);
        return this
    }

    @NonCPS
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
