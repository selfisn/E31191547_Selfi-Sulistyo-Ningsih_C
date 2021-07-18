package com.example.hidropon.model;

public class AllProdukModel {
    private int id_kategori;
    private String id_produk, nama_produk, foto_produk, deskripsi_produk, harga_produk, berat_produk;

    public AllProdukModel(String id_produk, int id_kategori, String nama_produk, String harga_produk, String berat_produk, String foto_produk, String deskripsi_produk) {
        this.id_produk = id_produk;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.berat_produk = berat_produk;
        this.foto_produk = foto_produk;
        this.deskripsi_produk = deskripsi_produk;
    }

    public String getIdProduk() {
        return id_produk;
    }

    public int getIdKategori() {
        return id_kategori;
    }

    public String getNamaProduk() {
        return nama_produk;
    }

    public String getHargaProduk() {
        return harga_produk;
    }

    public String getBeratProduk() {
        return berat_produk;
    }

    public String getFotoProduk() {
        return foto_produk;
    }

    public String getDeskripsiProduk() {
        return deskripsi_produk;
    }
}

