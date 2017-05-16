package info.yannxia.java.gaze;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by yann on 2017/5/15.
 * timeout able impl
 */
public class TimeoutableCacheStoreImpl extends CacheStoreImpl implements TimeoutableCacheStore {

    public TimeoutableCacheStoreImpl(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(TimeoutableCacheParam<T> cacheParam, Callable<T> loadItem) {
        if (cacheManager instanceof RedisCacheManager) {
            RedisCache redisCache = (RedisCache) cacheManager.getCache(cacheParam.getCacheName());
            Cache.ValueWrapper val = redisCache.get(cacheParam.getKey());
            if (val != null) {
                return (T) val.get();
            }

            try {
                TimeoutableCacheParam<T> timeoutableCacheParam = TimeoutableCacheParam.CacheParamBuilder.<T>aCacheParam()
                        .withCacheName(cacheParam.getCacheName())
                        .withItem(loadItem.call())
                        .withKey(cacheParam.getKey())
                        .withTimeout(cacheParam.getTimeout())
                        .withTimeoutTimeUnit(cacheParam.getTimeoutTimeUnit())
                        .build();
                this.put(timeoutableCacheParam);
            } catch (Exception e) {
                throw new GazeRuntimeException("can't loadItem", e);
            }
        }

        throw new GazeRuntimeException("just support redis cache");
    }

    @Override
    public <T> T put(TimeoutableCacheParam<T> cacheParam) {
        if (cacheManager instanceof RedisCacheManager) {
            RedisCache redisCache = (RedisCache) cacheManager.getCache(cacheParam.getCacheName());
            RedisCacheElement redisCacheElement;
            try {
                redisCacheElement = redisCacheElement(redisCache, cacheParam);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new GazeRuntimeException("can't create CacheElement", e);
            }

            redisCache.put(redisCacheElement);
        }

        return cacheParam.getItem();
    }


    private RedisCacheElement redisCacheElement(RedisCache redisCache,
                                                TimeoutableCacheParam cacheParam) throws InvocationTargetException, IllegalAccessException {

        Method getRedisCacheKeyMethod = ReflectionUtils
                .findMethod(RedisCache.class, "getRedisCacheKey");
        getRedisCacheKeyMethod.invoke(redisCache, cacheParam.getKey());
        RedisCacheKey redisCacheKey = (RedisCacheKey) getRedisCacheKeyMethod.invoke(redisCache, cacheParam.getKey());

        Method toStoreValueMethod = ReflectionUtils
                .findMethod(RedisCache.class, "toStoreValue");
        toStoreValueMethod.setAccessible(true);
        Object redisCacheObj = toStoreValueMethod.invoke(redisCache, cacheParam.getItem());

        return new RedisCacheElement(redisCacheKey, redisCacheObj)
                .expireAfter(cacheParam.getTimeoutTimeUnit().toSeconds(cacheParam.getTimeout()));
    }

}
