package com.example.repaso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Edit extends AppCompatActivity {
    TextView username;
    EditText email;
    EditText password;
    RadioButton radioAdmin;
    RadioButton radioUser;
    Button btnActualizar;
    ClsUser sqLite = new ClsUser(this, "dbUsers", null, 1);

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
        btnActualizar = findViewById(R.id.buttonUpdate);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));
        email.setText(intent.getStringExtra("email"));
        password.setText(intent.getStringExtra("password"));
        int role = intent.getIntExtra("role", -1);
        radioAdmin.setChecked(role == 1);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordAsString = password.getText().toString();
                String emailAsString = email.getText().toString();
                int newRole = (radioUser.isChecked()) ? 0 : 1;
                if (emailAsString.isEmpty() || passwordAsString.isEmpty()) {
                    Toast.makeText(Edit.this, "El email y la contraseña no pueden estar vacios", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailAsString.equals(intent.getStringExtra("email"))) {
                    updateUser(emailAsString, passwordAsString, newRole);
                    return;
                }
                SQLiteDatabase database = sqLite.getReadableDatabase();
                String query = "SELECT email from users where email = '" + emailAsString + "'";
                Cursor cursor = database.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    Toast.makeText(Edit.this, "Ese email no está disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateUser(emailAsString, passwordAsString, newRole);
            }
        });

    }

    private void updateUser(String newEmail, String newPassword, int newRole) {
        SQLiteOpenHelper sqLiteOpenHelper = new ClsUser(this, "dbUsers", null, 1);
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();

        // actualizando la base de datos
        ContentValues cv = new ContentValues();
        cv.put("email", newEmail);
        cv.put("password", newPassword);
        cv.put("role", newRole);
        String where = "username = ?";
        String[] whereArgs = {getIntent().getStringExtra("username")};
        database.update("users", cv, where, whereArgs);
        database.close();
        Toast.makeText(this, "Cliente actualizado con exito", Toast.LENGTH_SHORT).show();
        finish();
    }
}