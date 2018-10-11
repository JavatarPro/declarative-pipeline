package pro.javatar.pipeline.builder.model

class CacheRequest {

    Map<String, List<String>> caches = new HashMap<>()

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

    @Override
    public String toString() {
        return "CacheRequest{" +
                "caches=" + caches +
                '}';
    }

}
