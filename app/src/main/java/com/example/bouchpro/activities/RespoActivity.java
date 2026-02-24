package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.adapters.RespoAdapter;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Respo;

import java.util.List;

public class RespoActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView rv;
    EditText edNom, edRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respo);

        // --- FIX : Empêche la barre de retour de cacher le haut du contenu ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        db = DatabaseHelper.getInstance(this);
        edNom = findViewById(R.id.editNomRespo);
        edRole = findViewById(R.id.editRoleRespo);
        rv = findViewById(R.id.recyclerRespo);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Action bouton Enregistrer
        findViewById(R.id.btnSaveRespo).setOnClickListener(v -> {
            String nom = edNom.getText().toString().trim();
            String role = edRole.getText().toString().trim();

            if(!nom.isEmpty() && !role.isEmpty()){
                db.addRespo(nom, role);
                edNom.setText("");
                edRole.setText("");
                chargerListe();
                Toast.makeText(this, "Personnel ajouté !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Veuillez remplir les deux champs", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuration de la barre de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestion du Personnel");
        }

        chargerListe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Retourne au menu principal
        return true;
    }

    private void chargerListe() {
        List<Respo> list = db.getAllRespos();
        rv.setAdapter(new RespoAdapter(list));
    }
}