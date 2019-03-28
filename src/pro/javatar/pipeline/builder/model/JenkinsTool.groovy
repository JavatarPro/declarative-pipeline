package pro.javatar.pipeline.builder.model

class JenkinsTool {

    String java

    String maven

    String gradle

    String npmVersion

    String npmType

    String getJava() {
        return java
    }

    void setJava(String java) {
        this.java = java
    }

    JenkinsTool withJava(String java) {
        this.java = java
        return this
    }

    String getMaven() {
        return maven
    }

    void setMaven(String maven) {
        this.maven = maven
    }

    JenkinsTool withMaven(String maven) {
        this.maven = maven
        return this
    }

    String getGradle() {
        return gradle
    }

    void setGradle(String gradle) {
        this.gradle = gradle
    }

    JenkinsTool withGradle(String gradle) {
        this.gradle = gradle
        return this
    }

    String getNpmVersion() {
        return npmVersion
    }

    void setNpmVersion(String npmVersion) {
        this.npmVersion = npmVersion
    }

    JenkinsTool withNpmVersion(String npmVersion) {
        this.npmVersion = npmVersion
        return this
    }

    String getNpmType() {
        return npmType
    }

    void setNpmType(String npmType) {
        this.npmType = npmType
    }

    JenkinsTool withNpmType(String npmType) {
        this.npmType = npmType
        return this
    }

    @Override
    public String toString() {
        return "JenkinsTool{" +
                "java='" + java + '\'' +
                ", maven='" + maven + '\'' +
                ", gradle='" + gradle + '\'' +
                ", npmVersion='" + npmVersion + '\'' +
                ", npmType='" + npmType + '\'' +
                '}';
    }
}
