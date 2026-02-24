package com.example.bouchpro.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.List;

public class StockHistoryActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseHelper db;
    private int respoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        // --- SOLUTION TECHNIQUE : Empêche la barre de retour de cacher le contenu ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        db = DatabaseHelper.getInstance(this);
        rv = findViewById(R.id.rvStockHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));

        respoId = getIntent().getIntExtra("RESPO_ID", -1);
        String respoNom = getIntent().getStringExtra("RESPO_NOM");

        // Configuration de la barre de titre
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Stock : " + (respoNom != null ? respoNom : "Historique"));
        }

        // Clics sur les filtres
        findViewById(R.id.btnFiltreJour).setOnClickListener(v -> chargerHistorique(respoId, "JOUR"));
        findViewById(R.id.btnFiltreSemaine).setOnClickListener(v -> chargerHistorique(respoId, "SEMAINE"));
        findViewById(R.id.btnFiltreMois).setOnClickListener(v -> chargerHistorique(respoId, "MOIS"));
        findViewById(R.id.btnFiltreTout).setOnClickListener(v -> chargerHistorique(respoId, "TOUT"));
        findViewById(R.id.btnFiltreCalendrier).setOnClickListener(v -> ouvrirCalendrier());

        // Chargement par défaut
        chargerHistorique(respoId, "TOUT");
    }

    private void chargerHistorique(int id, String filtre) {
        List<Produit> list = db.getProduitsFiltre(id, filtre);
        StockAdapter adapter = new StockAdapter(list);
        rv.setAdapter(adapter);

        if(list.isEmpty()) {
            Toast.makeText(this, "Aucun stock pour cette période", Toast.LENGTH_SHORT).show();
        }
    }

    private void ouvrirCalendrier() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    String dateSql = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day);
                    chargerDateSpecifique(respoId, dateSql);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void chargerDateSpecifique(int id, String dateSql) {
        List<Produit> list = db.getProduitsByDateSpecifique(id, dateSql);
        rv.setAdapter(new StockAdapter(list));
        if(list.isEmpty()) Toast.makeText(this, "Rien trouvé pour cette date", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}