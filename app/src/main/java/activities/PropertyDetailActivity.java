package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.realestate.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import adapters.ImageAdapter;
import database.PropertyDao;
import models.Property;
import session.SessionManager;

public class PropertyDetailActivity extends BaseActivity {

    private Property property;
    private PropertyDao propertyDao;
    private SessionManager sessionManager;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        propertyDao = new PropertyDao(this);
        sessionManager = new SessionManager(this);
        property = getIntent().getParcelableExtra("property");

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ViewPager2 viewPager = findViewById(R.id.imageViewPager);
        TextView propertyPrice = findViewById(R.id.property_price);
        TextView propertyLocation = findViewById(R.id.property_location);
        TextView propertyType = findViewById(R.id.property_type);
        TextView propertyDescription = findViewById(R.id.property_description);

        if (property != null) {
            collapsingToolbar.setTitle(" "); // Hide default title
            
            TextView propertyTitle = findViewById(R.id.property_title);
            TextView propertyArea = findViewById(R.id.property_area);
            TextView tvBedrooms = findViewById(R.id.tvBedrooms);
            TextView tvBathrooms = findViewById(R.id.tvBathrooms);

            propertyTitle.setText(property.getTitle());
            propertyPrice.setText("PKR " + String.format("%,.0f", property.getPrice()));
            propertyLocation.setText(property.getAddress());
            propertyType.setText(property.getType());
            propertyDescription.setText(property.getDescription());
            
            // Set dummy data for now as these fields might not exist in Property model yet
            propertyArea.setText("2,500 sqft");
            tvBedrooms.setText("4 bedrooms");
            tvBathrooms.setText("3 bathrooms");

            String userRole = sessionManager.getUserRole();
            ImageAdapter imageAdapter = new ImageAdapter(this, property.getImagePaths() != null ? property.getImagePaths() : new ArrayList<>(), userRole);
            viewPager.setAdapter(imageAdapter);
        } else {
            Toast.makeText(this, "Property details not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.property_details_menu, menu);
        this.optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the user role from the session.
        String userRole = sessionManager.getUserRole();
        // Check if the user's role is "admin", ignoring case.
        boolean isAdmin = "admin".equalsIgnoreCase(userRole);

        // Set the visibility of "Edit" and "Delete" buttons based on whether the user is an admin.
        menu.findItem(R.id.action_edit).setVisible(isAdmin);
        menu.findItem(R.id.action_delete).setVisible(isAdmin);
        
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, EditPropertyActivity.class);
            intent.putExtra("property_id", property.getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Property")
                .setMessage("Are you sure you want to delete this property?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (propertyDao.deleteProperty(property.getId())) {
                        Toast.makeText(this, "Property deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the previous screen
                    } else {
                        Toast.makeText(this, "Failed to delete property", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
