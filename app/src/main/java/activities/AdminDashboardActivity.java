package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import adapters.AdminPropertyRecyclerAdapter;
import database.PropertyDao;
import models.Property;

import java.util.ArrayList;

public class AdminDashboardActivity extends BaseActivity implements AdminPropertyRecyclerAdapter.OnPropertyActionListener {

    RecyclerView rvProperties;
    FloatingActionButton fabAddProperty;
    PropertyDao propertyDao;
    ArrayList<Property> propertyList;
    AdminPropertyRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        rvProperties = findViewById(R.id.recyclerProperties);
        fabAddProperty = findViewById(R.id.fabAddProperty);
        propertyDao = new PropertyDao(this);

        rvProperties.setLayoutManager(new LinearLayoutManager(this));

        loadAllProperties();

        fabAddProperty.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPropertyActivity.class));
        });
    }

    private void loadAllProperties() {
        propertyList = (ArrayList<Property>) propertyDao.getAllProperties();
        adapter = new AdminPropertyRecyclerAdapter(this, propertyList, this);
        rvProperties.setAdapter(adapter);
    }

    @Override
    public void onDeleteProperty(int propertyId) {
        showDeleteConfirmationDialog(propertyId);
    }

    private void showDeleteConfirmationDialog(int propertyId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Property")
                .setMessage("Are you sure you want to delete this property?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (propertyDao.deleteProperty(propertyId)) {
                        Toast.makeText(this, "Property deleted successfully", Toast.LENGTH_SHORT).show();
                        loadAllProperties(); // Refresh the list
                    } else {
                        Toast.makeText(this, "Failed to delete property", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllProperties(); // refresh list after adding/editing
    }
}
