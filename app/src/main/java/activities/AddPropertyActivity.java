package activities;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.realestate.R;

import java.util.ArrayList;
import java.util.List;

import database.PropertyDao;
import models.Property;

public class AddPropertyActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etTitle, etDescription, etPrice, etLocation, etPhoneNumber;
    private Spinner spinnerType;
    private Button btnSelectImage, btnAddProperty;
    private TextView tvImageCount;
    private List<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etLocation = findViewById(R.id.etLocation);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spinnerType = findViewById(R.id.spinnerType);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddProperty = findViewById(R.id.btnAddProperty);
        tvImageCount = findViewById(R.id.tvImageCount);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.property_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> openFileChooser());
        btnAddProperty.setOnClickListener(v -> addProperty());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUris.clear();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                imageUris.add(data.getData());
            }
            tvImageCount.setText(imageUris.size() + " images selected");
        }
    }

    private void addProperty() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty() || location.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        List<String> imagePaths = new ArrayList<>();
        for (Uri uri : imageUris) {
            imagePaths.add(uri.toString());
        }

        Property property = new Property();
        property.setAdminId(1); // TODO: Replace with actual admin ID
        property.setTitle(title);
        property.setDescription(description);
        property.setPrice(price);
        property.setAddress(location); // Corrected from setLocation
        property.setType(type);
        property.setPhoneNumber(phoneNumber);
        property.setImagePaths(imagePaths);

        PropertyDao propertyDao = new PropertyDao(this);
        propertyDao.addProperty(property);

        Toast.makeText(this, "Property added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
