package az.xalqbank.msdocumentupload.Integrations;

import java.time.Duration;

public interface RedisCacheService {
    /**
     * Caches the given data with a specified key.
     *
     * @param key the key to associate with the cached data.
     * @param data the data to cache.
     * @param ttl the time-to-live for the cached data.
     */
    void cacheData(String key, Object data, Duration ttl);
}
