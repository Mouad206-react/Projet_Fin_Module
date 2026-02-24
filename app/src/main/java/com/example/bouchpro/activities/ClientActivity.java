package com.example.bouchpro.activities;

import android.content.Intent;
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
import com.example.bouchpro.adapters.ClientAdapter;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper myDb;
    private ClientAdapter adapter;
    private List<Client> listeComplete;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // --- FIX : Empêche la barre de retour de cacher le haut du contenu ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        myDb = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
        Button btn = findViewById(R.id.btnAllerAjout);

        // Navigation vers l'ajout
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(ClientActivity.this, AddClientActivity.class);
            startActivity(intent);
        });

        // Configuration de la recherche
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listeComplete != null) {
                    filtrerClients(newText);
                }
                return true;
            }
        });

        // Configurer la barre de titre et la flèche retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Liste des Clients");
        }
    }

    private void filtrerClients(String texte) {
        List<Client> listeFiltrée = new ArrayList<>();
        for (Client client : listeComplete) {
            // Recherche par nom ou téléphone
            if (client.getNom().toLowerCase().contains(texte.toLowerCase()) ||
                    client.getTelephone().contains(texte)) {
                listeFiltrée.add(client);
            }
        }
        if (adapter != null) {
            adapter.updateList(listeFiltrée);
        }
    }

    private void refreshList() {
        listeComplete = myDb.getAllClients();
        adapter = new ClientAdapter(listeComplete);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // On rafraîchit la liste à chaque retour sur cette page
        refreshList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Retourne à l'accueil
        return true;
    }
}