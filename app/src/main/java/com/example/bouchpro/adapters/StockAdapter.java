package com.example.bouchpro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bouchpro.R;
import com.example.bouchpro.models.Produit;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private List<Produit> produitList;

    // LE CONSTRUCTEUR (Celui qui manquait dans ton erreur)
    public StockAdapter(List<Produit> produitList) {
        this.produitList = produitList;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Produit p = produitList.get(position);

        holder.txtNom.setText(p.getNom());
        holder.txtDate.setText(p.getDate());

        // --- CALCUL DU TOTAL ---
        double total = p.getStock() * p.getPrix();

        // Affichage détaillé : "50 KG x 60 DH = 3000 DH"
        String detail = p.getStock() + " KG x " + p.getPrix() + " DH";
        String totalStr = "Total: " + String.format("%.2f", total) + " DH";

        // On affiche tout cela dans le champ info
        holder.txtInfo.setText(detail + "\n" + totalStr);

        // Optionnel : Mettre le total en bleu pour qu'il se voie bien
        holder.txtInfo.setTextColor(android.graphics.Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return produitList.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtDate, txtInfo;
        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            // On réutilise des IDs simples pour l'exemple
            txtNom = itemView.findViewById(R.id.txtTypeTrans);
            txtDate = itemView.findViewById(R.id.txtDateTrans);
            txtInfo = itemView.findViewById(R.id.txtMontantTrans);
        }
    }
}