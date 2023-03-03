package com.example.repaso;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListUsers extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayUsers;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        getSupportActionBar().hide();
        listView = findViewById(R.id.lvUsers);
        btnRegresar = findViewById(R.id.buttonRegresar);
        loadUsers();

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void loadUsers() {
        arrayUsers = retrieveUsers();
        // generar el adaptador que pasará los datos al listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.viewItem, arrayUsers);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> retrieveUsers() {
        ArrayList<String> userData = new ArrayList<>();
        ClsUser sqLite = new ClsUser(getApplicationContext(), "dbUsers", null, 1);
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT username, email, password, role FROM users";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // Generar un string para almacenar la informacion de cada usuario
                String strRole = (cursor.getInt(3) == 0 ) ? "Usuario": "Administrador";
                String linea = cursor.getString(0) + "\n" + cursor.getString(1) + "\n" + strRole;
                // guardarlo en el array
                userData.add(linea);
            } while (cursor.moveToNext());
        }
        database.close();
        return userData;
    }


}