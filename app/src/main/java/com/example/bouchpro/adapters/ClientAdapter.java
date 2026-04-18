package com.example.bouchpro.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bouchpro.R;
import com.example.bouchpro.activities.HistoryActivity;
import com.example.bouchpro.activities.TransactionActivity;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Client;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> clientList;

    public ClientAdapter(List<Client> clientList) {
        this.clientList = clientList;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);
        DatabaseHelper db = DatabaseHelper.getInstance(holder.itemView.getContext());

        holder.txtNom.setText(client.getNom());

        // --- CALCUL ET AFFICHAGE DU SOLDE ---
        double solde = db.getClientSolde(client.getId());
        holder.txtSolde.setText(String.format("%.2f DH", solde));

        // --- BOUTON TRANSACTION (Payer) ---
        holder.btnTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TransactionActivity.class);
            intent.putExtra("CLIENT_ID", client.getId());
            intent.putExtra("CLIENT_NOM", client.getNom());
            v.getContext().startActivity(intent);
        });

        // --- BOUTON MODIFIER (Crayon) ---
        holder.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Modifier le client");

            LinearLayout layout = new LinearLayout(v.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 20, 50, 20);

            final EditText inputNom = new EditText(v.getContext());
            inputNom.setHint("Nom");
            inputNom.setText(client.getNom());
            layout.addView(inputNom);

            final EditText inputTel = new EditText(v.getContext());
            inputTel.setHint("Téléphone");
            inputTel.setText(client.getTelephone());
            inputTel.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
            layout.addView(inputTel);

            builder.setView(layout);

            builder.setPositiveButton("Enregistrer", (dialog, which) -> {
                String nouveauNom = inputNom.getText().toString().trim();
                String nouveauTel = inputTel.getText().toString().trim();

                if (!nouveauNom.isEmpty()) {
                    db.updateClient(client.getId(), nouveauNom, nouveauTel);

                    // Mise à jour de l'objet dans la liste
                    client.setNom(nouveauNom);
                    client.setTelephone(nouveauTel);

                    notifyItemChanged(holder.getAdapterPosition());
                    Toast.makeText(v.getContext(), "Modifié avec succès !", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Annuler", null);
            builder.show();
        });

        // --- BOUTON INFOS (L'icône "i") ---
        holder.btnDetails.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Fiche Client")
                    .setMessage("Nom : " + client.getNom() +
                            "\nTéléphone : " + client.getTelephone() +
                            "\nDette actuelle : " + String.format("%.2f", solde) + " DH")
                    .setPositiveButton("Voir l'historique", (dialog, which) -> {
                        Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                        intent.putExtra("CLIENT_ID", client.getId());
                        intent.putExtra("CLIENT_NOM", client.getNom());
                        v.getContext().startActivity(intent);
                    })
                    .setNegativeButton("Fermer", null)
                    .show();
        });

        // --- BOUTON SUPPRIMER (Poubelle) ---
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Supprimer le client")
                    .setMessage("Voulez-vous vraiment supprimer " + client.getNom() + " ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        db.deleteClient(client.getId());
                        int actualPosition = holder.getAdapterPosition();
                        clientList.remove(actualPosition);
                        notifyItemRemoved(actualPosition);
                        notifyItemRangeChanged(actualPosition, clientList.size());
                        Toast.makeText(v.getContext(), "Client supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public void updateList(List<Client> newList) {
        this.clientList = newList;
        notifyDataSetChanged();
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtSolde;
        Button btnTransaction;
        ImageButton btnDetails, btnDelete, btnEdit; // <-- AJOUTÉ ICI

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomClient);
            txtSolde = itemView.findViewById(R.id.txtSoldeClient);
            btnTransaction = itemView.findViewById(R.id.btnTransactions);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEditClient); // <-- AJOUTÉ ICI
        }
    }
}