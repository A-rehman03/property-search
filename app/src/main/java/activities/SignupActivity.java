package activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import com.example.realestate.R;

import auth.UserAuthService;
import auth.ValidationUtils;

public class SignupActivity extends BaseActivity {

    EditText etUsername, etEmail, etPassword, etPhone;
    RadioGroup roleGroup;
    Button btnSignup;

    UserAuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SIGNUP_DEBUG", "ContentView set: " + R.layout.activity_signup);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authService = new UserAuthService(this);

        etUsername = findViewById(R.id.edtUsername);
        etEmail    = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        etPhone    = findViewById(R.id.edtPhone);
        roleGroup  = findViewById(R.id.roleGroup);
        btnSignup  = findViewById(R.id.btnSignup);
        Log.d("SIGNUP_DEBUG", "etUsername=" + etUsername + ", etEmail=" + etEmail
                + ", etPassword=" + etPassword + ", etPhone=" + etPhone);


        btnSignup.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        Log.d("SIGNUP_DEBUG", "References - etUsername=" + etUsername + ", etEmail=" + etEmail
                + ", etPassword=" + etPassword + ", etPhone=" + etPhone);

        String username = etUsername != null && etUsername.getText() != null
                ? etUsername.getText().toString().trim() : null;
        String email = etEmail != null && etEmail.getText() != null
                ? etEmail.getText().toString().trim() : null;
        String password = etPassword != null && etPassword.getText() != null
                ? etPassword.getText().toString().trim() : null;
        String phone = etPhone != null && etPhone.getText() != null
                ? etPhone.getText().toString().trim() : null;

        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        String role = null;
        if (selectedRoleId == R.id.radioAdmin) {
            role = "admin";
        } else if (selectedRoleId == R.id.radioUser) {
            role = "user";
        }

        Log.d("SIGNUP_DEBUG", "User input - username: " + username + ", email: " + email
                + ", password: " + password + ", phone: " + phone + ", role: " + role);

        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                phone == null || phone.isEmpty() ||
                role == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = authService.register(username, email, password, phone, role);

        if (success) {
            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
        }
    }

}
