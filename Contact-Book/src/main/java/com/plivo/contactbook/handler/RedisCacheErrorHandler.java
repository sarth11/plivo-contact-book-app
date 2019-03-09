package com.plivo.contactbook.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;


public class RedisCacheErrorHandler implements CacheErrorHandler {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		logger.debug("Cache {} is down to search for key :{} with exception :{}", cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		logger.debug("Cache {} is down to put for key :{} with exception :{}", cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		logger.debug("Cache {} is down to evict for key :{} with exception :{}", cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		logger.debug("Cache {} is down to clear with exception :{}", cache.getName(), exception.getMessage());
	}
}
