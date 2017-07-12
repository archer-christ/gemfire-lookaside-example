package gs.gemfire.lookaside.example;

public interface CacheManager {

    void createCache(String regionName);

    String getFromRegion(String key);

    void putIntoRegion(String key, String value);

    void close();
}
