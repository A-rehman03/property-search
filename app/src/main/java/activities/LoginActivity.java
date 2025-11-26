
package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.R; // Adjust if your package name is different
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import database.DatabaseHelper;
import models.User;

public class LoginActivity extends BaseActivity {

    TextInputEditText etEmail, etPassword;
    MaterialButton btnLogin;
    TextView txtSignupLink;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // --- initialize views ---
        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignupLink = findViewById(R.id.txtSignupLink);

        db = new DatabaseHelper(this);

        // --- login button ---
        btnLogin.setOnClickListener(v -> handleLogin());

        // --- signup link ---
        txtSignupLink.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.loginUser(email, pass);
        if (user != null) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

            Intent intent;
            if ("admin".equals(user.getRole())) {
                intent = new Intent(this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
            intent.putExtra("USER_ROLE", user.getRole());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
        }
    }
}
