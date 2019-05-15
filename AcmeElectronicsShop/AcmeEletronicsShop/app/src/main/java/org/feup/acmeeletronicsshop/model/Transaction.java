package org.feup.acmeeletronicsshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Transaction implements Serializable {
    private int id;
    private Date date;
    private double totalCost;
    private String token;
    private List<TransactionItem> itemList;


    public Transaction(String token, Date date, double totalCost, List<TransactionItem> itemList) {
        this.date = date;
        this.totalCost = totalCost;
        this.itemList = itemList;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<TransactionItem> getItemlist() {
        return itemList;
    }

    public void setItemlist(List<TransactionItem> itemList) {
        this.itemList = itemList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
