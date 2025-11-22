package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import database.PropertyDao;
import models.Property;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPropertyActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etPrice, etLocation, etType, etPhone;
    Button btnAdd;
    PropertyDao propertyDao;
    int adminId = 1; // TODO: Replace with logged-in admin ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        etTitle = findViewById(R.id.edtTitle);
        etDesc = findViewById(R.id.edtDescription);
        etPrice = findViewById(R.id.edtPrice);
        etLocation = findViewById(R.id.edtLocation);
        etType = findViewById(R.id.spinnerType);
        etPhone = findViewById(R.id.edtPhone);
        btnAdd = findViewById(R.id.btnSaveProperty);


        propertyDao = new PropertyDao(this);

        btnAdd.setOnClickListener(v -> addProperty());
    }

    private void addProperty() {
        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();
        double price = Double.parseDouble(etPrice.getText().toString());
        String location = etLocation.getText().toString();
        String type = etType.getText().toString();
        String phone = etPhone.getText().toString();
        String datePosted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Property p = new Property();
        p.setAdminId(adminId);
        p.setTitle(title);
        p.setDescription(desc);
        p.setPrice(price);
        p.setLocation(location);
        p.setType(type);
        p.setPhoneNumber(phone);
        p.setDatePosted(datePosted);

        long id = propertyDao.addProperty(p);

        if (id > 0) {
            Toast.makeText(this, "Property Added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Adding Property", Toast.LENGTH_SHORT).show();
        }
    }
}
