package com.example.unipiaudioservices;

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

public class MainAdapter extends FirestoreRecyclerAdapter<MainModel, MainAdapter.ViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firestore query.
     *
     * @param options The FirestoreRecyclerOptions containing the query and model class.
     */
    public MainAdapter(@NonNull FirestoreRecyclerOptions<MainModel> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position, @NonNull MainModel model)
    {
        holder.Tittle.setText(model.getTittle());
        holder.Author.setText(model.getAuthor());
        holder.Year.setText(String.valueOf(model.getYear_Created()));

        Picasso.get()
                .load(model.getPhoto_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);
    }



    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imageView;
        TextView Tittle, Author, Year;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Initialize the UI components
            imageView = itemView.findViewById(R.id.imageView);
            Tittle = itemView.findViewById(R.id.Tittle);
            Author = itemView.findViewById(R.id.Author);
            Year = itemView.findViewById(R.id.Year);
        }
    }
}
