package pro.javatar.pipeline.builder

class Docker {

    String credentialsId

    String registry

    List<String> env = new ArrayList<>()

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    Docker withCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
        return this
    }

    String getRegistry() {
        return registry
    }

    void setRegistry(String registry) {
        this.registry = registry
    }

    Docker withRegistry(String registry) {
        this.registry = registry
        return this
    }

    List<String> getEnv() {
        return env
    }

    void setEnv(List<String> env) {
        this.env = env
    }

    Docker withEnv(List<String> env) {
        this.env = env
        return this
    }

    Docker addEnv(String env) {
        this.env.add(env)
        return this
    }

    @Override
    public String toString() {
        return "Docker{" +
                "credentialsId='" + credentialsId + '\'' +
                ", registry='" + registry + '\'' +
                ", env=" + env +
                '}';
    }
}
