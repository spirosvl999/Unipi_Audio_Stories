package com.example.unipiaudioservices;

import android.content.Intent;
import android.os.Bundle;
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
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Confirm button action
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected language
                String selectedLanguage = spinnerLanguage.getSelectedItem().toString();

                // Apply the changes (this can include saving the language selection, updating the UI, etc.)
                Toast.makeText(SettingsActivity.this, "Language set to: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                // You can add further logic here to save this preference (e.g., using SharedPreferences)

                // Optionally, you can restart the activity to apply changes immediately
                // recreate();  // This is just an example if you need to reload the UI
            }
        });

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_settings) {
                    return true; // Already in Settings, do nothing
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
}
