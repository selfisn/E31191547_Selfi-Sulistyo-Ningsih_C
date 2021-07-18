package com.example.hidropon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    public static Activity loginActivity = null;
    public static Context mContext;
    private static String id_pelanggan_login;

    private EditText mViewUser, mViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;
        mContext = this;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        /** Menginisialisasi variable dengan Form User dan Form Password dari Layout LoginActivity */
        /** menginisiasikan ViewUser melalui editText emailSignin yaitu username */
        mViewUser=findViewById(R.id.et_emailSignin);
        /** menginisiasikan ViewPassword melalui editText passwordSignin yaitu password */
        mViewPassword =findViewById(R.id.et_passwordSignin);
        /** Menjalankan Method razia() Jika tombol SignIn di keyboard di sentuh */
        mViewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    razia();
                    return true;
                }
                return false;
            }
        });

        /** Menjalankan Method razia() jika merasakan tombol SignIn disentuh */
        findViewById(R.id.button_signinSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                razia();
            }
        });
        /** Ke RegisterActivity jika merasakan tombol SignUp disentuh */
        findViewById(R.id.button_signupSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
            }
        });
    }

    /** ke MainActivity jika data Status Login dari Data Preferences bernilai true */
    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }

    public static Context getContext(){
        return mContext;
    }

    public static void set_id_pelanggan(String id_pelanggan){
        LoginActivity.id_pelanggan_login = id_pelanggan;
    }

    /** Men-check inputan User dan Password dan Memberikan akses ke MainActivity */
    private void razia(){
        /** Mereset semua Error dan fokus menjadi default */
        mViewUser.setError(null);
        mViewPassword.setError(null);
        View fokus = null;
        boolean cancel = false;

        /** Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
        String user = mViewUser.getText().toString();
        String password = mViewPassword.getText().toString();

        /** Jika form user kosong atau TIDAK memenuhi kriteria di Method cekUser() maka, Set error
         *  di Form User dengan menset variable fokus dan error di Viewnya juga cancel menjadi true*/
        if (TextUtils.isEmpty(user)){
            mViewUser.setError("This field is required");
            fokus = mViewUser;
            cancel = true;
        }

        /** Sama syarat percabangannya dengan User seperti di atas. Bedanya ini untuk Form Password*/
        if (TextUtils.isEmpty(password)){
            mViewPassword.setError("This field is required");
            fokus = mViewPassword;
            cancel = true;
        }

        /** Jika cancel true, variable fokus mendapatkan fokus */
        if (cancel) fokus.requestFocus();
        else loginUser();
    }

    /** Menuju ke MainActivity dan Set User dan Status sedang login, di Preferences */
    public static void masuk(){
        Preferences.setLoggedInUser(getContext(),Preferences.getRegisteredUser(getContext()));
        Preferences.setLoggedInStatus(getContext(),true);
        Preferences.setIDPelanggan(getContext(), id_pelanggan_login);

        //Generate Order ID
        Random r = new Random();
        int randomNumb = r.nextInt(1000 - 10) + 10;
        String strRandNumb = String.valueOf(randomNumb);

        Preferences.setOrderId(getContext(), strRandNumb);
        getContext().startActivity(new Intent(getContext(),MainActivity.class));
        loginActivity.finish();
    }

    public void loginUser(){
        String email = mViewUser.getText().toString();
        String password = mViewPassword.getText().toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_LOGIN, params, CODE_POST_REQUEST, "loginUser");
        request.execute();
    }
}