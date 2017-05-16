package info.yannxia.java.gaze;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.concurrent.Callable;

/**
 * Created by yann on 2017/5/15.
 * imp for CacheStore
 */
public class CacheStoreImpl implements CacheStore {

    protected final CacheManager cacheManager;

    public CacheStoreImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> T get(String key, String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        return (T) cache.get(key).get();
    }

    @Override
    public <T> T get(String key, String cacheName, Callable<T> loadItem) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache.get(key, loadItem);
    }

    @Override
    public <T> T put(String key, String cacheName, T item) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(key, item);
        return item;
    }
}
