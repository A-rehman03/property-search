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
import activities.PropertyDetailActivity;

import java.util.ArrayList;

public class PropertyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Property> properties;

    public PropertyAdapter(Context context, ArrayList<Property> properties) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvLocation = convertView.findViewById(R.id.tvLocation);
            holder.btnCall = convertView.findViewById(R.id.btnCall);
            holder.ivImage = convertView.findViewById(R.id.ivPropertyImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Property property = properties.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvPrice.setText("PKR " + property.getPrice());
        holder.tvLocation.setText(property.getLocation());

        // Load first image (if any) - use URI
        if (property.getImagePaths() != null && !property.getImagePaths().isEmpty()) {
            holder.ivImage.setImageURI(Uri.parse(property.getImagePaths().split(",")[0]));
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_property_placeholder);
        }

        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + property.getPhoneNumber()));
            context.startActivity(intent);
        });

        // Click on item to view details
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyDetailActivity.class);
            intent.putExtra("property_id", property.getId());
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle, tvPrice, tvLocation;
        Button btnCall;
        ImageView ivImage;
    }
}
