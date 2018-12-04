package com.autoplus.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category implements Entity {
    private int id;
    private String title;
    private Category parent;
    private String reference;

    public Category(String title, String reference, Category parent) {
        this.title = title;
        this.reference = reference;
        this.parent = parent;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equals(title, category.title) &&
                Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, reference, parent);
    }

    @Override
    public String getReference() {
        return this.reference;
    }
}
