package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import database.PropertyDao;
import models.Property;

public class EditPropertyActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etPrice, etLocation, etType, etPhone;
    Button btnUpdate, btnDelete;
    PropertyDao propertyDao;
    int propertyId;
    Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        etLocation = findViewById(R.id.etLocation);
        etType = findViewById(R.id.etType);
        etPhone = findViewById(R.id.etPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        propertyDao = new PropertyDao(this);
        propertyId = getIntent().getIntExtra("property_id", -1);
        property = propertyDao.getPropertyById(propertyId);

        if (property != null) {
            etTitle.setText(property.getTitle());
            etDesc.setText(property.getDescription());
            etPrice.setText(String.valueOf(property.getPrice()));
            etLocation.setText(property.getLocation());
            etType.setText(property.getType());
            etPhone.setText(property.getPhoneNumber());
        }

        btnUpdate.setOnClickListener(v -> updateProperty());
        btnDelete.setOnClickListener(v -> deleteProperty());
    }

    private void updateProperty() {
        property.setTitle(etTitle.getText().toString());
        property.setDescription(etDesc.getText().toString());
        property.setPrice(Double.parseDouble(etPrice.getText().toString()));
        property.setLocation(etLocation.getText().toString());
        property.setType(etType.getText().toString());
        property.setPhoneNumber(etPhone.getText().toString());

        boolean success = propertyDao.updateProperty(property);
        if (success) {
            Toast.makeText(this, "Property Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProperty() {
        boolean success = propertyDao.deleteProperty(propertyId);
        if (success) {
            Toast.makeText(this, "Property Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
