package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.adapters.StockRespoAdapter;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Respo;

import java.util.List;

public class StockRespoActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_respo);

        // --- FIX TECHNIQUE : Empêche la barre rouge de cacher le titre ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        db = DatabaseHelper.getInstance(this);
        rv = findViewById(R.id.rvRespoStock);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Chargement des données
        List<Respo> list = db.getAllRespos();

        if (list.isEmpty()) {
            Toast.makeText(this, "Veuillez d'abord créer un employé dans 'Gérer le personnel'", Toast.LENGTH_LONG).show();
        } else {
            StockRespoAdapter adapter = new StockRespoAdapter(list);
            rv.setAdapter(adapter);
        }

        // Configuration de la barre de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sélection Responsable");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Retourne à l'accueil
        return true;
    }
}