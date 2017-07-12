package gs.gemfire.lookaside.example;

public class StubCacheManager implements CacheManager {

    public boolean putIntoRegionWasCalled = false;

    public StubCacheManager() {
    }

    public void createCache(String regionName) {
    }

    public String getFromRegion(String key) {
        if (key.equals("someone")) return "somesong";

        return null;
    }

    public void putIntoRegion(String key, String value) {
        putIntoRegionWasCalled = true;
    }

    public void close() {
    }

    public boolean isPutIntoRegionCalled() {
        return putIntoRegionWasCalled;
    }
}
