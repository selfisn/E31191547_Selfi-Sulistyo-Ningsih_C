package com.example.hidropon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.R;
import com.example.hidropon.model.ProdukTerbaru;

import java.util.List;

public class ProdukTerbaruAdapter extends RecyclerView.Adapter<ProdukTerbaruAdapter.ProdukTerbaruViewHolder> {

    Context context;
    List<ProdukTerbaru> ProdukTerbaruList;

    public ProdukTerbaruAdapter(Context context, List<ProdukTerbaru>produkTerbaruList){
        this.context = context;
        this.ProdukTerbaruList = produkTerbaruList;
    }

    @NonNull
    @Override
    public ProdukTerbaruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.produkterbaru_row_item, parent, false);
        return new ProdukTerbaruViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukTerbaruViewHolder holder, int position) {

        holder.ProdukTerbaruImageview.setImageResource(ProdukTerbaruList.get(position).getImageurl());
    }

    @Override
    public int getItemCount() {
        return ProdukTerbaruList.size();
    }

    public static class ProdukTerbaruViewHolder extends  RecyclerView.ViewHolder{

        ImageView ProdukTerbaruImageview;

        public ProdukTerbaruViewHolder(@NonNull View itemView){
            super(itemView);

            ProdukTerbaruImageview = itemView.findViewById(R.id.allProduct_ImgProduct);
        }
    }
}
