package com.example.unipiaudioservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FavStoriesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FavStoriesAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_stories);

        // Firebase instances
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        recyclerView = findViewById(R.id.favStoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Query Statistics collection for user's most listened stories
            Query query = db.collection("Statistics")
                    .whereEqualTo("user_id", userId) // Filter by logged-in user
                    .orderBy("listen_count", Query.Direction.DESCENDING); // Most listened first

            FirestoreRecyclerOptions<FavStoryModel> options = new FirestoreRecyclerOptions.Builder<FavStoryModel>()
                    .setQuery(query, FavStoryModel.class)
                    .build();

            adapter = new FavStoriesAdapter(options);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_menu) {
                    startActivity(new Intent(FavStoriesActivity.this, Menu_Stories.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(FavStoriesActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Highlight the correct tab
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
