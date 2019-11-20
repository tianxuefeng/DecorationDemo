package com.tc.list.entity;

public class CategoryEntity {
    public boolean selected;
    public String content;

    public CategoryEntity(String content) {
        this.content = content;
    }

    public CategoryEntity(boolean selected, String content) {
        this.selected = selected;
        this.content = content;
    }
}
