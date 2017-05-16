package info.yannxia.java.gaze;

import java.util.concurrent.TimeUnit;

/**
 * Created by yann on 2017/5/15.
 */
public class TimeoutableCacheParam<T> {

    private TimeoutableCacheParam() {
    }

    private String key;
    private T item;
    private TimeUnit timeoutTimeUnit;
    private Long timeout;
    private String cacheName = CacheStore.DEFAULT_CACHE_NAME;

    public String getKey() {
        return key;
    }

    public T getItem() {
        return item;
    }

    public TimeUnit getTimeoutTimeUnit() {
        return timeoutTimeUnit;
    }

    public Long getTimeout() {
        return timeout;
    }

    public String getCacheName() {
        return cacheName;
    }

    public static final class CacheParamBuilder<T> {
        private String key;
        private T item;
        private TimeUnit timeoutTimeUnit;
        private Long timeout;
        private String cacheName;

        private CacheParamBuilder() {
        }

        public static CacheParamBuilder aCacheParam() {
            return new CacheParamBuilder();
        }

        public CacheParamBuilder withKey(String key) {
            this.key = key;
            return this;
        }

        public CacheParamBuilder withItem(T item) {
            this.item = item;
            return this;
        }

        public CacheParamBuilder withTimeoutTimeUnit(TimeUnit timeoutTimeUnit) {
            this.timeoutTimeUnit = timeoutTimeUnit;
            return this;
        }

        public CacheParamBuilder withTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public CacheParamBuilder withCacheName(String cacheName) {
            this.cacheName = cacheName;
            return this;
        }

        public TimeoutableCacheParam<T> build() {
            TimeoutableCacheParam<T> timeoutableCacheParam = new TimeoutableCacheParam<>();
            timeoutableCacheParam.timeout = this.timeout;
            timeoutableCacheParam.cacheName = this.cacheName;
            timeoutableCacheParam.item = this.item;
            timeoutableCacheParam.timeoutTimeUnit = this.timeoutTimeUnit;
            timeoutableCacheParam.key = this.key;
            return timeoutableCacheParam;
        }
    }
}
