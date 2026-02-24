package com.example.bouchpro.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bouchpro.R;
import com.example.bouchpro.activities.AddStockActivity;
import com.example.bouchpro.activities.StockHistoryActivity;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Respo;
import java.util.List;

public class StockRespoAdapter extends RecyclerView.Adapter<StockRespoAdapter.RespoViewHolder> {

    private List<Respo> list;

    public StockRespoAdapter(List<Respo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RespoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respo_stock, parent, false);
        return new RespoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RespoViewHolder holder, int position) {
        Respo respo = list.get(position);
        DatabaseHelper db = DatabaseHelper.getInstance(holder.itemView.getContext());

        holder.nom.setText(respo.getNom());
        holder.role.setText(respo.getRole());

        // Bouton AJOUTER STOCK (+)
        holder.btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), AddStockActivity.class);
            i.putExtra("RESPO_ID", respo.getId());
            i.putExtra("RESPO_NOM", respo.getNom());
            v.getContext().startActivity(i);
        });

        // Bouton VOIR HISTORIQUE (🔍)
        holder.btnDetails.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), StockHistoryActivity.class);
            i.putExtra("RESPO_ID", respo.getId());
            i.putExtra("RESPO_NOM", respo.getNom()); // Optionnel mais utile pour le titre
            v.getContext().startActivity(i);
        });

        // --- BOUTON SUPPRIMER (Poubelle rouge) ---
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer le responsable")
                    .setMessage("Voulez-vous supprimer " + respo.getNom() + " ?\nCela effacera aussi tous les produits qu'il a saisis.")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        // 1. Supprimer dans la base de données
                        db.deleteRespo(respo.getId());

                        // 2. Supprimer de la liste et animer
                        int actualPosition = holder.getAdapterPosition();
                        list.remove(actualPosition);
                        notifyItemRemoved(actualPosition);
                        notifyItemRangeChanged(actualPosition, list.size());

                        Toast.makeText(v.getContext(), "Responsable supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class RespoViewHolder extends RecyclerView.ViewHolder {
        TextView nom, role;
        Button btnAdd;
        ImageButton btnDetails, btnDelete;

        public RespoViewHolder(@NonNull View v) {
            super(v);
            nom = v.findViewById(R.id.txtNomRespo);
            role = v.findViewById(R.id.txtRoleRespo);
            btnAdd = v.findViewById(R.id.btnAddStock);
            btnDetails = v.findViewById(R.id.btnViewDetails);
            btnDelete = v.findViewById(R.id.btnDeleteRespo); // Lien avec ton nouvel ID XML
        }
    }
}