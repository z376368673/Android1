package com.huazong.app.huazong.entity;


public class Navigation {
    private int normalPic;
    private int selectedPic;
    private String name;
    private Class<?> cls;

    public Navigation() {
    }

    public Navigation(int normalPic, int selectedPic, String name) {
        this.normalPic = normalPic;
        this.selectedPic = selectedPic;
        this.name = name;
    }

    public Navigation(int normalPic, int selectedPic, String name, Class<?> cls) {
        this.normalPic = normalPic;
        this.selectedPic = selectedPic;
        this.name = name;
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public int getNormalPic() {
        return normalPic;
    }

    public void setNormalPic(int normalPic) {
        this.normalPic = normalPic;
    }

    public int getSelectedPic() {
        return selectedPic;
    }

    public void setSelectedPic(int selectedPic) {
        this.selectedPic = selectedPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
