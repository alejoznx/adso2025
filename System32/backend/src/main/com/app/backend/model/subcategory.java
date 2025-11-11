package com.app.backend.model;



import jakarta.persistence;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.list;


@Data
@Entity
@Table (name "categories")
public class subcategory{

@id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String name;

@Column (length = 500 )
private String description;

@ Column (nullable = false)
private boolean active = true;

@ManyToOne
@JoinColumn(name = "category_id", nullable = false)
private Category category;

@OneToMany (mappedBy = "subcategory", cascade = CascadeType.All )
@JsonIgnore
private List<product> products;


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

public boolean getActive(){

    return active;

}


public void setActive(boolean active){
    this. active = active;
}

public Category getCategory(){
return category
}


public void setCategory(Category category){
this. category = category
}


}
