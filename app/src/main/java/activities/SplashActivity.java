package activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import auth.UserAuthService;

public class SplashActivity extends AppCompatActivity {

    UserAuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = new UserAuthService(this);

        if (authService.isLoggedIn()) {
            if (authService.getLoggedInUserRole().equals("admin")) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}
