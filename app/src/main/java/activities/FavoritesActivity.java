package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;

import java.util.ArrayList;
import java.util.List;

import adapters.PropertyAdapter;
import database.FavoriteDao;
import models.Property;
import session.SessionManager;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavorites;
    private PropertyAdapter adapter;
    private List<Property> favoriteProperties;
    private FavoriteDao favoriteDao;
    private SessionManager sessionManager;
    private TextView tvNoFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerViewFavorites = findViewById(R.id.recycler_view_favorites);
        tvNoFavorites = findViewById(R.id.tv_no_favorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        favoriteDao = new FavoriteDao(this);
        sessionManager = new SessionManager(this);

        loadFavoriteProperties();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadFavoriteProperties() {
        int userId = sessionManager.getUserId();
        favoriteProperties = favoriteDao.getFavoriteProperties(userId);

        if (favoriteProperties.isEmpty()) {
            tvNoFavorites.setVisibility(View.VISIBLE);
            recyclerViewFavorites.setVisibility(View.GONE);
        } else {
            tvNoFavorites.setVisibility(View.GONE);
            recyclerViewFavorites.setVisibility(View.VISIBLE);
            // Assuming the user role is "user" for the favorites screen
            adapter = new PropertyAdapter(this, (ArrayList<Property>) favoriteProperties, 0, userId); // Assuming '0' is a safe placeholder
            recyclerViewFavorites.setAdapter(adapter);
        }
    }
}
