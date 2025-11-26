package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import activities.EditPropertyActivity;
import models.Property;

public class AdminPropertyRecyclerAdapter extends RecyclerView.Adapter<AdminPropertyRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Property> properties;
    private OnPropertyActionListener listener;

    public interface OnPropertyActionListener {
        void onDeleteProperty(int propertyId);
    }

    public AdminPropertyRecyclerAdapter(Context context, ArrayList<Property> properties, OnPropertyActionListener listener) {
        this.context = context;
        this.properties = properties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvPrice.setText("PKR " + String.format("%,.0f", property.getPrice()));
        holder.tvLocation.setText(property.getLocation());

        // Load first image using Glide
        loadPropertyImage(holder.ivImage, property);

        // Edit button click listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPropertyActivity.class);
            intent.putExtra("property_id", property.getId());
            context.startActivity(intent);
        });

        // Delete button click listener
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteProperty(property.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public void updateProperties(ArrayList<Property> newProperties) {
        this.properties = newProperties;
        notifyDataSetChanged();
    }

    private void loadPropertyImage(ImageView imageView, Property property) {
        if (property.getImagePaths() == null || property.getImagePaths().isEmpty()) {
            imageView.setImageResource(R.drawable.default_property_image);
            return;
        }

        String imagePath = property.getImagePaths().get(0);
        if (imagePath == null || imagePath.isEmpty()) {
            imageView.setImageResource(R.drawable.default_property_image);
            return;
        }

        // Use Glide to load images - it handles both URIs and file paths
        // Glide's error handler will show the default image if loading fails
        try {
            // Parse as URI if it looks like one, otherwise use as string
            Object imageSource;
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://") || imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                imageSource = Uri.parse(imagePath);
            } else {
                // Try as file path first
                imageSource = imagePath;
            }
            
            Glide.with(context)
                    .load(imageSource)
                    .placeholder(R.drawable.default_property_image)
                    .error(R.drawable.default_property_image)
                    .fallback(R.drawable.default_property_image)
                    .into(imageView);
        } catch (Exception e) {
            // If anything fails, just show default image
            imageView.setImageResource(R.drawable.default_property_image);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvLocation;
        ImageView ivImage;
        MaterialButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txtPropertyTitle);
            tvPrice = itemView.findViewById(R.id.txtPropertyPrice);
            tvLocation = itemView.findViewById(R.id.txtPropertyLocation);
            ivImage = itemView.findViewById(R.id.imgProperty);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

