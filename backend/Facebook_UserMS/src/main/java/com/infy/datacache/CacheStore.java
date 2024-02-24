package com.infy.datacache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheStore<T> {
	private Cache<String, T> cache;

	public CacheStore(int expiryDuration) {
		cache = CacheBuilder.newBuilder().expireAfterAccess(expiryDuration, TimeUnit.SECONDS)
				.concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
	}

	public T get(String key) {
		return cache.getIfPresent(key);
	}

	public void add(String key, T value) {
		if (key != null && value != null) {
			cache.put(key, value);
		}
	}

}
