package com.example.unipiaudioservices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView, emailTextView, mostListenedStoryTextView, listenCountTextView;
    private ImageView mostListenedStoryImage;
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
        user_id = auth.getCurrentUser().getUid();

        // Initialize UI elements
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mostListenedStoryTextView = findViewById(R.id.mostListenedTextView);
        listenCountTextView = findViewById(R.id.listenCountTextView);
        mostListenedStoryImage = findViewById(R.id.storyImageView);
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

    private void loadUserProfile() {
        db.collection("users").document(user_id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String username = documentSnapshot.getString("username");
                String email = documentSnapshot.getString("email");

                usernameTextView.setText(username);
                emailTextView.setText(email);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadMostListenedStory() {
        db.collection("Statistics")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxCount = 0;
                    String mostListenedStoryId = null;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        int count = doc.getLong("listen_count").intValue();
                        if (count > maxCount) {
                            maxCount = count;
                            mostListenedStoryId = doc.getString("story_id");
                        }
                    }

                    if (mostListenedStoryId != null) {
                        fetchStoryDetails(mostListenedStoryId, maxCount);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show()
                );
    }

    private void fetchStoryDetails(String storyId, int listenCount) {
        db.collection("Stories").document(storyId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String title = documentSnapshot.getString("Tittle");
                String photoUrl = documentSnapshot.getString("PhotoUrl");

                mostListenedStoryTextView.setText(title);
                listenCountTextView.setText("Listen Count: " + listenCount);

                // Load image with Picasso
                Picasso.get().load(photoUrl).into(mostListenedStoryImage);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load story details", Toast.LENGTH_SHORT).show()
        );
    }
}

