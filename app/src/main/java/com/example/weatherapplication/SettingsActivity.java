package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    EditText etNewpass;
    Button btnSave, btnLogout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etNewpass = findViewById(R.id.etNewpass);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();


        btnSave.setOnClickListener(v -> changePassword());
        btnLogout.setOnClickListener(v -> logoutUser());

        findViewById(R.id.btnBack).setOnClickListener(v ->
                startActivity(new Intent(SettingsActivity.this, WeatherActivity.class)));
    }

    private void changePassword() {
            String newPss = etNewpass.getText().toString().trim();

            if(newPss.isEmpty()) {
                Toast.makeText(this, "Please Enter the password and continue", Toast.LENGTH_SHORT);
                return;
            }

        FirebaseUser user = auth.getCurrentUser();

                if(user != null) {
                    user.updatePassword(newPss).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(this, "Password Updated successfully!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "Failed to Update password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
    }



    private void saveUsername() {
        String name = etNewpass.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference("Users")
                .child(auth.getCurrentUser().getUid())
                .child("username").setValue(name);
        Toast.makeText(this, "Username updated", Toast.LENGTH_SHORT).show();

        finish(); // Closes current activity after saving or logging out
    }

    private void logoutUser() {
        auth.signOut();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        finish();
    }

}
