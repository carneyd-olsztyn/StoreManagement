package ie.carneyd.hibernate.storemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ie.carneyd.hibernate.storemanagement.cache.Cache;
import ie.carneyd.hibernate.storemanagement.cache.CacheItem;
import ie.carneyd.hibernate.storemanagement.item.Item;
import ie.carneyd.hibernate.storemanagement.repository.ItemDeleteRepository;
import ie.carneyd.hibernate.storemanagement.repository.ItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

@RestController
public class StoreManagementController {

	@Autowired
	ItemRepository repo;
	
	@Autowired
	ItemDeleteRepository deleteRepo;

	@Autowired
	Cache cache;

	@PostMapping(path="/item", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> createItem(@RequestBody Item item) {
		Item itemCreated;

		try {
			itemCreated = repo.save(item);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(itemCreated, HttpStatus.OK);
	}

	@PostMapping(path="/all-items", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Item>> createItem(@RequestBody List<Item> items) {
		Iterable<Item> itemsCreated;

		try {
			itemsCreated = repo.save(items);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(itemsCreated, HttpStatus.OK);
	}

	@GetMapping("/item/{id}")
	public Item getItem(HttpServletRequest req, @PathVariable("id") long id) throws Exception {
		// Check the cache for this address
		CacheItem item = cache.retrieveIfExistsAndNewerThan2Hours(req.getRequestURI());

		if (item != null) {
			return (Item) item.getValue();
		} else {
			Item retrievedItem = repo.findById(id).orElseThrow(() -> new Exception("Item does not exist"));

			item = new CacheItem(req.getRequestURI(), retrievedItem);

			cache.insertToCache(req.getRequestURI(), item);

			return retrievedItem;
		}
	}

	@GetMapping("/item/name/{name}")
	public Page<Item> getItemByName(HttpServletRequest req, @PathParam("name") String name, 
			@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) throws Exception {
		// Check the cache for this address
		CacheItem item = cache.retrieveIfExistsAndNewerThan2Hours(req.getRequestURI());

		if (item != null) {
			return (Page<Item>) item.getValue();
		} else {
			Page<Item> retrievedItem = repo.findByItemName(name, PageRequest.of(page, size));

			item = new CacheItem(req.getRequestURI(), retrievedItem);

			cache.insertToCache(req.getRequestURI(), item);

			return retrievedItem;
		}
	}

	@GetMapping("/item/brand/{brand}")
	public Page<Item> getItemByBrand(HttpServletRequest req, @PathParam("brand") String brand, 
			@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) throws Exception {
		// Check the cache for this address
		CacheItem item = cache.retrieveIfExistsAndNewerThan2Hours(req.getRequestURI());

		if (item != null) {
			return (Page<Item>) item.getValue();
		} else {
			Page<Item> retrievedItem = repo.findByItemBrand(brand, PageRequest.of(page, size));

			item = new CacheItem(req.getRequestURI(), retrievedItem);

			cache.insertToCache(req.getRequestURI(), item);

			return retrievedItem;
		}
	}

	@GetMapping("/all-items")
	public Page<Item> getAllItems(HttpServletRequest req, 
			@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size) throws Exception {
		// Check the cache for this address
		CacheItem item = cache.retrieveIfExistsAndNewerThan2Hours(req.getRequestURI());

		if (item != null) {
			return (Page<Item>) item.getValue();
		} else {
			Page<Item> retrievedItem = repo.findAll(PageRequest.of(page, size));

			item = new CacheItem(req.getRequestURI(), retrievedItem);

			cache.insertToCache(req.getRequestURI(), item);

			return retrievedItem;
		}
	}

	@DeleteMapping("/item/{id}")
	public ResponseEntity deleteItem(@PathVariable("id") Long id) throws Exception {
		deleteRepo.deleteById(id);

		return ResponseEntity.ok().build();
	}

	@PatchMapping(path="/item", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> updateCustomerContacts(@RequestBody Item itemPassed) {
		Item itemToUpdate;

		try {
			itemToUpdate = repo.findById(itemPassed.getId())
					.orElseThrow(() -> new Exception("Item does not exist"));
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		itemToUpdate.setItemBrand(itemPassed.getItemBrand());
		itemToUpdate.setItemName(itemPassed.getItemName());
		itemToUpdate.setPrice(itemPassed.getPrice());
		itemToUpdate.setQuantity(itemPassed.getQuantity());

		repo.save(itemToUpdate);

		return new ResponseEntity<>(itemToUpdate, HttpStatus.OK);
	}
	
}
