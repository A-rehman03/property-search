package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import database.PropertyDao;
import models.Property;
import adapters.PropertyAdapter;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    ListView lvProperties;
    Button btnAddProperty;
    PropertyDao propertyDao;
    ArrayList<Property> propertyList;

    int adminId = 1; // TODO: Replace with logged-in admin ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        lvProperties = findViewById(R.id.lvProperties);
        btnAddProperty = findViewById(R.id.btnAddProperty);
        propertyDao = new PropertyDao(this);

        loadAdminProperties();

        btnAddProperty.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPropertyActivity.class));
        });

        lvProperties.setOnItemClickListener((parent, view, position, id) -> {
            Property selected = propertyList.get(position);
            Intent i = new Intent(this, EditPropertyActivity.class);
            i.putExtra("property_id", selected.getId());
            startActivity(i);
        });
    }

    private void loadAdminProperties() {
        propertyList = propertyDao.getPropertiesByAdmin(adminId);
        PropertyAdapter adapter = new PropertyAdapter(this, propertyList);
        lvProperties.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdminProperties(); // refresh list after adding/editing
    }
}
