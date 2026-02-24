package com.example.bouchpro.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bouchpro.R;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Respo;
import java.util.List;

public class RespoAdapter extends RecyclerView.Adapter<RespoAdapter.RespoViewHolder> {
    private List<Respo> list;

    public RespoAdapter(List<Respo> list) { this.list = list; }

    @NonNull
    @Override
    public RespoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respo, parent, false);
        return new RespoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RespoViewHolder holder, int position) {
        Respo r = list.get(position);
        holder.nom.setText(r.getNom());
        holder.role.setText(r.getRole());

        // --- LOGIQUE DE SUPPRESSION ---
        holder.btnDelete.setOnClickListener(v -> {
            DatabaseHelper db = DatabaseHelper.getInstance(v.getContext());

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer le personnel")
                    .setMessage("Voulez-vous vraiment supprimer " + r.getNom() + " ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // 1. Supprimer de la base de données
                        db.deleteRespo(r.getId());

                        // 2. Supprimer de la liste affichée et animer
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
    public int getItemCount() { return list.size(); }

    public static class RespoViewHolder extends RecyclerView.ViewHolder {
        TextView nom, role;
        ImageButton btnDelete; // Ajouté

        public RespoViewHolder(View v) {
            super(v);
            nom = v.findViewById(R.id.txtNomRespo);
            role = v.findViewById(R.id.txtRoleRespo);
            btnDelete = v.findViewById(R.id.btnDeleteRespo); // Lien avec l'ID du XML
        }
    }
}