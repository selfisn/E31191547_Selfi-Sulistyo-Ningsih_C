package com.example.hidropon.model;

import android.graphics.drawable.Drawable;

public class TerakhirDilihat {
    String name;
    String describtion;
    String price;
    String quantity;
    String unit;
    int imageUrl;
    int bigimageurl;

    public TerakhirDilihat(String name, String describtion, String price, String quantity, String unit, int imageUrl, int bigimageurl){
        this.name = name;
        this.describtion = describtion;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.bigimageurl = bigimageurl;
    }

    public int getBigimageurl(){ return bigimageurl; }
    public void setBigimageurl(int bigimageurl){ this.bigimageurl = bigimageurl; }
    public String getName(){
        return  name;
    }
    public  void setName(String name){
        this.name = name;
    }
    public String getDescribtion(){
        return  describtion;
    }
    public  void setDescribtion(String describtion){
        this.describtion = describtion;
    }
    public String getPrice(){
        return  price;
    }
    public  void setPrice(String price){
        this.price = price;
    }
    public String getQuantity(){
        return  quantity;
    }
    public  void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public String getUnit(){
        return  unit;
    }
    public  void setUnit(String unit){
        this.unit = unit;
    }
    public int getImageUrl(){
        return  imageUrl;
    }
    public  void setImageUrl(int imageUrl){
        this.imageUrl = imageUrl;
    }
}
