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
    private String user_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the user_id (document name in "users" collection)
        if (auth.getCurrentUser() != null) {
            user_id = auth.getCurrentUser().getUid();
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
    }

    // Load user profile from Firestore using HashMap
    private void loadUserProfile() {
        db.collection("users").document(user_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve user data as a HashMap
                        Map<String, Object> userData = documentSnapshot.getData();
                        if (userData != null) {
                            String username = (String) userData.get("username");
                            String email = (String) userData.get("email");

                            // Set values in UI
                            usernameTextView.setText("Username: " + username);
                            emailTextView.setText("Email: " + email);
                        }
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
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxCount = 0;
                    String mostListenedStoryId = null;

                    // Find the story with max listens
                    for (var doc : queryDocumentSnapshots) {
                        Map<String, Object> statData = doc.getData();
                        if (statData != null) {
                            Long countValue = (Long) statData.get("listen_count");
                            if (countValue != null) {
                                int count = countValue.intValue();
                                if (count > maxCount) {
                                    maxCount = count;
                                    mostListenedStoryId = (String) statData.get("story_id");
                                }
                            }
                        }
                    }

                    // Fetch details of the most listened story if found
                    if (mostListenedStoryId != null) {
                        fetchStoryDetails(mostListenedStoryId, maxCount);
                    } else {
                        mostListenedStoryTextView.setText("Most Listened Story: None");
                        listenCountTextView.setText("Listen Count: 0");
                        storyImageView.setImageResource(R.drawable.ic_launcher_foreground); // Set a default image
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show()
                );
    }

    // Fetch story details by story ID
    private void fetchStoryDetails(String storyId, int listenCount) {
        db.collection("Stories").document(storyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve story data as a HashMap
                        Map<String, Object> storyData = documentSnapshot.getData();
                        if (storyData != null) {
                            String title = (String) storyData.get("Tittle");
                            String photoUrl = (String) storyData.get("PhotoUrl");

                            // Set story title and listen count in UI
                            mostListenedStoryTextView.setText("Most Listened Story: " + title);
                            listenCountTextView.setText("Listen Count: " + listenCount);

                            // Load story image using Picasso
                            if (photoUrl != null && !photoUrl.isEmpty()) {
                                Picasso.get().load(photoUrl).into(storyImageView);
                            } else {
                                storyImageView.setImageResource(R.drawable.ic_launcher_foreground);
                            }
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
