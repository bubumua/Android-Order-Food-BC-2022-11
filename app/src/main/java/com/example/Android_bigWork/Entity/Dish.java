package com.example.Android_bigWork.Entity;

public class Dish {
    private int GID;
    private String name;
    private String description;
    private double price;
    private String category;
    private int CID;
    private boolean customizable;

    public Dish(String name,double price) {
        new Dish(0,name,"no description",price,"popular",0,false);
    }

    public Dish(int gid, String name, String description, double price, String category, int CID, boolean customizable) {
        this.GID=gid;
        setName(name);
        setDescription(description);
        setPrice(price);
        setCategory(category);
        setCID(CID);
        setCustomizable(customizable);
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

}
