package org.feup.acmeeletronicsshop.model;

public class Product {
    private int id;
    private String name;
    private String model;
    private String maker;
    private String color;
    private String description;
    private int price;
    private long barcode;

    public Product(){};

    public Product(int id, String name, String model, String maker, String color, String description, int price, long barcode) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.maker = maker;
        this.color = color;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
}
