package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS

import java.time.Period

class NomadItem {

    String url

    String vcsConfig

    Period deploymentTimeout

    String getUrl() {
        return url
    }

    void setUrl(String url) {
        this.url = url
    }

    NomadItem withUrl(String url) {
        setUrl(url)
        return this
    }

    String getVcsConfig() {
        return vcsConfig
    }

    void setVcsConfig(String vcsConfig) {
        this.vcsConfig = vcsConfig
    }

    NomadItem withVcsConfig(String vcsConfig) {
        setVcsConfig(vcsConfig)
        return this
    }

    Period getDeploymentTimeout() {
        return deploymentTimeout
    }

    void setDeploymentTimeout(Period deploymentTimeout) {
        this.deploymentTimeout = deploymentTimeout
    }

    NomadItem withDeploymentTimeout(Period deploymentTimeout) {
        setDeploymentTimeout(deploymentTimeout)
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "NomadItem{" +
                "url='" + url + '\'' +
                ", vcsConfig='" + vcsConfig + '\'' +
                ", deploymentTimeout=" + deploymentTimeout +
                '}';
    }
}
