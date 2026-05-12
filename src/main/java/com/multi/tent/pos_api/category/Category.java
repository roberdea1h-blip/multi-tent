package com.multi.tent.pos_api.category;

import com.multi.tent.pos_api.common.Entity.CreatableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends  CreatableEntity {
    private String name;

    protected Category() {}

    private Category(String name) {
        this.name = name;
    }

    public static Category create(String name) {
        return new Category(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
