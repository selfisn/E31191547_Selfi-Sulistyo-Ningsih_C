package com.example.hidropon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.adapter.AllProdukAdapter;
import com.example.hidropon.adapter.SemuaProdukAdapter;
import com.example.hidropon.model.AllProdukModel;
import com.example.hidropon.model.SemuaProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllProduk extends AppCompatActivity {

    RecyclerView AllProdukRecycler;
    AllProdukAdapter allProdukAdapter;
    List<AllProdukModel> allprodukModelList;
    ImageView back, cart;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private static List<AllProdukModel> semuaProdukList;
    private static Context mContext;
    private static RecyclerView AllProductsRecyclerView;
    private static AllProdukAdapter semuaProdukAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_produk);

        mContext = this;

        AllProdukRecycler = findViewById(R.id.all_produk);
        AllProductsRecyclerView = findViewById(R.id.all_produk);
        back = findViewById(R.id.backButtonCart);
        cart = findViewById(R.id.cart_btn_product);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent back = new Intent(AllProduk.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent back = new Intent(AllProduk.this, CartActivity.class);
                startActivity(back);
                finish();
            }
        });

        //adding data to model//\
//        allprodukModelList = new ArrayList<>();
//        allprodukModelList.add(new AllProdukModel(1, R.drawable.stroberi));
//        allprodukModelList.add(new AllProdukModel(1, R.drawable.bayam));
//        allprodukModelList.add(new AllProdukModel(1, R.drawable.bawangmerah));
//        allprodukModelList.add(new AllProdukModel(1, R.drawable.produk_sayur));
//        allprodukModelList.add(new AllProdukModel(1, R.drawable.cabai));
//
//        setProdukRecycler(allprodukModelList);
        showAllProduk();
    }

    public static Context getContext(){
        return mContext;
    }

    public static RecyclerView getAllProductsRecyclerView(){
        return AllProductsRecyclerView;
    }

    private void showAllProduk() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_PRODUCT, null, CODE_GET_REQUEST, "showAllProduk");
        request.execute();
    }

    public static void setAllProductList(JSONArray products) throws JSONException {
        semuaProdukList = new ArrayList<>();
        for (int i = 0; i < products.length(); i++) {
            JSONObject obj = products.getJSONObject(i);

            semuaProdukList.add(new AllProdukModel(
                    obj.getString("id_produk"),
                    obj.getInt("id_kategori"),
                    obj.getString("nama_produk"),
                    obj.getString("harga_produk"),
                    obj.getString("berat_produk"),
                    obj.getString("foto_produk"),
                    obj.getString("deskripsi_produk"))
            );
        }

        setSemuaProdukRecycler(semuaProdukList);
    }

    private static void setSemuaProdukRecycler(List<AllProdukModel> semuaProdukDataList) {
//        RecyclerView.LayoutManager LayoutManagerSemuaProduk = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager LayoutManager = new GridLayoutManager(getContext(), 3);
        getAllProductsRecyclerView().setLayoutManager(LayoutManager);
        semuaProdukAdapter = new AllProdukAdapter(getContext(), semuaProdukDataList);
        getAllProductsRecyclerView().setAdapter(semuaProdukAdapter);
    }

    private void setProdukRecycler(List<AllProdukModel> allprodukModelList){
        RecyclerView.LayoutManager LayoutManager = new GridLayoutManager(this, 3);
        AllProdukRecycler.setLayoutManager(LayoutManager);
        allProdukAdapter = new AllProdukAdapter(this, allprodukModelList);
        AllProdukRecycler.setAdapter(allProdukAdapter);
    }

}









