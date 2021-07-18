package com.example.hidropon;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
    private static String method;
    String url;
    HashMap<String, String> params;
    int requestCode;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode, String method) {
        this.url = url;
        this.params = params;
        this.requestCode = requestCode;
        this.method = method;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject object = new JSONObject(s);
            if (!object.getBoolean("error")) {
                if (method == "loginUser"){
                    boolean isInvalid = object.getBoolean("error_login");
                    if (isInvalid){
                        Toast.makeText(LoginActivity.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray result = object.getJSONArray("data_pelanggan");
                        JSONObject data_pelanggan = result.getJSONObject(0);
                        LoginActivity.set_id_pelanggan(data_pelanggan.getString("id_pelanggan"));
                        LoginActivity.masuk();
                    }
                } else if(method == "registerUser"){
                    boolean isInvalid = object.getBoolean("error_reg");
                    if (isInvalid){
                        Toast.makeText(RegisterActivity.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        RegisterActivity.getRegisterActivity().finish();
                    }
                } else if (method == "getAllProduk"){
                    MainActivity.refreshAllProductList(object.getJSONArray("products"));
                } else if(method == "showAllProduk"){
                    AllProduk.setAllProductList(object.getJSONArray("products"));
                } else if(method == "addToCart") {
                    Toast.makeText(ProdukDetail.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                } else if(method == "addToCartDirect"){
                    Toast.makeText(MainActivity.getContext(), "Berhasil melakukan pembelian langsung!", Toast.LENGTH_SHORT).show();
                    CheckOut.afterSuccess();
                } else if (method == "getCartData") {
                    CartActivity.refreshCartList(object.getJSONArray("cart"));
                    CartActivity.showGrandTotal(object.getJSONArray("total"));
                } else if (method == "deleteFromCart") {
                    Toast.makeText(CartActivity.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    CartActivity.refreshCartList(object.getJSONArray("cart"));
                    CartActivity.showGrandTotal(object.getJSONArray("total"));
                } else if(method == "getNamaLokasi"){
                    CheckOut.setNamaLokasi(object.getJSONArray("lokasi"));
                } else if (method == "prosesCheckout") {
                    Toast.makeText(CheckOut.getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    CheckOut.afterSuccess();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();

        if (requestCode == CODE_POST_REQUEST)
            return requestHandler.sendPostRequest(url, params);


        if (requestCode == CODE_GET_REQUEST)
            return requestHandler.sendGetRequest(url);

        return null;
    }
}
