package com.example.repaso;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClsUser extends SQLiteOpenHelper {

    String tblUser = "CREATE TABLE users(username text primary key, email text, password text, role int)";
    // se pueden definir las demas tablas

    public ClsUser(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(tblUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE users");
        sqLiteDatabase.execSQL(tblUser);
    }
}
