package org.feup.acmeeletronicsshop.model;

public class TransactionItem {
    private int id;
    private int quantity;
    private String itemName;

    public TransactionItem(int id, int quantity, String itemName) {
        this.id = id;
        this.quantity = quantity;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
