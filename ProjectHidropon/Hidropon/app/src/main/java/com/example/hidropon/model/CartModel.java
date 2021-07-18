package com.example.hidropon.model;

public class CartModel {
    private String id_pembelian_produk, id_pembelian, id_produk, jumlah, nama, harga, berat, subberat, subharga;

    public CartModel(String id_pembelian_produk, String id_pembelian, String id_produk, String jumlah, String nama, String harga, String berat, String subberat, String subharga){
        this.id_pembelian_produk = id_pembelian_produk;
        this.id_pembelian = id_pembelian;
        this.id_produk = id_produk;
        this.jumlah = jumlah;
        this.nama = nama;
        this.harga = harga;
        this.berat = berat;
        this.subberat = subberat;
        this.subharga = subharga;
    }

    public String getIdPembelianProduk() {
        return id_pembelian_produk;
    }
    public String getIdPembelian() {
        return id_pembelian;
    }
    public String getIdProduk() {
        return id_produk;
    }
    public String getJumlah() {
        return jumlah;
    }
    public String getNama() {
        return nama;
    }
    public String getHarga() {
        return harga;
    }
    public String getBerat() {
        return berat;
    }
    public String getSubBerat() {
        return subberat;
    }
    public String getSubHarga() {
        return subharga;
    }
}
