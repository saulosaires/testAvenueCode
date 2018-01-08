package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "Product")
public class Product extends model.Entity{
 	
	@Expose
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique = true, nullable = false, precision = 22, scale = 0)
    private Long id;
	
	@Expose
	@Column(name = "name", length = 50, nullable = false)
    private String name;
	
	@Expose
	@Column(name = "description", length = 150, nullable = false)
    private String description;

	@ManyToOne()
	@JoinColumn(name="parent_id")
	private Product parent;

	@Expose
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL,orphanRemoval=true)
	private Set<Product> subordinates = new HashSet<Product>();
   
   @Expose
   @OneToMany(mappedBy="product", cascade=CascadeType.ALL,orphanRemoval=true)  
   private Set<Image> images = new HashSet<Image>();
 
   
   public Product(){}
  
	public Product(Long id) {
		super();
		this.id = id;

	}

	public Product(Long id, String name,String description) {
		super();
		this.id = id;
		this.name = name;
		this.description=description;
	}
	
	public Product(Long id, String name, Product parent) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	 

	public Product(Long id, String name, String description, Set<Product> subordinates, Set<Image> images) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.subordinates = subordinates;
		this.images = images;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product getParent() {
		return parent;
	}

	public void setParent(Product parent) {
		this.parent = parent;
	}

	public Set<Product> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(Set<Product> subordinates) {
		this.subordinates = subordinates;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

 
	
		
}
