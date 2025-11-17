package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import models.Property;
import activities.EditPropertyActivity;

import java.util.ArrayList;

public class AdminPropertyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Property> properties;

    public AdminPropertyAdapter(Context context, ArrayList<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return properties.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_property, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvLocation = convertView.findViewById(R.id.tvLocation);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.ivImage = convertView.findViewById(R.id.ivPropertyImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Property property = properties.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvPrice.setText("PKR " + property.getPrice());
        holder.tvLocation.setText(property.getLocation());

        // Load first image
        if (property.getImagePaths() != null && !property.getImagePaths().isEmpty()) {
            holder.ivImage.setImageURI(Uri.parse(property.getImagePaths().split(",")[0]));
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_property_placeholder);
        }

        // Edit button
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPropertyActivity.class);
            intent.putExtra("property_id", property.getId());
            context.startActivity(intent);
        });

        // Delete button callback
        holder.btnDelete.setOnClickListener(v -> {
            if (context instanceof AdminPropertyActionListener) {
                ((AdminPropertyActionListener) context).onDeleteProperty(property.getId());
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle, tvPrice, tvLocation;
        ImageView ivImage;
        Button btnEdit, btnDelete;
    }

    // Interface to handle delete callback in Activity
    public interface AdminPropertyActionListener {
        void onDeleteProperty(int propertyId);
    }
}
