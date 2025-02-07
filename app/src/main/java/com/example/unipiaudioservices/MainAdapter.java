package com.example.unipiaudioservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.ViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position, @NonNull MainModel model) {
        holder.Tittle.setText(model.getTittle());
        holder.Author.setText(model.getName());
        holder.Year.setText(model.getYear());
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false));
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView Tittle, Author, Year;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            Tittle = itemView.findViewById(R.id.Tittle);
            Author = itemView.findViewById(R.id.Author);
            Year = itemView.findViewById(R.id.Year);
        }
    }
}
