package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.util.Logger

class Docker implements Serializable {

    Map<Environment, DockerRegistry> dockerRegistries = new HashMap<>()

    String customDockerFileName = ""

    Docker() {
        Logger.debug("Docker:default constructor")
    }

    Map<String, DockerRegistry> getDockerRegistries() {
        return dockerRegistries
    }

    void setDockerRegistries(Map<Environment, DockerRegistry> dockerRegistries) {
        this.dockerRegistries = dockerRegistries
    }

    Docker withDockerRegistries(Map<Environment, DockerRegistry> dockerRegistries) {
        this.dockerRegistries = dockerRegistries
        return this
    }

    Docker addDockerRegistries(String env, DockerRegistry dockerRegistry) {
        Environment environment = new Environment(env);
        this.dockerRegistries.put(environment, dockerRegistry)
        return this
    }

    Docker addDockerRegistries(Environment env, DockerRegistry dockerRegistry) {
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
