package com.example.unipiaudioservices;

import android.content.Intent;
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

public class FavStoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavStoriesAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_stories);

        // Set up Firestore database
        db = FirebaseFirestore.getInstance();

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create query to fetch favorite stories
        Query query = db.collection("FavoriteStories");

        // Create FirestoreRecyclerOptions with query
        FirestoreRecyclerOptions<FavStoryModel> options = new FirestoreRecyclerOptions.Builder<FavStoryModel>()
                .setQuery(query, FavStoryModel.class)
                .build();

        // Set up adapter with options
        adapter = new FavStoriesAdapter(options);
        recyclerView.setAdapter(adapter);

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);  // Make sure this is correct

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the ID of the clicked item

                if (itemId == R.id.nav_menu) {
                    // Already in Menu_Stories, do nothing
                    startActivity(new Intent(FavStoriesActivity.this, Menu_Stories.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Go to ProfileActivity
                    startActivity(new Intent(FavStoriesActivity.this, ProfileActivity.class));
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
        // Start listening to Firestore changes
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to avoid memory leaks
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
