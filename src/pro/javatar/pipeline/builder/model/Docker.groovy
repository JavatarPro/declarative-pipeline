package pro.javatar.pipeline.builder.model

class Docker {

    Map<Environment, DockerRegistry> dockerRegistries = new HashMap<>()

    String customDockerFileName = ""

    Map<String, DockerRegistry> getDockerRegistries() {
        return dockerRegistries
    }

    void setDockerRegistries(Map<String, DockerRegistry> dockerRegistries) {
        this.dockerRegistries = dockerRegistries
    }

    Docker withDockerRegistries(Map<String, DockerRegistry> dockerRegistries) {
        this.dockerRegistries = dockerRegistries
        return this
    }

    Docker addDockerRegistries(String env, DockerRegistry dockerRegistry) {
        this.dockerRegistries.put(env, dockerRegistry)
        return this
    }

    String getCustomDockerFileName() {
        return customDockerFileName
    }

    void setCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
    }

    Docker withCustomDockerFileName(String customDockerFileName) {
        this.customDockerFileName = customDockerFileName
        return this
    }


    @Override
    public String toString() {
        return "Docker{" +
                "dockerRegistries=" + dockerRegistries +
                ", customDockerFileName='" + customDockerFileName + '\'' +
                '}';
    }
}
