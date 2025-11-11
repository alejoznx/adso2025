package com.app.backend.model;



import jakarta.persistence;
import lombok.*;


@Data
@Entity
@Table (name "products")
public class Product{

@id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String name;

@Column (length = 1000 )
private String description;

@Column(nullable = false)
private Double price;

private Integer stock;

@ Column (nullable = false)
private boolean active = true;

@ManyToOne
@JoinColumn(name = "category_id", nullable = false)
private Category category;


@ManyToOne
@JoinColumn(name = "subcategory_id", nullable = false)
private Subcategory subcategory;


public Long getName(){
    this.name = name; 
}

public void  setName (String name){

    this. name = name;

}

public String getDescription(){
return description;
}

public void setDescription(){

this.description = description;

}

public Double getPrice(){
return  price;

}

public void setPrice(Double price){
this.price = price;
}

public Integer getStock(){
return  stock;

}

public void setStock(Integer stock){
this.stock = stock;

}

public boolean getActive(){
    return active;

}

public void setActive(boolean active){
    this. active = active;
}

public Category getCategory(){
return category;
}


public void setCategory(Category category){
this. category = category;
}

public Category getSubCategory(){
return subcategory;
}


public void setSubCategory(Subcategory subcategory){
this.subcategory = subcategory;
}


}
