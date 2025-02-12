package com.example.unipiaudioservices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView, emailTextView, mostListenedStoryTextView, listenCountTextView;
    private CircleImageView storyImageView;
    private Button favStoriesButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the logged-in user's ID
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize UI elements
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mostListenedStoryTextView = findViewById(R.id.mostListenedTextView);
        listenCountTextView = findViewById(R.id.listenCountTextView);
        storyImageView = findViewById(R.id.storyImageView);
        favStoriesButton = findViewById(R.id.favStoriesButton);

        // Load user data
        loadUserProfile();

        // Load most listened story
        loadMostListenedStory();

        // Button to go to Favorite Stories
        favStoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavStoriesActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_menu) {
                    startActivity(new Intent(ProfileActivity.this, Menu_Stories.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Already in Profile activity dont do nothing
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                    return true;
                }

                return false;
            }
        });

        // Highlight the Profile tab since we are on the ProfileActivity
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    // Load user profile from Firestore
    private void loadUserProfile() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("Username");
                        String email = documentSnapshot.getString("Email");

                        // Set values in UI
                        usernameTextView.setText("Username: " + (username != null ? username : "N/A"));
                        emailTextView.setText("Email: " + (email != null ? email : "N/A"));
                    } else {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                );
    }

    // Load most listened story data
    private void loadMostListenedStory() {
        db.collection("Statistics")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxCount = 0;
                    String mostListenedStoryTitle = null;

                    // Find the story with the highest listen count
                    for (var doc : queryDocumentSnapshots) {
                        Long countValue = doc.getLong("listen_count");
                        String storyTitle = doc.getString("story_id"); // story_id is actually Tittle

                        if (countValue != null && storyTitle != null) {
                            int count = countValue.intValue();
                            if (count > maxCount) {
                                maxCount = count;
                                mostListenedStoryTitle = storyTitle;
                            }
                        }
                    }

                    // Fetch story details if found
                    if (mostListenedStoryTitle != null) {
                        fetchStoryDetails(mostListenedStoryTitle, maxCount);
                    } else {
                        mostListenedStoryTextView.setText("Most Listened Story: None");
                        listenCountTextView.setText("Listen Count: 0");
                        storyImageView.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show()
                );
    }

    // Fetch story details by matching `Tittle` in the "Stories" collection
    private void fetchStoryDetails(String storyTitle, int listenCount) {
        db.collection("Stories")
                .whereEqualTo("Tittle", storyTitle) // Match Tittle with story_id from Statistics
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Get the first matching document
                        var documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String title = documentSnapshot.getString("Tittle");
                        String photoUrl = documentSnapshot.getString("photo_url");

                        // Set story title and listen count in UI
                        mostListenedStoryTextView.setText("Most Listened Story: " + title);
                        listenCountTextView.setText("Listen Count: " + listenCount);

                        // Load story image using Picasso
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Picasso.get().load(photoUrl).into(storyImageView);
                        } else {
                            storyImageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    } else {
                        Toast.makeText(this, "Story details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load story details", Toast.LENGTH_SHORT).show()
                );
    }
}