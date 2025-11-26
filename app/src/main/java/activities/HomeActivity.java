package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import adapters.PropertyAdapter;
import auth.UserAuthService;
import database.PropertyDao;
import models.Property;
import session.SessionManager;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private ArrayList<Property> propertyList;
    private PropertyDao propertyDao;
    private UserAuthService authService;
    private String userRole;
    private FloatingActionButton fabAddProperty;
    private SessionManager sessionManager;
    private NavigationView navigationView;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recyclerProperties);
        fabAddProperty = findViewById(R.id.fabAddProperty);

        propertyDao = new PropertyDao(this);
        authService = new UserAuthService(this);
        sessionManager = new SessionManager(this);

        userId = sessionManager.getUserId();

        // Update Nav Header with user email
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserEmail.setText(sessionManager.getEmail());

        Object roleObj = authService.getLoggedInUserRole();
        userRole = (roleObj != null) ? roleObj.toString() : "user";

        setupAdminFeatures();

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already here
                return true;
            } else if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        loadAllProperties();
    }

    private void setupAdminFeatures() {
        Menu navMenu = navigationView.getMenu();
        if ("admin".equals(userRole)) {
            fabAddProperty.setVisibility(View.VISIBLE);
            fabAddProperty.setOnClickListener(v -> startActivity(new Intent(this, AddPropertyActivity.class)));
            navMenu.findItem(R.id.nav_admin_section).setVisible(true);
        } else {
            fabAddProperty.setVisibility(View.GONE);
            navMenu.findItem(R.id.nav_admin_section).setVisible(false);
        }
    }

    private void loadAllProperties() {
        propertyList = (ArrayList<Property>) propertyDao.getAllProperties();
        // Corrected adapter instantiation
        adapter = new PropertyAdapter(this, propertyList, sessionManager.getUserId(), userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Already here
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (id == R.id.nav_logout) {
            authService.logoutUser();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_admin_dashboard) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
        } else if (id == R.id.nav_admin_properties) {
            Intent intent = new Intent(this, MyPropertiesActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Filter Properties"); // Removed title as it's in the layout now

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filter, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etLocation = view.findViewById(R.id.etLocation);
        EditText etMinPrice = view.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = view.findViewById(R.id.etMaxPrice);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);
        View btnApply = view.findViewById(R.id.btnApply);
        View btnReset = view.findViewById(R.id.btnReset);

        String[] propertyTypes = getResources().getStringArray(R.array.property_types);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, propertyTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);

        btnApply.setOnClickListener(v -> {
            String location = etLocation.getText().toString().trim();
            String minPriceStr = etMinPrice.getText().toString().trim();
            String maxPriceStr = etMaxPrice.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            Double minPrice = minPriceStr.isEmpty() ? null : Double.valueOf(minPriceStr);
            Double maxPrice = maxPriceStr.isEmpty() ? null : Double.valueOf(maxPriceStr);

            filterProperties(type, minPrice, maxPrice, location);
            dialog.dismiss();
        });

        btnReset.setOnClickListener(v -> {
            loadAllProperties();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void filterProperties(String type, Double minPrice, Double maxPrice, String location) {
        propertyList = (ArrayList<Property>) propertyDao.filterProperties(type, minPrice, maxPrice, location);
        // Corrected adapter instantiation
        adapter = new PropertyAdapter(this, propertyList, sessionManager.getUserId(), userId);
        recyclerView.setAdapter(adapter);
        if (propertyList.isEmpty()) {
            Toast.makeText(this, "No properties found with the selected filters.", Toast.LENGTH_SHORT).show();
        }
    }
}
