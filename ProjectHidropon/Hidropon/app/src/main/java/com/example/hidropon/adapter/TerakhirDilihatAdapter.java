package com.example.hidropon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.ProdukDetail;
import com.example.hidropon.R;
import com.example.hidropon.model.TerakhirDilihat;

import java.util.List;

public class TerakhirDilihatAdapter extends RecyclerView.Adapter<TerakhirDilihatAdapter.TerakhirDilihatViewHolder> {

    Context context;
    List<TerakhirDilihat> terakhirDilihatList;

    public TerakhirDilihatAdapter(Context context, List<TerakhirDilihat> terakhirDilihatList){
        this.context = context;
        this.terakhirDilihatList = terakhirDilihatList;
    }

    @NonNull
    @Override
    public TerakhirDilihatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.terakhir_dilihat_item, parent, false);
            return new TerakhirDilihatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TerakhirDilihatViewHolder holder, int position) {

        holder.name.setText(terakhirDilihatList.get(position).getName());
        holder.description.setText(terakhirDilihatList.get(position).getDescribtion());
        holder.price.setText(terakhirDilihatList.get(position).getPrice());
        holder.qty.setText(terakhirDilihatList.get(position).getQuantity());
        holder.unit.setText(terakhirDilihatList.get(position).getUnit());
        holder.bg.setBackgroundResource(terakhirDilihatList.get(position).getImageUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProdukDetail.class);
                i.putExtra("name", terakhirDilihatList.get(position).getName());
                i.putExtra("image", terakhirDilihatList.get(position).getBigimageurl());
                i.putExtra("price", terakhirDilihatList.get(position).getPrice());
                i.putExtra("desc", terakhirDilihatList.get(position).getDescribtion());
                System.out.println("XXXXXXXX: " + terakhirDilihatList.get(position).getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return terakhirDilihatList.size();
    }

    public static class TerakhirDilihatViewHolder extends RecyclerView.ViewHolder{

        TextView name, description, price, qty, unit;
        ConstraintLayout bg;

        public TerakhirDilihatViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nama_produk);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            unit = itemView.findViewById(R.id.unit);
            bg = itemView.findViewById(R.id.terakhirdilihat_layout);
        }
    }
}
