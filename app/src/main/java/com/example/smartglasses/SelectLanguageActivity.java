package com.example.smartglasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SelectLanguageActivity extends BaseActivity {

    private Button buttonArabic;
    private Button buttonFrench;
    private Button buttonEnglish;
    private Button buttonNext;
    private Button selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        buttonArabic = findViewById(R.id.button_arabic);
        buttonFrench = findViewById(R.id.button_french);
        buttonEnglish = findViewById(R.id.button_english);
        buttonNext = findViewById(R.id.button_next);

        View.OnClickListener languageClickListener = v -> {
            resetButtonBackground();
            v.setBackgroundColor(getResources().getColor(R.color.gray));
            selectedButton = (Button) v;

            String languageCode = "";
            switch (v.getId()) {
                case R.id.button_arabic:
                    languageCode = "ar";
                    break;
                case R.id.button_french:
                    languageCode = "fr";
                    break;
                case R.id.button_english:
                    languageCode = "en";
                    break;
            }
            saveLanguagePreference(languageCode);
            setLocale(languageCode);
            setResult(RESULT_OK);
            finish();
        };

        buttonArabic.setOnClickListener(languageClickListener);
        buttonFrench.setOnClickListener(languageClickListener);
        buttonEnglish.setOnClickListener(languageClickListener);

    }

    private void resetButtonBackground() {
        buttonArabic.setBackgroundColor(getResources().getColor(R.color.blue));
        buttonFrench.setBackgroundColor(getResources().getColor(R.color.blue));
        buttonEnglish.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    private void setLocale(String languageCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();
    }
}
