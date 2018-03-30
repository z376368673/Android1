package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/20.
 */

public class Category {
    private int id;
    private String name;
    private int topId;  // 顶级id。 0 表示顶级，即当前类目为顶级类目
    private int parentId;  // 父级id。 0 表示顶级，即当前类目为顶级类目
    private int level;  // 第几级
    private boolean isDefault;
    private boolean checked;

    public Category(int id, String name, int topId, int parentId, int level) {
        this.id = id;
        this.name = name;
        this.topId = topId;
        this.parentId = parentId;
        this.level = level;
    }

    public static Category makeTop(int id, String name){
        return new Category(id,name,0,0,1);
    }

    public Category() {
    }

    public void toggleChecked(){
        checked = !checked;
    }

    public void toggleDefault(){
        isDefault = !isDefault;
    }

    public Category makeChild(int id,String name){
        return  new Category(id,name,topId,this.id,level+1);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
