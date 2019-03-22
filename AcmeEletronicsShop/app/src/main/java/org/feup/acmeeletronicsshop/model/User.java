package org.feup.acmeeletronicsshop.model;

public class User {

    private int id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String fiscalNumber;
    private String publicKey;
    private String privateKey;

    public User(){

    }

    public User(String publicKey, String privateKey, int id, String name, String address, String email, String password, String fiscalNumber) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.fiscalNumber = fiscalNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }

    public void setFiscalNumber(String fiscalNumber) {
        this.fiscalNumber = fiscalNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


}
