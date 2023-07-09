package ie.carneyd.hibernate.storemanagement.repository;

import org.springframework.data.repository.CrudRepository;

import ie.carneyd.hibernate.storemanagement.item.Item;

public interface ItemDeleteRepository extends CrudRepository<Item, Long> {

}
