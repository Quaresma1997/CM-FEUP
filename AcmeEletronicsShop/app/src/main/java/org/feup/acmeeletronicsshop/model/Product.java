package org.feup.acmeeletronicsshop.model;

public class Product {
    private int id;
    private String name;
    private int price;
    private long barcode;
    private int quantity;

    public Product(){};



    public Product(int id, String name, int price, long barcode, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barcode = barcode;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
