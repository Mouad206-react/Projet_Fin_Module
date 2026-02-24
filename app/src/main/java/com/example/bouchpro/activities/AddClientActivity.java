package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bouchpro.R;
import com.example.bouchpro.database.DatabaseHelper;

public class AddClientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        // --- SOLUTION POUR LE PROBLÈME D'AFFICHAGE (COUPURE DU HAUT) ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        DatabaseHelper myDb = DatabaseHelper.getInstance(this);
        EditText edNom = findViewById(R.id.edNom);
        EditText edTel = findViewById(R.id.edTel);
        Button btn = findViewById(R.id.btnSauvegarder);

        // Action de sauvegarde
        btn.setOnClickListener(v -> {
            String nom = edNom.getText().toString().trim();
            String tel = edTel.getText().toString().trim();

            if(!nom.isEmpty()) {
                myDb.addClient(nom, tel);
                Toast.makeText(this, "Client enregistré !", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuration de la barre de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nouveau Client");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}