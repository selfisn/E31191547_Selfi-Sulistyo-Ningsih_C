package com.example.hidropon.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hidropon.CartActivity;
import com.example.hidropon.R;
import com.example.hidropon.model.CartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<CartModel> cartModelList;

    public CartAdapter(Context context, List<CartModel> cartModelList){
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_row_item, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.namaProduk.setText(cartModelList.get(position).getNama());
        holder.hargaProduk.setText(cartModelList.get(position).getHarga());
        holder.jumlahBeli.setText(cartModelList.get(position).getJumlah());
        holder.subTotal.setText(cartModelList.get(position).getSubHarga());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Apakah anda yakin untuk menghapus?");
                builder.setIcon(R.drawable.warning);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        CartActivity.deleteFromCart(cartModelList.get(position).getIdProduk());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        TextView namaProduk, hargaProduk, jumlahBeli, subTotal;
        ImageButton btnDelete;
        ConstraintLayout bg;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            namaProduk = itemView.findViewById(R.id.itemCart_NamaProduk);
            hargaProduk = itemView.findViewById(R.id.itemCart_HargaProduk);
            jumlahBeli = itemView.findViewById(R.id.itemCart_Jumlah);
            subTotal = itemView.findViewById(R.id.itemCart_SubTotal);
            btnDelete = itemView.findViewById(R.id.btnHapusCart);

        }
    }

}
