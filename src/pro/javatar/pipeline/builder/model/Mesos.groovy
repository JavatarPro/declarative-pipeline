package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

@Deprecated
class Mesos implements Serializable {

    Map<String, VcsRepoTO> vcsConfigRepos = new HashMap<>()

    Mesos() {
        Logger.debug("Mesos:default constructor")
    }

    Map<String, VcsRepoTO> getVcsConfigRepos() {
        return vcsConfigRepos
    }

    void setVcsConfigRepos(Map<String, VcsRepoTO> vcsConfigRepos) {
        this.vcsConfigRepos = vcsConfigRepos
    }

    Mesos withVcsConfigRepos(Map<String, VcsRepoTO> vcsConfigRepos) {
        this.vcsConfigRepos = vcsConfigRepos
        return this
    }

    void addVcsConfigRepo(String env, VcsRepoTO repoName) {
        this.vcsConfigRepos.put(env, repoName)
    }

    @NonCPS
    @Override
    public String toString() {
        return "Mesos{" +
                "vcsConfigRepos=" + vcsConfigRepos +
                '}';
    }
}
