package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import auth.UserAuthService;
import auth.ValidationUtils;
import models.User;

public class SignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etPhone;
    RadioButton rbUser, rbAdmin;
    Button btnSignup;

    UserAuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        rbUser = findViewById(R.id.rbUser);
        rbAdmin = findViewById(R.id.rbAdmin);
        btnSignup = findViewById(R.id.btnSignup);

        authService = new UserAuthService(this);

        btnSignup.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String role = rbAdmin.isChecked() ? "admin" : "user";

        if (!ValidationUtils.isNonEmpty(name) || !ValidationUtils.isNonEmpty(password)
                || !ValidationUtils.isNonEmpty(email)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setRole(role);

        boolean success = authService.registerUser(user);

        if (success) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
