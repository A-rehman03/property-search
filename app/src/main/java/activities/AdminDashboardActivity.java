package activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import adapters.PropertyAdapter;
import database.PropertyDao;
import models.Property;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    RecyclerView rvProperties;
    FloatingActionButton fabAddProperty;
    PropertyDao propertyDao;
    ArrayList<Property> propertyList;

    int adminId = 1; // TODO: Replace with logged-in admin ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        rvProperties = findViewById(R.id.recyclerProperties);
        fabAddProperty = findViewById(R.id.fabAddProperty);
        propertyDao = new PropertyDao(this);

        rvProperties.setLayoutManager(new LinearLayoutManager(this));

        loadAdminProperties();

        fabAddProperty.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPropertyActivity.class));
        });
    }

    private void loadAdminProperties() {
        propertyList = propertyDao.getPropertiesByAdmin(adminId);
        PropertyAdapter adapter = new PropertyAdapter(this, propertyList);
        rvProperties.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdminProperties(); // refresh list after adding/editing
    }
}
