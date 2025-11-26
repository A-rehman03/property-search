package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.example.realestate.R;

import java.util.ArrayList;
import java.util.List;

import adapters.ImageAdapter;
import database.PropertyDao;
import models.Property;
import session.SessionManager;

public class PropertyDetailsActivity extends BaseActivity {

    private Property property;
    private PropertyDao propertyDao;
    private SessionManager sessionManager;

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

        ViewPager2 viewPager = findViewById(R.id.imageViewPager);
        TextView propertyPrice = findViewById(R.id.property_price);
        TextView propertyDescription = findViewById(R.id.property_description);
        TextView propertyLocation = findViewById(R.id.property_location);
        TextView propertyType = findViewById(R.id.property_type);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            property = intent.getParcelableExtra("property");
        }

        if (property != null) {
            collapsingToolbar.setTitle(property.getTitle());
            propertyPrice.setText("PKR " + String.format("%,.0f", property.getPrice()));
            propertyLocation.setText(property.getAddress());
            propertyType.setText(property.getType());
            propertyDescription.setText(property.getDescription());

            List<String> imageUrls = property.getImagePaths() != null ? property.getImagePaths() : new ArrayList<>();
            ImageAdapter adapter = new ImageAdapter(this, imageUrls, sessionManager.getUserRole());
            viewPager.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Property details not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.property_details_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String userRole = sessionManager.getUserRole();
        boolean isAdmin = "admin".equalsIgnoreCase(userRole);

        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);

        if (editItem != null) {
            editItem.setVisible(isAdmin);
        }
        if (deleteItem != null) {
            deleteItem.setVisible(isAdmin);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_edit) {
            Intent editIntent = new Intent(this, EditPropertyActivity.class);
            editIntent.putExtra("property_id", property.getId());
            startActivity(editIntent);
            return true;
        } else if (itemId == R.id.action_delete) {
            if (propertyDao.deleteProperty(property.getId())) {
                Toast.makeText(this, "Property deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete property", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
