package com.example.repaso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class Edit extends AppCompatActivity {
    TextView username;
    EditText email;
    EditText password;
    RadioButton radioAdmin;
    RadioButton radioUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();
        username = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        radioAdmin = findViewById(R.id.radioAdmin);
        radioUser = findViewById(R.id.radioUser);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));
        email.setText(intent.getStringExtra("email"));
        password.setText(intent.getStringExtra("password"));
        int role = intent.getIntExtra("role", -1);
        radioUser.setChecked(role == 0);
    }
}