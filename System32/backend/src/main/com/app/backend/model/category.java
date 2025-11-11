package com.app.backend.model;



import jakarta.persistence;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.list;


@Data
@Entity
@Table (name "categories")
public class category{

@id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique = true)
private String name;

@Column (length = true )
private String description;

@ Column (nullable = false)
private boolean active = true;

@OneToMany (mappedBy = "category", cascade = CascadeType.All )
@JsonIgnore
private list<Subcategory>Subcategories;

public Long getId(){
return id;
}

public Long setId(){
    this id;
}

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

this.description;

}

public boolean getActive(){

    return active;

}


public void setActive(boolean active){
    this. active = active;
}

public List<Subcategory> getSubcategories(){
return Subcategories;

}


public void setSubcategories(List<Subcategory> Subcategories){
    this.subcategories = subcategories:
}

}
