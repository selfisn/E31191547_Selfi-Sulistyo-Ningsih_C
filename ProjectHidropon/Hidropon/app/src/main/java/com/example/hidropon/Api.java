package com.example.hidropon;

public class Api {
    // URL API
    // GANTI SESUAI SERVER ANDA
    private static final String ROOT_URL = "http://192.168.1.208/WebServices/Hidropon-WebService/v1/Api.php?apicall=";
    // URL GAMBAR
    // GANTI SESUAI SERVER ANDA
    public static final String IMG_URL = "http://192.168.1.208/WebServices/Hidropon-WebService/img/";

    // INI JANGAN DIGANTI!!!
    public static final String URL_ADD_TO_CART = ROOT_URL + "addtocart";
    public static final String URL_ADD_TO_CART_DIRECT = ROOT_URL + "addtocartdirect";
    public static final String URL_GET_NEW_PRODUCT = ROOT_URL + "getnewproducts";
    public static final String URL_GET_PRODUCT = ROOT_URL + "getproduk";
    public static final String URL_GET_CART = ROOT_URL + "showcart";
    public static final String URL_CHECKOUT = ROOT_URL + "checkout";
    public static final String URL_DELETE_FROM_CART = ROOT_URL + "deleteitemfromcart";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_REGISTER = ROOT_URL + "registeruser";
    public static final String URL_GET_NAMA_LOKASI = ROOT_URL + "getnamalokasi";
}
