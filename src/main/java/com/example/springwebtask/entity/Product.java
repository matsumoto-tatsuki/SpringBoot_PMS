package com.example.springwebtask.entity;

public class Product {
    private String productId;
    private String productName;
    private int productPrice;
    private String categoryName;
    private String description;

    public Product(String productId,String productName,int productPrice,String categoryName,String description){
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
