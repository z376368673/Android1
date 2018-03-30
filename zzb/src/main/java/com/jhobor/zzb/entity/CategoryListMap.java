package com.jhobor.zzb.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/20.
 */

public class CategoryListMap {
    private Category parent;
    private List<Category> children;

    public CategoryListMap(Category parent, List<Category> children) {
        this.parent = parent;
        this.children = children;
    }

    public CategoryListMap(Category parent) {
        this.parent = parent;
        children = new ArrayList<>();
    }

    public void addChild(Category child){
        children.add(child);
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
