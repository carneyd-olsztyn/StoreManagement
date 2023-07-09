package ie.carneyd.hibernate.storemanagement.cache;

import java.time.LocalDateTime;
import java.util.Hashtable;

import org.springframework.stereotype.Service;

@Service
public class Cache {

	private final int CAPACITY = 10;
	private Hashtable<String, CacheItem> cache = new Hashtable<>();

	public Cache() {
		super();
	}
	
	public boolean isCacheFull() {
		return cache.keySet().size() >= CAPACITY;
	}
	
	public int getCAPACITY() {
		return CAPACITY;
	}

	public CacheItem retrieveIfExistsAndNewerThan2Hours(String url) {
		if(cache.containsKey(url)) {
			
			CacheItem item = cache.get(url);
			
			if(item.getLastUsed().isBefore(LocalDateTime.now().minusHours(2))) {
				return null;
			}
			
			item.usedCacheItem();
			
			return item;
		}
		
		return null;
	}
	
	public void insertToCache(String url, CacheItem cacheItem) throws Exception {
		if(isCacheFull()) {
			// Remove the oldest item
			CacheItem firstCacheItem = cache.values()
				.stream()
				.sorted((item1, item2) -> 
							item1.getLastUsed().isBefore(item2.getLastUsed()) ? -1 : 1)
				.findFirst()
				.orElseThrow(() -> new Exception("Cache is Full, it cannot be empty"));
			
			cache.remove(firstCacheItem.getUrl());
			
			cache.put(url, cacheItem);
		} else {
			cache.put(url, cacheItem);
		}
	}
	
}
