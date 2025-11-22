package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;

import activities.AdminDashboardActivity;
import models.Property;

import java.util.ArrayList;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Property> propertyList;
    private String userRole; // "admin" or "user"

    public PropertyAdapter(Context context, ArrayList<Property> propertyList, String userRole) {
        this.context = context;
        this.propertyList = propertyList;
        this.userRole = userRole;
    }

    public PropertyAdapter(AdminDashboardActivity context, ArrayList<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;

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
        holder.tvPrice.setText("PKR " + property.getPrice());
        holder.tvLocation.setText(property.getLocation());

        // Show/hide buttons based on role
        if ("admin".equals(userRole)) {
            holder.adminActions.setVisibility(View.VISIBLE);
            holder.userActions.setVisibility(View.GONE);
        } else {
            holder.adminActions.setVisibility(View.GONE);
            holder.userActions.setVisibility(View.VISIBLE);
        }

        // Button actions
        holder.btnEdit.setOnClickListener(v -> {
            // Implement edit property
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Implement delete property
        });

        holder.btnFavorite.setOnClickListener(v -> {
            // Implement favorite property
        });

        holder.btnContact.setOnClickListener(v -> {
            // Implement contact owner
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPrice, tvLocation;
        Button btnEdit, btnDelete, btnFavorite, btnContact;
        View adminActions, userActions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            adminActions = itemView.findViewById(R.id.adminActions);
            userActions = itemView.findViewById(R.id.userActions);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnContact = itemView.findViewById(R.id.btnContact);
        }
    }
}
