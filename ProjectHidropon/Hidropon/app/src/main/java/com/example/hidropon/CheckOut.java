package com.example.hidropon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hidropon.model.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CheckOut extends AppCompatActivity {

    // INIT
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static Context mContext;
    private static Activity checkOutActivity;
    private static Spinner spinnerNamaLokasi;

    TextView nomorPesanan, alamatPengiriman;
    Intent intentFrom;
    private static Bundle bundleIntent;
    private static String strRandNumb;
    private static String thisFrom;
    private static String id_pelanggan, id_produk, jumlah, nama, harga, berat, subberat, subharga;

    private static List<String> spItemNamaLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mContext = this;
        checkOutActivity = this;

        alamatPengiriman = findViewById(R.id.txAlamatPengiriman);
        nomorPesanan = findViewById(R.id.checkOut_NomorPesanan);

        intentFrom = getIntent();
        bundleIntent = intentFrom.getExtras();
        thisFrom = bundleIntent.getString("from");

        // Jika diakses dari activity cart
        if (thisFrom.equalsIgnoreCase("CartActivity")){
            // mengambil id_pembelian sesuai data yg disimpan preferences dan ditampilkan
            nomorPesanan.setText(Preferences.getOrderId(getBaseContext()));
        } else if (thisFrom.equalsIgnoreCase("BuyNow")) {
            //Data Produk dari pass intent produk detail
            id_pelanggan = Preferences.getIDPelanggan(getBaseContext());
            id_produk = bundleIntent.getString("id_produk").toString();
            jumlah = bundleIntent.getString("jumlah").toString();
            nama = bundleIntent.getString("nama").toString();
            harga = bundleIntent.getString("harga").toString();
            berat = bundleIntent.getString("berat").toString();
            subberat = bundleIntent.getString("subberat").toString();
            subharga = bundleIntent.getString("subharga").toString();

            //Generate Order ID
            Random r = new Random();
            int randomNumb = r.nextInt(1000 - 10) + 10;
            strRandNumb = String.valueOf(randomNumb);

            // menampilkan order id untuk pembelian langsung
            nomorPesanan.setText(strRandNumb);
        } else {
            // untuk mencegah null
            nomorPesanan.setText("XX");
        }

        findViewById(R.id.backButtonCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(CheckOut.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        findViewById(R.id.cart_from_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(CheckOut.this, CartActivity.class);
                startActivity(back);
                finish();
            }
        });

        findViewById(R.id.btnProsesCheckout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesCheckout();
            }
        });

        getNamaLokasi();
    }

    public static Context getContext() {
        return mContext;
    }

    private void getNamaLokasi() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_NAMA_LOKASI, null, CODE_GET_REQUEST, "getNamaLokasi");
        request.execute();
    }

    public static void setNamaLokasi(JSONArray lokasi) throws JSONException {
        spItemNamaLokasi = new ArrayList<>();
        for (int i = 0; i < lokasi.length(); i++) {
            JSONObject obj = lokasi.getJSONObject(i);
            spItemNamaLokasi.add(obj.getString("nama_lokasi"));
        }

        spinnerNamaLokasi = (Spinner) checkOutActivity.findViewById(R.id.spNamaLokasi);
        ArrayAdapter<String> namaLokasiAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_item, spItemNamaLokasi);
        namaLokasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNamaLokasi.setAdapter(namaLokasiAdapter);
    }

    private void addToCartDirect() {
        String id_pembelian = strRandNumb;

        // Add Parameter
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pembelian", id_pembelian);
        params.put("id_pelanggan", id_pelanggan);
        params.put("id_produk", id_produk);
        params.put("jumlah", jumlah);
        params.put("nama", nama);
        params.put("harga", harga);
        params.put("berat", berat);
        params.put("subberat", subberat);
        params.put("subharga", subharga);

        // Request to API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_ADD_TO_CART_DIRECT, params, CODE_POST_REQUEST, "addToCartDirect");
        request.execute();
    }

    private void prosesCheckout() {
        String id_pembelian = Preferences.getOrderId(getBaseContext());
        String id_pelanggan = Preferences.getIDPelanggan(getBaseContext());
        String nama_lokasi = spinnerNamaLokasi.getSelectedItem().toString();
        String alamat_pengiriman = alamatPengiriman.getText().toString();
        String thisFrom = bundleIntent.get("from").toString();
        String sub_harga = "0";

        // Jika melakukan buy now (tidak melalui cart)
        if (thisFrom.equalsIgnoreCase("BuyNow")) {
            id_pembelian = strRandNumb;
            sub_harga = subharga;
        }

        // Add parameter to send
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pembelian", id_pembelian);
        params.put("id_pelanggan", id_pelanggan);
        params.put("total_pembelian", sub_harga);
        params.put("nama_lokasi", nama_lokasi);
        params.put("alamat_pengiriman", alamat_pengiriman);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CHECKOUT, params, CODE_POST_REQUEST, "prosesCheckout");
        request.execute();

        // Jika melakukan buy now,  untuk menambahkan data ke table pembelian_produk
        if (thisFrom.equalsIgnoreCase("BuyNow")) {
            addToCartDirect();
        }
    }

    public static void afterSuccess() {
        // Jika actv ini diakses dari activity cart
        if (thisFrom.equalsIgnoreCase("CartActivity")){
            //Generate Order ID
            Random r = new Random();
            int randomNumb = r.nextInt(1000 - 10) + 10;
            String strRandNumb = String.valueOf(randomNumb);

            Preferences.setOrderId(getContext(), strRandNumb);
            mContext.startActivity(new Intent(getContext(), MainActivity.class));
        } else if (thisFrom.equalsIgnoreCase("BuyNow")) {
            Intent i = new Intent(CheckOut.getContext(), MainActivity.class);
            getContext().startActivity(i);
        }
    }
}