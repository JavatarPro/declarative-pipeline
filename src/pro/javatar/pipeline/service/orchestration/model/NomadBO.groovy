package pro.javatar.pipeline.service.orchestration.model

import pro.javatar.pipeline.service.vcs.model.VcsRepo

import java.time.Period

class NomadBO {

    String env

    String url

    Period deploymentTimeout

    VcsRepo vcsRepo

    String getEnv() {
        return env
    }

    void setEnv(String env) {
        this.env = env
    }

    NomadBO withEnv(String env) {
        setEnv(env)
        return this
    }

    String getUrl() {
        return url
    }

    void setUrl(String url) {
        this.url = url
    }

    NomadBO withUrl(String url) {
        setUrl(url)
        return this
    }

    Period getDeploymentTimeout() {
        return deploymentTimeout
    }

    void setDeploymentTimeout(Period deploymentTimeout) {
        this.deploymentTimeout = deploymentTimeout
    }

    NomadBO withDeploymentTimeout(Period deploymentTimeout) {
        setDeploymentTimeout(deploymentTimeout)
        return this
    }

    VcsRepo getVcsRepo() {
        return vcsRepo
    }

    void setVcsRepo(VcsRepo vcsRepo) {
        this.vcsRepo = vcsRepo
    }

    NomadBO withVcsRepo(VcsRepo vcsRepo) {
        setVcsRepo(vcsRepo)
        return this
    }

    @Override
    public String toString() {
        return "NomadBO{" +
                "env='" + env + '\'' +
                ", url='" + url + '\'' +
                ", deploymentTimeout=" + deploymentTimeout +
                ", vcsRepo=" + vcsRepo.toString() +
                '}';
    }
}
