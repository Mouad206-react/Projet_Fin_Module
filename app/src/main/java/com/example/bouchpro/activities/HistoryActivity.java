package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.adapters.TransactionAdapter;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Transaction;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // --- FIX : Empêche la barre rouge de cacher le haut du contenu ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Récupération des infos envoyées par la page précédente
        int clientId = getIntent().getIntExtra("CLIENT_ID", -1);
        String clientNom = getIntent().getStringExtra("CLIENT_NOM");

        // 2. Configuration de la barre de titre
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Historique : " + clientNom);
        }

        // 3. Mise à jour du texte dans le layout
        TextView txtTitre = findViewById(R.id.txtTotalHistorique);
        txtTitre.setText("Opérations de " + clientNom);

        // 4. Configuration du RecyclerView
        RecyclerView rv = findViewById(R.id.recyclerHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 5. Récupération des données depuis SQLite
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<Transaction> transactions = db.getTransactionsByClient(clientId);

        // 6. Liaison avec l'Adapter
        TransactionAdapter adapter = new TransactionAdapter(transactions);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Retourne à la liste des clients
        return true;
    }
}