package info.yannxia.java.gaze;

import java.util.concurrent.Callable;

/**
 * Created by yann on 2017/5/15.
 * Interface for cache store
 */
public interface CacheStore {

    String DEFAULT_CACHE_NAME = "gaze_cache";

    /**
     * try get item from cache, if not exist to null
     *
     * @param key key
     * @return target item
     */
    default <T> T get(String key) {
        return get(key, DEFAULT_CACHE_NAME);
    }

    /**
     * try get item from cache,special cache name, if not exist to null
     *
     * @param key       key
     * @param cacheName cache name
     * @return target item
     */
    <T> T get(String key, String cacheName);

    /**
     * try get item from cache, if not exist invoke loadItem Function
     *
     * @param key key
     * @return target item
     */
    default <T> T get(String key, Callable<T> loadItem) {
        return get(key, DEFAULT_CACHE_NAME, loadItem);
    }

    /**
     * try get item from cache, special cache name, if not exist invoke loadItem Function
     *
     * @param key       key
     * @param cacheName cache name
     * @param loadItem  item
     * @return target item
     */
    <T> T get(String key, String cacheName, Callable<T> loadItem);

    /**
     * save item to save
     *
     * @param key  key
     * @param item item
     * @return self item
     */
    default <T> T put(String key, T item) {
        return put(key, DEFAULT_CACHE_NAME, item);
    }


    /**
     * save item to save, special cache name
     *
     * @param key       key
     * @param cacheName cache name
     * @param item      item
     * @return item
     */
    <T> T put(String key, String cacheName, T item);
}
