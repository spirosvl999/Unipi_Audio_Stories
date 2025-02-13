package com.example.unipiaudioservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Menu_Stories extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_stories);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseFirestore.getInstance()
                .collection("Stories");

        FirestoreRecyclerOptions<MainModel> options = new FirestoreRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the ID of the clicked item

                if (itemId == R.id.nav_menu) {
                    // Already in Menu_Stories, do nothing
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Go to ProfileActivity
                    startActivity(new Intent(Menu_Stories.this, ProfileActivity.class));
                    return true;
                }else if (itemId ==R.id.nav_settings){
                    startActivity(new Intent(Menu_Stories.this, SettingsActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Highlight the correct tab (already in Menu_Stories)
        bottomNavigationView.setSelectedItemId(R.id.nav_menu); // This should be correct
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in Firestore
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to avoid memory leaks
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }
}
