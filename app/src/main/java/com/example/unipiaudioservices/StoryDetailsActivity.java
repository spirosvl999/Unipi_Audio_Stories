package com.example.unipiaudioservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class StoryDetailsActivity extends AppCompatActivity {
    private TextView titleTextView, authorTextView, yearTextView, insideTextView;
    private ImageView storyImageView;
    private Button playButton;
    private TextToSpeech textToSpeech;
    private String storyText;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String story_id, user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // Initialize Firestore & Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "guest";

        // Get data from intent
        Intent intent = getIntent();
        story_id = intent.getStringExtra("Tittle"); // Assuming Title is the story ID
        storyText = intent.getStringExtra("Inside"); // Initialize class variable

        // Initialize UI elements
        titleTextView = findViewById(R.id.tittleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        yearTextView = findViewById(R.id.yearTextView);
        insideTextView = findViewById(R.id.insideTextView);
        storyImageView = findViewById(R.id.storyImageView);
        playButton = findViewById(R.id.playButton);

        // Set data to UI
        String title = intent.getStringExtra("Tittle");
        String author = intent.getStringExtra("Author");
        long year = intent.getLongExtra("Year_Created", 0);
        String photoUrl = intent.getStringExtra("PhotoUrl");

        titleTextView.setText(title);
        authorTextView.setText("By: " + author);
        yearTextView.setText("Year: " + year);
        insideTextView.setText(storyText);

        // Load image using Picasso
        Picasso.get().load(photoUrl).into(storyImageView);

        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: Language not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "TTS: Initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Play button click
        playButton.setOnClickListener(view -> {
            if (storyText != null && !storyText.isEmpty()) {
                speakStory();  // Play the story
                updateListenCount(story_id, user_id);
            } else {
                Toast.makeText(this, "Story text is empty or null", Toast.LENGTH_SHORT).show();
            }
        });

        Button viewMoreButton = findViewById(R.id.viewMoreButton);
        viewMoreButton.setOnClickListener(view -> {
            if (insideTextView.getMaxLines() == 5) {
                insideTextView.setMaxLines(Integer.MAX_VALUE); // Expand
                viewMoreButton.setText("View Less");
            } else {
                insideTextView.setMaxLines(5); // Collapse
                viewMoreButton.setText("View More");
            }
        });
    }

    // Increment listen_count in Firestore
    private void updateListenCount(String story_id, String user_id) {
        if (story_id == null || user_id == null) {
            Toast.makeText(this, "Invalid Story or User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure valid document ID (Firestore doesn't allow special characters like "/")
        String statDocId = user_id + "_" + story_id.replace(" ", "_");

        // Reference to the statistics document
        DocumentReference statRef = db.collection("Statistics").document(statDocId);

        statRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists -> Increment listen_count
                Long currentCount = documentSnapshot.getLong("listen_count");
                if (currentCount == null) currentCount = 0L;

                statRef.update("listen_count", currentCount + 1)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Listen count updated!", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to update count", Toast.LENGTH_SHORT).show()
                        );
            } else {
                // Document does NOT exist -> Create a new entry
                Map<String, Object> stats = new HashMap<>();
                stats.put("story_id", story_id);
                stats.put("user_id", user_id);
                stats.put("listen_count", 1); // Start with 1 listen

                statRef.set(stats)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "New stats created!", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to create stats", Toast.LENGTH_SHORT).show()
                        );
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error fetching stats", Toast.LENGTH_SHORT).show()
        );
    }

    // Method to speak the story
    private void speakStory() {
        if (textToSpeech == null) {
            Toast.makeText(this, "TTS not initialized!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!textToSpeech.isSpeaking()) {
            textToSpeech.speak(storyText, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Stop TTS when activity is destroyed
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}