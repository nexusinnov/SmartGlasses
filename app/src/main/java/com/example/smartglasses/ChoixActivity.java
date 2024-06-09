package com.example.smartglasses;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ChoixActivity extends BaseActivity {

    private Button buttonCouleur;
    private Button buttonObjet;
    private Button buttonNext;
    private Button buttonText;
    private static final String TAG = "ChoixActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);

        buttonCouleur = findViewById(R.id.button_couleur);
        buttonObjet = findViewById(R.id.button_objet);
        buttonNext = findViewById(R.id.button_next);
        buttonText = findViewById(R.id.button_text);
        buttonText.setOnClickListener(v -> {
            // Log the button click
            Log.d(TAG, "Button Text clicked");

            // Change button background color
            buttonCouleur.getBackground().setColorFilter(getResources().getColor(R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            buttonObjet.getBackground().clearColorFilter();

            // Show the next button
            buttonNext.setVisibility(View.VISIBLE);

            // Send the script name to the next intent
            Intent intent = new Intent(ChoixActivity.this, VitesseActivity.class);
            intent.putExtra("functionality", "detectText");
            startActivity(intent);
        });

        buttonCouleur.setOnClickListener(v -> {
            // Log the button click
            Log.d(TAG, "Button Couleur clicked");

            // Change button background color
            buttonCouleur.getBackground().setColorFilter(getResources().getColor(R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            buttonObjet.getBackground().clearColorFilter();

            // Show the next button
            buttonNext.setVisibility(View.VISIBLE);

            // Send the script name to the next intent
            Intent intent = new Intent(ChoixActivity.this, VitesseActivity.class);
            intent.putExtra("functionality", "detectColor");
            startActivity(intent);
        });
        buttonObjet.setOnClickListener(v -> {
            // Log the button click
            Log.d(TAG, "Button Objet clicked");

            // Change button background color
            buttonObjet.getBackground().setColorFilter(getResources().getColor(R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            buttonCouleur.getBackground().clearColorFilter();

            // Show the next button
            buttonNext.setVisibility(View.VISIBLE);

            // Send the script name to the next intent
            Intent intent = new Intent(ChoixActivity.this, VitesseActivity.class);
            intent.putExtra("functionality", "detectObject");
            startActivity(intent);
        });

        buttonNext.setOnClickListener(v -> {
            // Launch the next activity
            Intent intent = new Intent(ChoixActivity.this, VitesseActivity.class);
            startActivity(intent);
        });
    }

}
