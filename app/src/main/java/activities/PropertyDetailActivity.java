package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import database.DatabaseHelper;
import models.Property;

public class PropertyDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvPrice, tvLocation, tvType, tvDesc;
    ImageView ivImage;
    Button btnAddFav;
    DatabaseHelper db;
    int propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        ivImage = findViewById(R.id.imgProperty);
        tvTitle = findViewById(R.id.txtTitle);
        tvPrice = findViewById(R.id.txtPrice);
        tvLocation = findViewById(R.id.txtLocation);
        tvType = findViewById(R.id.txtType);
        tvDesc = findViewById(R.id.txtDescription);
        // btnAddFav = findViewById(R.id.btnAddFavorite);
        db = new DatabaseHelper(this);

        propertyId = getIntent().getIntExtra("property_id", -1);
        Property p = db.getPropertyById(propertyId);

        if (p != null) {
            tvTitle.setText(p.getTitle());
            tvPrice.setText("PKR " + p.getPrice());
            tvLocation.setText(p.getLocation());
            tvType.setText(p.getType());
            tvDesc.setText(p.getDescription());
        }

//        btnAddFav.setOnClickListener(v -> {
//            db.addToFavorites(1, propertyId); // TODO: replace hardcoded user ID
//            btnAddFav.setText("Added!");
//        });
    }
}
