package com.autoplus.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private int id;
    private String title;
    private String url;
    private Category parent;
    private List<Category> subcategories = new ArrayList<>();

    public Category(String title, String url, Category parent) {
        this.title = title;
        this.url = url;
        this.parent = parent;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
        for (Category c: subcategories) {
            c.setParent(this);
        }
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
                Objects.equals(url, category.url) &&
                Objects.equals(parent, category.parent) &&
                Objects.equals(subcategories, category.subcategories);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, url, parent, subcategories);
    }
}
