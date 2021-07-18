package com.example.hidropon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hidropon.adapter.KategoriAdapter;
import com.example.hidropon.adapter.ProdukTerbaruAdapter;
import com.example.hidropon.adapter.SemuaProdukAdapter;
import com.example.hidropon.adapter.TerakhirDilihatAdapter;
import com.example.hidropon.model.Kategori;
import com.example.hidropon.model.ProdukTerbaru;
import com.example.hidropon.model.SemuaProduk;
import com.example.hidropon.model.TerakhirDilihat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Inisualisasi
    private static RecyclerView ProdukTerbaruRecyclerView;
    private static Context mContext;
    private static SemuaProdukAdapter semuaProdukAdapter;
    private static List<SemuaProduk> semuaProdukList;

    RecyclerView KategoriRecyclerView, TerakhirDilihatRecycler;
    ImageView allProduk;
    KategoriAdapter kategoriAdapter;
    List<Kategori> kategoriList;

    TerakhirDilihatAdapter terakhirDilihatAdapter;
    List<TerakhirDilihat> terakhirDilihatList;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // Force to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        ProdukTerbaruRecyclerView = findViewById(R.id.ProdukTerbaruRecycler);
        KategoriRecyclerView = findViewById(R.id.KategoryRecycler);
        allProduk = findViewById(R.id.allProdukImage);
        TerakhirDilihatRecycler = findViewById(R.id.terakhirdilihat_item);


        allProduk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, AllProduk.class);
                startActivity(i);
            }
        });

        //adding data to model//
        kategoriList = new ArrayList<>();
        kategoriList.add(new Kategori(1, R.drawable.kategori_sayur));
        kategoriList.add(new Kategori(2, R.drawable.kategori_buah));
        kategoriList.add(new Kategori(3, R.drawable.kategori_herbal));
        kategoriList.add(new Kategori(1, R.drawable.kategori_sayur));
        kategoriList.add(new Kategori(2, R.drawable.kategori_buah));
        kategoriList.add(new Kategori(3, R.drawable.kategori_herbal));

        terakhirDilihatList = new ArrayList<>();
        terakhirDilihatList.add(new TerakhirDilihat("Strawberry", "Strawberry is sweet and sour fruit", "15.000", "1", "KG", R.drawable.card1, R.drawable.b1));
        terakhirDilihatList.add(new TerakhirDilihat("Strawberry", "Strawberry is sweet and sour fruit", "15.000", "1", "KG", R.drawable.card1, R.drawable.b1));
        terakhirDilihatList.add(new TerakhirDilihat("Strawberry", "Strawberry is sweet and sour fruit", "15.000", "1", "KG", R.drawable.card1, R.drawable.b1));

        setKategoriRecycler(kategoriList);
        setTerakhirDilihatRecycler(terakhirDilihatList);

        findViewById(R.id.settingMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Untuk menampilkan konfirmasi logout
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Apakah anda yakin untuk logout?");
                builder.setIcon(R.drawable.question);
                // Jika diklik OK
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Preferences.clearLoggedInUser(getBaseContext());
                        Preferences.setLoggedInStatus(getBaseContext(),false);
                        startActivity(new Intent(getBaseContext(),LoginActivity.class));finish();
                    }
                });
                // Jika diklik NO
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        findViewById(R.id.cartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        // Memanggil method untuk menampilkan pada RV
        getAllProduk();
    }

    // Untuk mendapatkan context activity ini
    public static Context getContext(){
        return mContext;
    }

    public static RecyclerView getProdukTerbaruRecyclerView(){
        return ProdukTerbaruRecyclerView;
    }

    private void getAllProduk() {
        // Untuk request data ke webservice
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_NEW_PRODUCT, null, CODE_GET_REQUEST, "getAllProduk");
        request.execute();
    }

    public static void refreshAllProductList(JSONArray products) throws JSONException {
        // Digunakan untuk menambahkan respon yg dikirim API kedalam Array dan ditampilkan di recyclerview
        semuaProdukList = new ArrayList<>();
        for (int i = 0; i < products.length(); i++) {
            JSONObject obj = products.getJSONObject(i);

            semuaProdukList.add(new SemuaProduk(
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

    private static void setSemuaProdukRecycler(List<SemuaProduk> semuaProdukDataList) {
        RecyclerView.LayoutManager LayoutManagerSemuaProduk = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        getProdukTerbaruRecyclerView().setLayoutManager(LayoutManagerSemuaProduk);
        semuaProdukAdapter = new SemuaProdukAdapter(getContext(), semuaProdukDataList);
        getProdukTerbaruRecyclerView().setAdapter(semuaProdukAdapter);
    }

    private void setKategoriRecycler(List<Kategori> kategoriDataList) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        KategoriRecyclerView.setLayoutManager(LayoutManager);
        kategoriAdapter = new KategoriAdapter(this, kategoriDataList);
        KategoriRecyclerView.setAdapter(kategoriAdapter);
    }

    private void setTerakhirDilihatRecycler(List<TerakhirDilihat> terakhirDilihatDataList) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        TerakhirDilihatRecycler.setLayoutManager(LayoutManager);
        terakhirDilihatAdapter = new TerakhirDilihatAdapter(this, terakhirDilihatDataList);
        TerakhirDilihatRecycler.setAdapter(terakhirDilihatAdapter);
    }
}