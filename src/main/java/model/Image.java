package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "Image")
public class Image  extends model.Entity{

	@Expose
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique = true, nullable = false, precision = 22, scale = 0)
    private Long id;
	
	@Expose
	@Column(name = "type", length = 150, nullable = false)
    private String type;

	
	@ManyToOne  
	@JoinColumn(name = "product_id")
	private Product product;

	public Image(){}
	
	public Image(String type) {
		super();
		this.type = type;
	}
	
	public Image(Long id, String type, Product product) {
		super();
		this.id = id;
		this.type = type;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	 
	
 
	
	
	
}
