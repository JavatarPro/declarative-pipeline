package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

@Deprecated
class CacheRequest implements Serializable {

    Map<String, List<String>> caches = new HashMap<>()

    CacheRequest() {
        Logger.debug("CacheRequest:default constructor")
    }

    Map<String, List<String>> getCaches() {
        return caches
    }

    void setCaches(Map<String, List<String>> caches) {
        this.caches = caches
    }

    CacheRequest withCaches(Map<String, List<String>> caches) {
        this.caches = caches
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "CacheRequest{" +
                "caches=" + caches +
                '}';
    }

}
