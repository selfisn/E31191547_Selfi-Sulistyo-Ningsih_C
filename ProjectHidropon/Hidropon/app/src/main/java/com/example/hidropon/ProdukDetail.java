package com.example.hidropon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProdukDetail extends AppCompatActivity {

    // INIT
    ImageView img, back;
    TextView prodName, prodPrice, prodDesc;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    public static Context mContext;

    private String id_produk = "";
    private String berat_produk = "";

    String name, price, desc, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);
        mContext = this;

        Intent i = getIntent();

        // Untuk mendapatkan data yg dibawa intent
        setIdProduk(i.getStringExtra("id_produk"));
        setBeratProduk(i.getStringExtra("berat_produk"));
        name = i.getStringExtra("nama_produk");
        image = i.getStringExtra("foto_produk");
        price = i.getStringExtra("harga_produk");
        desc = i.getStringExtra("deskripsi_produk");

        // Set value dari intent ke setiap komponen
        prodName = findViewById(R.id.prodName);
        prodDesc = findViewById(R.id.prodDesc);
        prodPrice = findViewById(R.id.prodPrice);
        img = findViewById(R.id.big_image);
        back = findViewById(R.id.backButtonCart);

        prodName.setText(name);
        prodPrice.setText(price);
        prodDesc.setText(desc);
        Picasso.with(ProdukDetail.this).load(Api.IMG_URL + image).into(img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ProdukDetail.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        findViewById(R.id.button_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Custom Dialog untuk menantukan jumlah yg ingin dimasukkan ke cart
                LayoutInflater li = LayoutInflater.from(ProdukDetail.this);
                View promptsView = li.inflate(R.layout.layout_input_jumlah_addtocart, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProdukDetail.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                // Konten dari dialog
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // edit text
                                        addToCart(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                // Menampilkan custom dialog
                alertDialog.show();
            }
        });

        findViewById(R.id.cart_from_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProdukDetail.this, CartActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProdukDetail.this, CheckOut.class);

                // Direct BuyNow Prop
                String id_produk = getIdProduk();
                String jumlah = "1";
                String nama = prodName.getText().toString();
                String harga = prodPrice.getText().toString();
                String berat = getBeratProduk();
                String subberat = getBeratProduk();
                String subharga = prodPrice.getText().toString();

                // Data to pass
                i.putExtra("from", "BuyNow");
                i.putExtra("id_produk", id_produk);
                i.putExtra("jumlah", jumlah);
                i.putExtra("nama", nama);
                i.putExtra("harga", harga);
                i.putExtra("berat", berat);
                i.putExtra("subberat", subberat);
                i.putExtra("subharga", subharga);

                // Start activity
                startActivity(i);
                finish();
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    private void addToCart(String jumlah_produk) {
        String id_pembelian = Preferences.getOrderId(getBaseContext());
        String id_pelanggan = Preferences.getIDPelanggan(getBaseContext());
        String id_produk = getIdProduk();
        String jumlah = jumlah_produk;
        String nama = prodName.getText().toString();
        String harga = prodPrice.getText().toString();
        String berat = getBeratProduk();
        String subberat = getBeratProduk();
        String subharga = prodPrice.getText().toString();

        // Add parameter yg akan dimasukkan ke dalam DB
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

        // Request ke API untuk input data yg dibawa ke DB sesuai parameter diatas
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_ADD_TO_CART, params, CODE_POST_REQUEST, "addToCart");
        request.execute();
    }

    private void setIdProduk(String id_produk) {
        this.id_produk = id_produk;
    }

    private void setBeratProduk(String berat_produk) {
        this.berat_produk = berat_produk;
    }

    private String getIdProduk() {
        return this.id_produk;
    }

    private String getBeratProduk() {
        return this.berat_produk;
    }
}