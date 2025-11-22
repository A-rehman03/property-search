package activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import adapters.PropertyAdapter;
import auth.UserAuthService;
import database.DatabaseHelper;
import models.Property;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PropertyAdapter adapter;
    ArrayList<Property> propertyList;
    DatabaseHelper db;
    UserAuthService authService;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recyclerView = findViewById(R.id.recyclerProperties);

        db = new DatabaseHelper(this);
        authService = new UserAuthService(this);

        // Get logged-in user's role
        Object roleObj = authService.getLoggedInUserRole();
        if (roleObj != null) {
            userRole = roleObj.toString();
        } else {
            userRole = "user"; // default fallback
        }

        propertyList = db.getAllProperties();

        adapter = new PropertyAdapter(this, propertyList, userRole);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Toast.makeText(this, "Logged in as: " + userRole, Toast.LENGTH_SHORT).show();
    }
}
