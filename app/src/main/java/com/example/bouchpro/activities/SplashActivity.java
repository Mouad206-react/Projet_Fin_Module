package com.example.bouchpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bouchpro.MainActivity;
import com.example.bouchpro.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Cacher la barre du haut pour un effet plein écran
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Lancer la MainActivity après 3 secondes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Ferme la page de splash pour qu'on ne revienne pas dessus
            }
        }, 3000); // 3000 ms = 3 secondes
    }
}