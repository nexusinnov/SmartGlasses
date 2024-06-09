package com.example.smartglasses;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bouton "A Propos de nous"
        Button aboutUsButton = findViewById(R.id.apropos_button);
        aboutUsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });

        // Bouton "Se connecter"
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChoixActivity.class);
            startActivity(intent);
        });
    }
}
