package pro.javatar.pipeline.service.orchestration.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.service.vcs.model.VcsRepo

import java.time.Period

import static pro.javatar.pipeline.util.StringUtils.isBlank

class NomadBO {

    String env

    String url

    Period deploymentTimeout

    VcsRepo vcsRepo

    String nomadFolder

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

    void setDeploymentTimeout(String deploymentTimeout) {
        this.deploymentTimeout = Period.parse(deploymentTimeout)
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

    String getNomadFolder() {
        if (isBlank(nomadFolder)) {
            return "../${getVcsRepo().getName()}"
        }
        return nomadFolder
    }

    void setNomadFolder(String nomadFolder) {
        this.nomadFolder = nomadFolder
    }

    NomadBO withNomadFolder(String nomadFolder) {
        this.nomadFolder = nomadFolder
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "NomadBO{" +
                "env='" + env + '\'' +
                ", url='" + url + '\'' +
                ", deploymentTimeout=" + deploymentTimeout +
                ", nomadFolder=" + nomadFolder +
                ", vcsRepo=" + vcsRepo.toString() +
                '}';
    }
}
