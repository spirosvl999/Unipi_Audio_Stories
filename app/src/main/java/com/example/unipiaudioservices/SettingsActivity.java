package com.example.unipiaudioservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerLanguage;
    private MaterialButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Spinner setup (Language selection)
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Load current language
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String langCode = prefs.getString("lang_code", "en");

        // Set spinner selection based on saved language code
        int position = getPositionForLanguageCode(langCode);
        spinnerLanguage.setSelection(position, false); // Set selection without triggering listener

        // Confirm button action
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageToLoad = null;

                // Get selected language
                String selectedLanguage = spinnerLanguage.getSelectedItem().toString();
                Toast.makeText(SettingsActivity.this, "Language set to: " + selectedLanguage, Toast.LENGTH_SHORT).show();

                // Map selected language to language code
                if (selectedLanguage.equals("English")) {
                    languageToLoad = "en";
                } else if (selectedLanguage.equals("Ελληνικά")) {
                    languageToLoad = "el";
                } else if (selectedLanguage.equals("Shqiptare")) {
                    languageToLoad = "sq";
                }else if (selectedLanguage.equals("Italiano")) {
                    languageToLoad = "it";
                }


                if (languageToLoad != null) {
                    // Save the selected language
                    prefs.edit()
                            .putString("lang_code", languageToLoad)
                            .apply();

                    // Change locale immediately
                    setLocale(languageToLoad);

                    // Restart the activity to apply changes
                    Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_settings) {
                    // Already in Settings, do nothing
                } else if (itemId == R.id.nav_menu) {
                    startActivity(new Intent(SettingsActivity.this, Menu_Stories.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("app_prefs", MODE_PRIVATE);
        String langCode = prefs.getString("lang_code", "en"); // Default to English
        setLocale(newBase, langCode);
        super.attachBaseContext(newBase);
    }

    private void setLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
        }

        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    private void setLocale(String langCode) {
        setLocale(this, langCode);
    }

    private int getPositionForLanguageCode(String langCode) {
        switch (langCode) {
            case "en":
                return 0; // English is the first item in the array
            case "el":
                return 1; // Greek is the second item
            case "sq":
                return 2; // Albanian is the third item
            default:
                return 0; // Default to English
        }
    }
}