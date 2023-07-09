package ie.carneyd.hibernate.storemanagement.cache;

import java.time.LocalDateTime;

public class CacheItem {

	private String url;
	private LocalDateTime created;
	private LocalDateTime lastUsed;
	private Object value;

	
	public CacheItem(String url, Object value) {
		super();
		this.url = url;
		this.value = value;
		this.lastUsed = LocalDateTime.now();
		this.created = LocalDateTime.now();
	}


	public void usedCacheItem() {
		lastUsed = LocalDateTime.now();
	}

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDateTime getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(LocalDateTime lastUsed) {
		this.lastUsed = lastUsed;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

}
