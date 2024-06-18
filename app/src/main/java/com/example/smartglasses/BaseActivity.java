package com.example.smartglasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int REQUEST_SELECT_LANGUAGE = 1;
    private TextToSpeech textToSpeech;
    private boolean isTextToSpeechInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        // Initialiser le TextToSpeech
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Configuration de la langue pour la synthèse vocale
            SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
            int result = textToSpeech.setLanguage(Locale.forLanguageTag(prefs.getString("My_Lang", "en")));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Gérer le cas où la langue n'est pas supportée
                Log.e("TextToSpeech", "La langue n'est pas supportée");
            } else {
                isTextToSpeechInitialized = true;
                // Appeler announceAllTexts ici si vous souhaitez que la synthèse vocale commence immédiatement après l'initialisation
                announceAllTexts((ViewGroup) getWindow().getDecorView());
            }
        } else {
            // Gérer le cas où l'initialisation a échoué
            Log.e("TextToSpeech", "Échec de l'initialisation de TextToSpeech");
        }
    }

    @Override
    protected void onDestroy() {
        // Arrêter et libérer les ressources du TextToSpeech lors de la destruction de l'activité
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_language:
                Intent intent = new Intent(BaseActivity.this, SelectLanguageActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_LANGUAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String languageCode = prefs.getString("My_Lang", "en");
        setLocale(languageCode);
    }

    private void setLocale(String languageCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LANGUAGE && resultCode == RESULT_OK) {
            Intent intent = new Intent(BaseActivity.this, BaseActivity.this.getClass());
            loadLocale();
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTextToSpeechInitialized) {
            addCustomClickListener((ViewGroup) getWindow().getDecorView());
            announceAllTexts((ViewGroup) getWindow().getDecorView());
        }
    }

    private void announceAllTexts(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                String text = textView.getText().toString();
                if (!text.isEmpty()) {
                    speak(text);
                }
            } else if (child instanceof Button) {
                Button button = (Button) child;
                String text = button.getText().toString();
                if (!text.isEmpty()) {
                    speak(text);
                }
            } else if (child instanceof ViewGroup) {
                announceAllTexts((ViewGroup) child);  // Appel récursif pour les ViewGroups imbriqués
            }
        }
    }

    private void speak(String text) {
        if (isTextToSpeechInitialized && textToSpeech != null && !text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null); // Utilisez QUEUE_ADD
        } else {
            Log.e("TextToSpeech", "TextToSpeech n'est pas initialisé ou le texte est vide");
        }
    }
    private void speakButton(String text) {
        if (isTextToSpeechInitialized && textToSpeech != null && !text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); // Utilisez QUEUE_ADD
        } else {
            Log.e("TextToSpeech", "TextToSpeech n'est pas initialisé ou le texte est vide");
        }
    }
    private void addCustomClickListener(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                String text = button.getText().toString();
                if (!text.isEmpty()) {
                    View.OnClickListener originalClickListener = getOriginalClickListener(button);
                    button.setOnClickListener(new CustomClickListener(button, text, originalClickListener));
                }
            } else if (child instanceof ViewGroup) {
                addCustomClickListener((ViewGroup) child);  // Appel récursif pour les ViewGroups imbriqués
            }
        }
    }

    private View.OnClickListener getOriginalClickListener(Button button) {
        try {
            // Utiliser la réflexion pour obtenir le OnClickListener original du bouton
            Field listenerInfoField = View.class.getDeclaredField("mListenerInfo");
            listenerInfoField.setAccessible(true);
            Object listenerInfo = listenerInfoField.get(button);
            Field onClickListenerField = listenerInfo.getClass().getDeclaredField("mOnClickListener");
            onClickListenerField.setAccessible(true);
            return (View.OnClickListener) onClickListenerField.get(listenerInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class CustomClickListener implements View.OnClickListener {

        private final Button button;
        private final String buttonText;
        private final View.OnClickListener originalClickListener;
        private boolean isDoubleClick = false;

        CustomClickListener(Button button, String buttonText, View.OnClickListener originalClickListener) {
            this.button = button;
            this.buttonText = buttonText;
            this.originalClickListener = originalClickListener;
        }

        @Override
        public void onClick(View v) {
            // Ensuite, traiter le clic personnalisé et lire le texte
            if (!isDoubleClick) {
                isDoubleClick = true;
                speakButton(buttonText + " " + getString(R.string.button_clique));
                v.postDelayed(() -> isDoubleClick = false, 1000); // Réinitialiser après 1 seconde
            }
            else {
                originalClickListener.onClick(v);
            }

        }
    }
}
