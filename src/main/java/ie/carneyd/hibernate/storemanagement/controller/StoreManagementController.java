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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ie.carneyd.hibernate.storemanagement.cache.Cache;
import ie.carneyd.hibernate.storemanagement.cache.CacheItem;
import ie.carneyd.hibernate.storemanagement.item.Item;
import ie.carneyd.hibernate.storemanagement.repository.ItemDeleteRepository;
import ie.carneyd.hibernate.storemanagement.repository.ItemRepository;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary="Save an Item to the Database")
	@PostMapping(path="/item", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Item> createItem(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description="Item for Creation", required=true)
			@RequestBody Item item) {
		Item itemCreated;

		try {
			itemCreated = repo.save(item);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(itemCreated, HttpStatus.OK);
	}

	@Operation(summary="Save a list of Items to the Database")
	@PostMapping(path="/all-items", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Item>> createItems(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description="Items for Creation", required=true)
			@RequestBody List<Item> items) {
		Iterable<Item> itemsCreated;

		try {
			itemsCreated = repo.save(items);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>((List) itemsCreated, HttpStatus.OK);
	}

	@Operation(summary="Get an Item by Primary Key")
	@GetMapping("/item/{id}")
	public Item getItem(HttpServletRequest req, 
			@ApiParam(name= "id", value="Primary Key of the Item", required=true) @PathVariable("id") long id) throws Exception {
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

	@Operation(summary="Search for Items by Name")
	@GetMapping("/item/name/{name}")
	public Page<Item> getItemsByName(HttpServletRequest req, 
			@ApiParam(name="name", value="Name of the Item", required=true) @PathParam("name") String name, 
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

	@Operation(summary="Search for Items by Brand")
	@GetMapping("/item/brand/{brand}")
	public Page<Item> getItemsByBrand(HttpServletRequest req, 
			@ApiParam(name="brand", value = "Brand of the Item", required=true) @PathParam("brand") String brand, 
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

	@Operation(summary="Get all Items")
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

	@Operation(summary="Delete Item by Primary Key")
	@DeleteMapping("/item/{id}")
	public ResponseEntity deleteItem(
			@ApiParam(name="id", value="Primary Key of the Item", required=true) @PathVariable("id") Long id) throws Exception {
		deleteRepo.deleteById(id);

		return ResponseEntity.ok().build();
	}

	@Operation(summary="Update Item")
	@RequestMapping(path="/item", consumes=MediaType.APPLICATION_JSON_VALUE, 
				method={RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<Item> updateItem(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description="Item for Updating", required=true)
			@RequestBody Item itemPassed) {
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
