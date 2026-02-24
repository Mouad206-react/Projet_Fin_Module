package com.example.bouchpro.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bouchpro.R;
import com.example.bouchpro.database.DatabaseHelper;
import com.example.bouchpro.models.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {
    Spinner spinnerClients;
    EditText editMontant;
    RadioGroup radioGroup;
    DatabaseHelper myDb;
    List<Client> listClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // --- FIX TECHNIQUE : Empêche la barre rouge de cacher le haut du formulaire ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation
        myDb = DatabaseHelper.getInstance(this);
        spinnerClients = findViewById(R.id.spinnerClients);
        editMontant = findViewById(R.id.editMontant);
        radioGroup = findViewById(R.id.radioGroupType);
        Button btnSave = findViewById(R.id.btnEnregistrerTrans);

        chargerSpinner();

        // Récupérer le client si on vient de la liste des clients
        int idClientRecu = getIntent().getIntExtra("CLIENT_ID", -1);
        if (idClientRecu != -1) {
            selectionnerClientDansSpinner(idClientRecu);
        }

        // Action Enregistrer
        btnSave.setOnClickListener(v -> enregistrerTransaction());

        // Configuration de la barre de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nouvelle Opération");
        }
    }

    private void chargerSpinner() {
        listClients = myDb.getAllClients();
        if (listClients.isEmpty()) {
            Toast.makeText(this, "Aucun client trouvé !", Toast.LENGTH_LONG).show();
            return;
        }
        List<String> noms = new ArrayList<>();
        for (Client c : listClients) { noms.add(c.getNom()); }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, noms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClients.setAdapter(adapter);
    }

    private void selectionnerClientDansSpinner(int idClient) {
        for (int i = 0; i < listClients.size(); i++) {
            if (listClients.get(i).getId() == idClient) {
                spinnerClients.setSelection(i);
                break;
            }
        }
    }

    private void enregistrerTransaction() {
        if (spinnerClients.getSelectedItem() == null) {
            Toast.makeText(this, "Veuillez d'abord créer un client", Toast.LENGTH_SHORT).show();
            return;
        }

        int index = spinnerClients.getSelectedItemPosition();
        int clientId = listClients.get(index).getId();
        String montantStr = editMontant.getText().toString().trim();

        if (montantStr.isEmpty()) {
            Toast.makeText(this, "Entrez un montant", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant = Double.parseDouble(montantStr);
        String type = (radioGroup.getCheckedRadioButtonId() == R.id.radioCredit) ? "CREDIT" : "PAIEMENT";
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        boolean ok = myDb.addTransaction(clientId, type, montant, date);

        if (ok) {
            Toast.makeText(this, "Opération enregistrée !", Toast.LENGTH_SHORT).show();
            finish(); // Retourne à la page précédente
        } else {
            Toast.makeText(this, "Erreur d'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}