package com.example.hidropon.model;

public class ProdukTerbaru {

    Integer id;
    int imageurl;

    public ProdukTerbaru(Integer id, int imageurl) {
        this.id = id;
        this.imageurl = imageurl;

    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public int getImageurl() {

        return imageurl;
    }

    public void setImageurl(int imageurl) {

        this.imageurl = imageurl;
    }
}
