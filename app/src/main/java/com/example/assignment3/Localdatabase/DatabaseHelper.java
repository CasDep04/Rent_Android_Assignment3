package com.example.assignment3.Localdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "EZRENT.DB";
    private static final int DB_VERSION = 1;

    //ACOUNT TABLE
    public static final String TABLE_ACC = "current_account";
    public static final String ACC_ID = "_id";
    public static final String ACC_EMAIL = "email";
    public static final String ACC_PASSWORD = "password";
    public static final String ACC_ROLE = "role";

    private static final String CREATE_TABLE_ACCOUNT =
            "CREATE TABLE " + TABLE_ACC + " (" +
                    ACC_ID + " TEXT NOT NULL, " +
                    ACC_EMAIL + " TEXT NOT NULL, " +
                    ACC_PASSWORD + " TEXT NOT NULL, " +
                    ACC_ROLE + " TEXT NOT NULL" +
                    ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACC);

        onCreate(db);
    }
}

