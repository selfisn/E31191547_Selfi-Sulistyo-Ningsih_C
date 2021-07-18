package com.example.hidropon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.Api;
import com.example.hidropon.ProdukDetail;
import com.example.hidropon.R;
import com.example.hidropon.model.AllProdukModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllProdukAdapter extends RecyclerView.Adapter<AllProdukAdapter.AllProdukViewHolder>{

    Context context;
    List<AllProdukModel> semuaProdukList;

    public AllProdukAdapter(Context context, List<AllProdukModel> semuaProdukList){
        this.context = context;
        this.semuaProdukList = semuaProdukList;
    }

    @NonNull
    @Override
    public AllProdukAdapter.AllProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_produk_row_item, parent, false);
        return new AllProdukAdapter.AllProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProdukAdapter.AllProdukViewHolder holder, int position) {
        holder.namaProduk.setText(semuaProdukList.get(position).getNamaProduk());
        Picasso.with(context).load(Api.IMG_URL + semuaProdukList.get(position).getFotoProduk()).into(holder.gambarProduk);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProdukDetail.class);
                i.putExtra("id_produk", semuaProdukList.get(position).getIdProduk());
                i.putExtra("nama_produk", semuaProdukList.get(position).getNamaProduk());
                i.putExtra("berat_produk", semuaProdukList.get(position).getBeratProduk());
                i.putExtra("deskripsi_produk", semuaProdukList.get(position).getDeskripsiProduk());
                i.putExtra("harga_produk", semuaProdukList.get(position).getHargaProduk());
                i.putExtra("foto_produk", semuaProdukList.get(position).getFotoProduk());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return semuaProdukList.size();
    }

    public static class AllProdukViewHolder extends RecyclerView.ViewHolder{

        TextView namaProduk;
        ImageView gambarProduk;

        public AllProdukViewHolder(@NonNull View itemView) {
            super(itemView);

            namaProduk = itemView.findViewById(R.id.allProduct_NamaProduk);
            gambarProduk = itemView.findViewById(R.id.allProduct_ImgProduct);
        }
    }
}

