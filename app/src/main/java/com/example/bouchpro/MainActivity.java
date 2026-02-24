package com.example.bouchpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bouchpro.activities.ClientActivity;
import com.example.bouchpro.activities.RespoActivity;
import com.example.bouchpro.activities.StockRespoActivity;
import com.example.bouchpro.activities.TransactionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- FIX : GESTION DES MARGES AUTOMATIQUES (ANTI-RECOUVREMENT) ---
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // --- NAVIGATION ---

        Button btnClients = findViewById(R.id.btnClients);
        Button btnTransactions = findViewById(R.id.btnTransactions);
        Button btnProduits = findViewById(R.id.btnProduits);
        Button btnRespo = findViewById(R.id.btnMenuRespo);

        btnClients.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ClientActivity.class));
        });

        btnTransactions.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TransactionActivity.class));
        });

        btnProduits.setOnClickListener(v -> {
            // Ouvre la liste des responsables pour ajouter du stock
            startActivity(new Intent(MainActivity.this, StockRespoActivity.class));
        });

        btnRespo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RespoActivity.class));
        });
    }
}