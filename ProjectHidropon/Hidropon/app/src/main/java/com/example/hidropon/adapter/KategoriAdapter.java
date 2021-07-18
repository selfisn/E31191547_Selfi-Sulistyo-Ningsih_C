package com.example.hidropon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.R;
import com.example.hidropon.model.Kategori;

import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {

    Context context;
    List<Kategori> kategoriList;

    public KategoriAdapter(Context context, List<Kategori> kategoriList){
        this.context = context;
        this.kategoriList = kategoriList;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.kategori_row_item, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {

        holder.KategoriImage.setImageResource(kategoriList.get(position).getImageurl());
    }

    @Override
    public int getItemCount() {

        return kategoriList.size();
    }

    public static class KategoriViewHolder extends RecyclerView.ViewHolder{

        ImageView KategoriImage;

        public KategoriViewHolder(@NonNull View itemView){
            super(itemView);

            KategoriImage = itemView.findViewById(R.id.allProduct_ImgProduct);

        }
    }

}
