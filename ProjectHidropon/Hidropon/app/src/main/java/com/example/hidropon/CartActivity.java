package com.example.hidropon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hidropon.adapter.CartAdapter;
import com.example.hidropon.model.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    // INIT
    ImageView backBtn;
    Button btnCheckout;

    private static RecyclerView cartRecyclerView;
    private static Context mContext;
    private static TextView GrandTotal;
    public static Activity CartActivity = null;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private static CartAdapter cartAdapter;
    private static List<CartModel> cartModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mContext = this;
        CartActivity = this;

        cartRecyclerView = findViewById(R.id.cartRecycler);
        backBtn = findViewById(R.id.backButtonCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        GrandTotal = findViewById(R.id.txGrandTotal);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent back = new Intent(CartActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent checkout = new Intent(CartActivity.this, CheckOut.class);
                checkout.putExtra("from", "CartActivity");
                startActivity(checkout);
                finish();
            }
        });

        getCartData();
    }

    public static Context getContext(){
        return mContext;
    }
    private static TextView getGrandTotalText() { return GrandTotal; }

    private void getCartData() {
        // get id pembelian yg disimpan di preferences
        String id_pembelian = Preferences.getOrderId(getBaseContext());

        // Menambahkan parameter untuk dikirim ke API
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pembelian", id_pembelian);

        // requset ke API menampilkan data cart sesuai parameter yg telah di set sebelumnta
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_CART, params, CODE_POST_REQUEST, "getCartData");
        request.execute();
    }

    public static void showGrandTotal(JSONArray total) throws JSONException {
        // Jika totalnya lebih dari 0
        if (total.length() > 0){
            // Mendapatkan data total dari API
            JSONObject gTotal = total.getJSONObject(0);
            String grand_total = gTotal.getString("total_harga");
            getGrandTotalText().setText(grand_total);
        } else {
            getGrandTotalText().setText("0");
        }
    }

    public static void deleteFromCart(String id_produk_to_delete) {
        String id_pembelian = Preferences.getOrderId(getContext());

        // set parameter
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pembelian", id_pembelian);
        params.put("id_produk", id_produk_to_delete);

        // do request
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_FROM_CART, params, CODE_POST_REQUEST, "deleteFromCart");
        request.execute();
    }

    public static void refreshCartList(JSONArray cart) throws JSONException {
        cartModelList = new ArrayList<>();
        for (int i = 0; i < cart.length(); i++) {
            JSONObject obj = cart.getJSONObject(i);

            cartModelList.add(new CartModel(
                    obj.getString("id_pembelian_produk"),
                    obj.getString("id_pembelian"),
                    obj.getString("id_produk"),
                    obj.getString("jumlah"),
                    obj.getString("nama"),
                    obj.getString("harga"),
                    obj.getString("berat"),
                    obj.getString("subberat"),
                    obj.getString("subharga"))
            );
        }

        setCartRecyclerView(cartModelList);
    }

    private static void setCartRecyclerView(List<CartModel> cartModelDataList) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(LayoutManager);
        cartAdapter = new CartAdapter(getContext(), cartModelDataList);
        cartRecyclerView.setAdapter(cartAdapter);
    }
}