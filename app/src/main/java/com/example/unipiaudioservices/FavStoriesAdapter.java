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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavStoriesAdapter extends FirestoreRecyclerAdapter<FavStoryModel, FavStoriesAdapter.ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firestore query.
     *
     * @param options The FirestoreRecyclerOptions containing the query and model class.
     */
    public FavStoriesAdapter(@NonNull FirestoreRecyclerOptions<FavStoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FavStoryModel model) {
        // Bind the data to the views
        holder.title.setText(model.getTitle());
        holder.author.setText(model.getAuthor());
        holder.year.setText(String.valueOf(model.getYearCreated()));

        // Load image with Picasso
        Picasso.get()
                .load(model.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        // Handle item clicks
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), StoryDetailsActivity.class);
            intent.putExtra("Title", model.getTitle());
            intent.putExtra("Author", model.getAuthor());
            intent.putExtra("YearCreated", model.getYearCreated());
            intent.putExtra("ImageUrl", model.getImageUrl());
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_story, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // UI elements for each item
        CircleImageView imageView;
        TextView title, author, year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the UI components
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.tittleTextView);
            author = itemView.findViewById(R.id.authorTextView);
            year = itemView.findViewById(R.id.yearTextView);
        }
    }
}
