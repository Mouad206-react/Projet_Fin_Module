package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.database.DatabaseHelper;

public class ProduitActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit);

        // --- FIX : Empêche la barre de retour de cacher le haut du contenu ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation de la base de données
        db = DatabaseHelper.getInstance(this);

        // Configuration du RecyclerView
        rv = findViewById(R.id.recyclerProduits);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Bouton Ajouter
        Button btnAdd = findViewById(R.id.btnAjouterNouveauProduit);
        btnAdd.setOnClickListener(v -> {
            // Ici tu peux ouvrir un dialogue pour créer un nouveau type de viande
            Toast.makeText(this, "Fonctionnalité : Créer un nouveau produit", Toast.LENGTH_SHORT).show();
        });

        // Configuration de la barre de retour et du titre
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestion du Stock");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Retourne à l'accueil
        return true;
    }
}