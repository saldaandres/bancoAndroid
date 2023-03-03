package com.example.repaso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // instanciamos la clase  que contiene la clase con el codigo de la base de datos
    ClsUser sqLite = new ClsUser(this, "dbUsers", null, 1);
    EditText userName;
    EditText email;
    EditText password;
    RadioButton roleUser;
    RadioButton roleAdmin;
    Button btnSave, btnSearch, btnDelete, btnEdit, btnListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        userName = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        roleUser = findViewById(R.id.radioUser);
        roleAdmin = findViewById(R.id.radioAdmin);
        btnSave = findViewById(R.id.buttonSave);
        btnSearch = findViewById(R.id.buttonSearch);
        btnEdit = findViewById(R.id.buttonEdit);
        btnDelete = findViewById(R.id.buttonDelete);
        btnListar = findViewById(R.id.buttonListar);
        btnListar.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnListar.setEnabled(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = sqLite.getReadableDatabase();
                String nameOnScreen = userName.getText().toString();
                String passwordOnScreen = password.getText().toString();

                if (nameOnScreen.isEmpty() || passwordOnScreen.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debes ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                String query = "SELECT username, email, password, role FROM users WHERE username = '" + nameOnScreen + "'";
                Cursor cursor = database.rawQuery(query, null);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(MainActivity.this, "No existe este usuario", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passwordOnScreen.equals(cursor.getString(2))) {
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentEdit = new Intent(getApplicationContext(), Edit.class);
                intentEdit.putExtra("username" , nameOnScreen);
                intentEdit.putExtra("email", cursor.getString(1));
                intentEdit.putExtra("password", cursor.getString(2));
                intentEdit.putExtra("role" , cursor.getInt(3));
                startActivity(intentEdit);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int role = roleUser.isChecked() ? 0 : 1;
                guardarUsuario(userName.getText().toString(), email.getText().toString(), password.getText().toString(), role);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringPassword = password.getText().toString();
                String stringName = userName.getText().toString();

                if (stringPassword.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Digita la clave si deseas borrar el usuario", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase database = sqLite.getReadableDatabase();
                String query = "SELECT username, password FROM users WHERE username = '" + stringName + "'";
                Cursor cursor = database.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    borrarUsuario(database, cursor, stringPassword);
                    return;
                }

                Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser(email.getText().toString());
            }
        });

        // evento para listar los usuarios
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roleAdmin.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), ListUsers.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "El usuario no tiene ese privilegio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchUser(String email) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT username, role FROM users WHERE email =  '" + email + "'";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // mostrar los datos del usuario en el formulario
            userName.setText(cursor.getString(0));
            roleAdmin.setChecked(cursor.getInt(1) == 1);
            btnListar.setEnabled(roleAdmin.isChecked());
            roleUser.setChecked(!roleAdmin.isChecked());
            btnEdit.setEnabled(true);
            database.close();
            return;
        }

        Toast.makeText(this, "No hay un usuario con ese correo", Toast.LENGTH_SHORT).show();
        database.close();
    }


    private void guardarUsuario(String userName, String email, String password, int role) {
        // si los campos estan vacios dejo un mensaje y retorno
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = sqLite.getReadableDatabase();
        String sql = "SELECT username FROM users WHERE username = '"+userName+"'";

        Cursor cursorClient = database.rawQuery(sql, null);
        // chequear si el nombre de usuario ya esta tomado y retorno
        if (cursorClient.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "Nombre de usuario no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // copiando los datos a la base de datos
        ContentValues cvClient = new ContentValues();
        cvClient.put("username", userName);
        cvClient.put("email", email);
        cvClient.put("password", password);
        cvClient.put("role", role);
        database.insert("users", null, cvClient);
        database.close();
        Toast.makeText(getApplicationContext(), "Cliente creado con exito", Toast.LENGTH_SHORT).show();
    }

    private boolean borrarUsuario (SQLiteDatabase database, Cursor cursor, String stringPassword) {
        String dbPassword = cursor.getString(1);
        String name = cursor.getString(0);

        if (cursor.getString(1).equals(stringPassword)) {
            String query = "DELETE FROM users WHERE username = '"+ name + "'";
            database.execSQL(query);
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            return true;
        }

        Toast.makeText(this, "Clave incorrecta", Toast.LENGTH_SHORT).show();
        return false;
    }

}