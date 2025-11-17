package activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;

public class SettingsActivity extends AppCompatActivity {

    Button btnLogout, btnToggleDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnLogout = findViewById(R.id.btnLogout);
        btnToggleDarkMode = findViewById(R.id.btnToggleDarkMode);

        btnLogout.setOnClickListener(v -> {
            // TODO: Clear session & go to login
            finish();
        });

        btnToggleDarkMode.setOnClickListener(v -> {
            // TODO: Toggle dark/light theme
        });
    }
}
