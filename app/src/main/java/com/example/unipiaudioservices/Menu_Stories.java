package com.example.unipiaudioservices;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unipiaudioservices.databinding.ActivityMenuStoriesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Query the "Stories" collection
        Query query = db.collection("Stories");

        // FirestoreRecyclerOptions for MainModel
        FirestoreRecyclerOptions<MainModel> options = new FirestoreRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class) // Pass the query and model class
                .build();

        // Set up the adapter
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
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
