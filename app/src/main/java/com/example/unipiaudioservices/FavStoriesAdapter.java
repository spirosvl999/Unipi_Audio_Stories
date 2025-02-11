package com.example.unipiaudioservices;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavStoriesAdapter extends FirestoreRecyclerAdapter<FavStoryModel, FavStoriesAdapter.ViewHolder> {

    public FavStoriesAdapter(@NonNull FirestoreRecyclerOptions<FavStoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FavStoryModel model) {
        holder.title.setText(model.getTittle());

        // Load image with Picasso
        Picasso.get()
                .load(model.getPhoto_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        // Fetch listen_count from Firestore
        fetchListenCount(holder.listenCount, model.getTittle());

        // Handle item clicks
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), StoryDetailsActivity.class);
            intent.putExtra("Title", model.getTittle());
            intent.putExtra("ImageUrl", model.getPhoto_url());
            view.getContext().startActivity(intent);
        });
    }

    // Fetch listen count from "Statistics" collection
    private void fetchListenCount(TextView listenCountView, String storyTitle) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Statistics")
                .document(userId)  // User's statistics document
                .collection("Stories")
                .document(storyTitle) // Specific story statistics
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long listenCount = documentSnapshot.getLong("listen_count");
                        if (listenCount != null) {
                            listenCountView.setText("Listen Count: " + listenCount);
                        } else {
                            listenCountView.setText("Listen Count: 0");
                        }
                    } else {
                        listenCountView.setText("Listen Count: 0");
                    }
                })
                .addOnFailureListener(e -> listenCountView.setText("Listen Count: 0"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_story, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView title, listenCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.storyImage);
            title = itemView.findViewById(R.id.Tittle);
            listenCount = itemView.findViewById(R.id.listenCount);
        }
    }
}
