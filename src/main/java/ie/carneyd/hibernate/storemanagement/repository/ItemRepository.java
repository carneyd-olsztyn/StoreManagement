package ie.carneyd.hibernate.storemanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import ie.carneyd.hibernate.storemanagement.item.Item;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

	public Page<Item> findByItemName(String itemName, Pageable pageable);

	public Page<Item> findByItemBrand(String itemBrand, Pageable pageable);

	public Optional<Item> findById(long id);

	public Item save(Item item);

	public Iterable<Item> save(Iterable<Item> item);
}
