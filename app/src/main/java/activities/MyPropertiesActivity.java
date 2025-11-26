package activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.realestate.R;
import java.util.ArrayList;
import adapters.PropertyAdapter;
import database.PropertyDao;
import models.Property;
import session.SessionManager;

public class MyPropertiesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private ArrayList<Property> propertyList;
    private PropertyDao propertyDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerMyProperties);
        propertyDao = new PropertyDao(this);
        sessionManager = new SessionManager(this);

        loadMyProperties();
    }

    private void loadMyProperties() {
        int adminId = sessionManager.getUserId();
        // Assuming the 'userId' you need is the same as the currently logged-in user's ID.
        int userId = sessionManager.getUserId();
        propertyList = (ArrayList<Property>) propertyDao.getPropertiesByAdmin(adminId);
        adapter = new PropertyAdapter(this, propertyList, adminId, userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
