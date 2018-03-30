package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/18.
 */

public class SizeColor {
    private int sizeId;
    private int colorId;
    private String size;
    private String color;
    private boolean checked;

    public SizeColor(int sizeId, int colorId, String size, String color) {
        this.sizeId = sizeId;
        this.colorId = colorId;
        this.size = size;
        this.color = color;
    }

    public SizeColor() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
