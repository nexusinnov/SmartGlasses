package com.example.smartglasses;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class VitesseActivity extends BaseActivity {


    private static final String TAG = "Picture";
    private SeekBar seekBar;
    private TextView textViewSpeed;
    private Button buttonStart;
    private String functionality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitesse);

        Intent intent = getIntent();
        functionality = intent.getStringExtra("functionality");

        seekBar = findViewById(R.id.seek_bar_speed);
        textViewSpeed = findViewById(R.id.text_view_speed);
        buttonStart = findViewById(R.id.button_start);

        seekBar.setProgress(50);
        textViewSpeed.setText(getString(R.string.speed_percentage, seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSpeed.setText(getString(R.string.speed_percentage, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendScriptName(functionality);
            }
        });
    }
    private void sendScriptName(String scriptName) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.137.76:5000/execute";
        JSONObject jsonObject = new JSONObject();

        // Get the current language code
        String language = Locale.getDefault().getLanguage();

        try {
            jsonObject.put("script_name", scriptName);
            jsonObject.put("language", language); // Add language to JSON object
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "JSON Object: " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response received: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Request error: " + error.toString());
            }
        });

        queue.add(jsonObjectRequest);
        Log.d(TAG, "Request sent: " + scriptName);
    }

}

