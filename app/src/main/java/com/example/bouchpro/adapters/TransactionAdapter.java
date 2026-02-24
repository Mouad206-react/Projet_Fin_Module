package com.example.bouchpro.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransViewHolder> {
    private List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) { this.list = list; }

    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
        Transaction t = list.get(position);
        holder.txtDate.setText(t.getDate());
        holder.txtMontant.setText(String.format("%.2f DH", t.getMontant()));
        holder.txtType.setText(t.getType());

        // LOGIQUE DES COULEURS
        if (t.getType().equals("CREDIT")) {
            holder.txtType.setTextColor(Color.RED);
            holder.txtMontant.setTextColor(Color.RED);
        } else {
            holder.txtType.setTextColor(Color.parseColor("#2E7D32")); // Vert foncé
            holder.txtMontant.setTextColor(Color.parseColor("#2E7D32"));
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class TransViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtType, txtMontant;
        public TransViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDateTrans);
            txtType = itemView.findViewById(R.id.txtTypeTrans);
            txtMontant = itemView.findViewById(R.id.txtMontantTrans);
        }
    }
}