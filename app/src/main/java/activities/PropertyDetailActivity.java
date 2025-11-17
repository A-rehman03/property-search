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

        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvLocation = findViewById(R.id.tvLocation);
        tvType = findViewById(R.id.tvType);
        tvDesc = findViewById(R.id.tvDesc);
        ivImage = findViewById(R.id.ivPropertyImage);
        btnAddFav = findViewById(R.id.btnAddFavorite);

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

        btnAddFav.setOnClickListener(v -> {
            db.addToFavorites(1, propertyId); // TODO: replace hardcoded user ID
            btnAddFav.setText("Added!");
        });
    }
}
