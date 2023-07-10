package ie.carneyd.hibernate.storemanagement.cache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

class CacheItemTest {

	@Test
	void testLastUsedCreated() {
		CacheItem item = new CacheItem("url", Integer.getInteger("1"));
		assertTrue(item.getLastUsed().isAfter(LocalDateTime.now().minusMinutes(1)));
	}

	@Test
	void testCreatedCreated() {
		CacheItem item = new CacheItem("url", Integer.getInteger("1"));
		assertTrue(item.getCreated().isAfter(LocalDateTime.now().minusMinutes(1)));
	}

	@Test
	void testLastUsedNotRecent() {
		CacheItem item = new CacheItem("url", Integer.getInteger("1"));
		assertFalse(item.getLastUsed().isAfter(LocalDateTime.now()));
	}

	@Test
	void testLastUsedRecent() {
		CacheItem item = new CacheItem("url", Integer.getInteger("1"));
		LocalDateTime beforeUsingAgain = LocalDateTime.now();
		try {
			Thread.sleep(2000);
		} catch(InterruptedException ie) {}
		item.usedCacheItem();
		assertTrue(item.getLastUsed().isAfter(beforeUsingAgain));
	}

}
