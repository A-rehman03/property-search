package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import database.DatabaseHelper;
import models.Property;
import adapters.PropertyAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    PropertyAdapter adapter;
    ArrayList<Property> propertyList;
    DatabaseHelper db;

    ImageButton btnFavorites, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.propertyListView);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnSearch = findViewById(R.id.btnSearch);

        db = new DatabaseHelper(this);
        propertyList = db.getAllProperties();

        adapter = new PropertyAdapter(this, propertyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((p, v, pos, id) -> {
            Intent i = new Intent(this, PropertyDetailActivity.class);
            i.putExtra("property_id", propertyList.get(pos).getId());
            startActivity(i);
        });

        btnFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, FavoritesActivity.class))
        );

        btnSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class))
        );
    }
}
