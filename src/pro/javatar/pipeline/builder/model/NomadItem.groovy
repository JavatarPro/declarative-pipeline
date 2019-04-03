package pro.javatar.pipeline.builder.model

import java.time.Period

class NomadItem {

    String url

    String vcsConfig

    Period period

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

    Period getPeriod() {
        return period
    }

    void setPeriod(Period period) {
        this.period = period
    }

    NomadItem withPeriod(Period period) {
        setPeriod(period)
        return this
    }

    @Override
    public String toString() {
        return "NomadItem{" +
                "url='" + url + '\'' +
                ", vcsConfig='" + vcsConfig + '\'' +
                ", period=" + period +
                '}';
    }
}
