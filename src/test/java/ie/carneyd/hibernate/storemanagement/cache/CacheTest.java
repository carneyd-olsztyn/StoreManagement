package ie.carneyd.hibernate.storemanagement.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CacheTest {

	@Test
	void testCacheEmpty() {
		Cache cache = new Cache();
		assertFalse(cache.isCacheFull());
	}

	@Test
	void testCacheFull() {
		Cache cache = new Cache();
		CacheItem item = new CacheItem("url", Integer.getInteger("1"));
		
		try {
			for(int i = 0; i < cache.getCAPACITY(); i++ ) {
				cache.insertToCache("URL_"+i, item);
			}
		} catch(Exception e) {
			fail("Error with insertions");
		}
		assertTrue(cache.isCacheFull());
	}

	@Test
	void testCacheInsertBeyondFull() {
		Cache cache = new Cache();
		
		try {
			for(int i = 0; i <= cache.getCAPACITY(); i++ ) {
				CacheItem item = new CacheItem("URL_"+i, Integer.getInteger("1"));
				cache.insertToCache("URL_"+i, item);
				Thread.sleep(1000);
			}
		} catch(Exception e) {
			fail("Error with insertions");
		}

		assertFalse(cache.cacheContainsKey("URL_0"));
	}
	
	@Test
	void testInsertToCache() {
		String url = "url";
		
		Cache cache = new Cache();
		CacheItem item = new CacheItem(url, Integer.getInteger("1"));
		
		try {
			cache.insertToCache(url, item);
		} catch(Exception e) {
			fail();
		}
		
		assertEquals(1, cache.usedCacheSize());
	}
	
	@Test
	void testCacheContainsKey() {
		String url = "url";
		
		Cache cache = new Cache();
		CacheItem item = new CacheItem(url, Integer.getInteger("1"));
		
		try {
			cache.insertToCache(url, item);
		} catch(Exception e) {
			fail();
		}
		
		assertTrue(cache.cacheContainsKey(url));
	}
	
	@Test
	void testRetrieveIfExistsAndNewerThan2Hours_NewerThan2Hours() {
		String url = "url";
		
		Cache cache = new Cache();
		CacheItem item = new CacheItem(url, Integer.getInteger("1"));
		
		try {
			cache.insertToCache(url, item);
		} catch(Exception e) {
			fail();
		}

		assertEquals(item, cache.retrieveIfExistsAndNewerThan2Hours(url));
	}
	
	@Test
	void testRetrieveIfExistsAndNewerThan2Hours_OlderThan2Hours() {
		String url = "url";
		
		Cache cache = new Cache();
		CacheItem item = new CacheItem(url, Integer.getInteger("1"));
		
		try {
			item.setLastUsed(item.getLastUsed().minusHours(4));
			cache.insertToCache(url, item);
		} catch(Exception e) {
			fail();
		}

		assertNull(cache.retrieveIfExistsAndNewerThan2Hours(url));
	}

}
