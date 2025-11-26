package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.realestate.R;

import java.util.List;

import session.SessionManager;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private SessionManager sessionManager;

    public ImageAdapter(Context context, List<String> imageUrls, String userRole) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.sessionManager = new SessionManager(context); // Initialize SessionManager
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_property_image)
                //.error(R.drawable.ic_image_not_found) // Show error placeholder
                .into(holder.imageView);

        // Check user role and set delete button visibility
        String userRole = sessionManager.getUserRole();
        if ("admin".equalsIgnoreCase(userRole)) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                // Handle delete action here
                Toast.makeText(context, "Delete image at position: " + position, Toast.LENGTH_SHORT).show();
                // You would typically call a method to delete the image from the database and storage here
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView deleteButton; // Add reference to the delete button

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.delete_image_button); // Find delete button
        }
    }
}
