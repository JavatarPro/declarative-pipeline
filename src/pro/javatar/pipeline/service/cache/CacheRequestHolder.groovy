package pro.javatar.pipeline.service.cache

class CacheRequestHolder {

    // Map<service, List<folder path>>
    private static Map<String, List<String>> caches = new HashMap<>()

    static List<String> getCaches(String service) {
        return caches.get(service)
    }

    static void setCaches(Map<String, List<String>> cacheMap) {
        caches.putAll(cacheMap)
    }

    static CacheRequestHolder addCache(String service, String folder) {
        caches.put(folder)
        return this
    }

    @Override
    public String toString() {
        return "CacheRequestHolder{" +
                "caches=" + caches +
                '}';
    }
}
