package ie.carneyd.hibernate.storemanagement.item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description="Inventory Item in the Store")
public class Item {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes="Primary Key for the Item")  
	private long id;
	
	@ApiModelProperty(notes="Item Name")  
	private String itemName;
	
	@ApiModelProperty(notes="Item Brand")
	private String itemBrand;
	
	@ApiModelProperty(notes="Quantity of Items")
	private int quantity;
	
	@ApiModelProperty(notes="Price of individual Items")
	private double price;
	
	protected Item() {}
	
	public Item(String itemName, String itemBrand, int quantity, double price) {
		super();
		this.itemName = itemName;
		this.itemBrand = itemBrand;
		this.quantity = quantity;
		this.price = price;
	}
	
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemBrand() {
		return itemBrand;
	}
	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", itemName=" + itemName + ", itemBrand=" + itemBrand + ", quantity=" + quantity
				+ "]";
	}
}
