package activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.realestate.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import session.SessionManager;
import activities.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {

    MaterialButton btnLogout;
    SwitchMaterial switchDarkMode;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sessionManager = new SessionManager(this);

        btnLogout = findViewById(R.id.btnLogout);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // Initialize switch state
        switchDarkMode.setChecked(sessionManager.isDarkModeEnabled());

        // -------------------------
        // LOGOUT
        // -------------------------
        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();   // clears token / user data
            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        // -------------------------
        // DARK MODE TOGGLE
        // -------------------------
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sessionManager.setDarkMode(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sessionManager.setDarkMode(false);
            }
            recreate(); // apply theme again
        });
    }
}
