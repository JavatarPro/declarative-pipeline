package pro.javatar.pipeline.builder.model

class Mesos {

    Map<String, VcsRepoTO> vcsConfigRepos = new HashMap<>()

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

    @Override
    public String toString() {
        return "Mesos{" +
                "vcsConfigRepos=" + vcsConfigRepos +
                '}';
    }
}
