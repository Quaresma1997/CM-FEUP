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


    private int cardNumber;
    private String cardType;
    private String cardValidity;

    public User() {

    }

    public User(int id, String name, String address, String email, String password, String fiscalNumber, String publicKey, String privateKey, int cardNumber, String cardType, String cardValidity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.fiscalNumber = fiscalNumber;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardValidity = cardValidity;
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

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardValidity() {
        return cardValidity;
    }

    public void setCardValidity(String cardValidity) {
        this.cardValidity = cardValidity;
    }


}
