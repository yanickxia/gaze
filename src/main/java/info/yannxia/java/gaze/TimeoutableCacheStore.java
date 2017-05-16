package info.yannxia.java.gaze;

import java.util.concurrent.Callable;

/**
 * Created by yann on 2017/5/15.
 */
public interface TimeoutableCacheStore extends CacheStore {
    <T> T get(TimeoutableCacheParam<T> cacheParam, Callable<T> loadItem);

    <T> T put(TimeoutableCacheParam<T> cacheParam);
}
