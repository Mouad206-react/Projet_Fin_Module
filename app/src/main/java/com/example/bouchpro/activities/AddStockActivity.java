package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bouchpro.R;
import com.example.bouchpro.adapters.StockAdapter;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Produit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddStockActivity extends AppCompatActivity {

    private int respoId;
    private DatabaseHelper db;
    private List<Produit> sessionList = new ArrayList<>(); // Liste pour l'affichage en direct
    private StockAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        // Fix Marges
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = DatabaseHelper.getInstance(this);
        respoId = getIntent().getIntExtra("RESPO_ID", -1);
        String respoNom = getIntent().getStringExtra("RESPO_NOM");

        TextView txtRespo = findViewById(R.id.txtRespoName);
        txtRespo.setText("Responsable : " + respoNom);

        // Configuration du RecyclerView pour la session en cours
        rv = findViewById(R.id.rvSessionStock);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StockAdapter(sessionList); // On commence avec la liste vide
        rv.setAdapter(adapter);

        EditText etNom = findViewById(R.id.etNomProduit);
        EditText etPrix = findViewById(R.id.etPrixProduit);
        EditText etStock = findViewById(R.id.etQuantiteProduit);
        Button btnSave = findViewById(R.id.btnSaveStock);

        btnSave.setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            String prixStr = etPrix.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (!nom.isEmpty() && !prixStr.isEmpty() && !stockStr.isEmpty()) {
                double prix = Double.parseDouble(prixStr);
                double stock = Double.parseDouble(stockStr);

                // 1. Sauvegarder dans la base de données
                long idGenere = db.addProduitAndGetId(nom, prix, stock, date, respoId);

                if (idGenere != -1) {
                    // 2. Créer l'objet produit et l'ajouter à la liste de l'écran
                    Produit nouveauProduit = new Produit((int)idGenere, nom, prix, stock, date, respoId);
                    sessionList.add(0, nouveauProduit); // Ajouter en haut de la liste

                    // 3. Notifier l'adapter pour qu'il affiche la nouvelle ligne
                    adapter.notifyItemInserted(0);
                    rv.scrollToPosition(0); // Remonter en haut pour voir l'ajout

                    // 4. Vider les champs
                    etNom.setText(""); etPrix.setText(""); etStock.setText("");
                    etNom.requestFocus();
                    Toast.makeText(this, nom + " ajouté !", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Saisie du Stock");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}