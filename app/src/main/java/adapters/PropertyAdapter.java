
package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import activities.PropertyDetailActivity;
import database.FavoriteDao;
import models.Property;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Property> propertyList;
    private int userId;
    private FavoriteDao favoriteDao;

    public PropertyAdapter(Context context, ArrayList<Property> propertyList, int userId, int id) {
        this.context = context;
        this.propertyList = propertyList;
        this.userId = userId;
        this.favoriteDao = new FavoriteDao(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvPrice.setText("PKR " + String.format("%,.0f", property.getPrice()));
        holder.tvLocation.setText(property.getAddress());

        // Load first image using Glide
        if (property.getImagePaths() != null && !property.getImagePaths().isEmpty()) {
            Glide.with(context)
                    .load(property.getImagePaths().get(0))
                    .placeholder(R.drawable.default_property_image)
                    .error(R.drawable.default_property_image)
                    .into(holder.ivPropertyImage);
        } else {
            holder.ivPropertyImage.setImageResource(R.drawable.default_property_image);
        }

        // Set initial favorite state
        if (favoriteDao.isFavorite(userId, property.getId())) {
            holder.btnFavorite.setIconResource(R.drawable.ic_favorite_filled);
        } else {
            holder.btnFavorite.setIconResource(R.drawable.ic_favorite_realestate);
        }

        // Favorite button action (without toast)
        holder.btnFavorite.setOnClickListener(v -> {
            if (favoriteDao.isFavorite(userId, property.getId())) {
                favoriteDao.removeFavorite(userId, property.getId());
                holder.btnFavorite.setIconResource(R.drawable.ic_favorite_realestate);
            } else {
                favoriteDao.addFavorite(userId, property.getId());
                holder.btnFavorite.setIconResource(R.drawable.ic_favorite_filled);
            }
        });

        // Contact button action
        holder.btnContact.setOnClickListener(v -> {
            String phoneNumber = property.getPhoneNumber();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Contact number not available.", Toast.LENGTH_SHORT).show();
            }
        });

        // Item click to view details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyDetailActivity.class);
            intent.putExtra("property", (Parcelable) property);
            // No need to pass userRole if details screen determines edit/delete visibility
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPrice, tvLocation;
        ImageView ivPropertyImage;
        MaterialButton btnFavorite;
        Button btnContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivPropertyImage = itemView.findViewById(R.id.ivPropertyImage);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnContact = itemView.findViewById(R.id.btnContact);
        }
    }
}
