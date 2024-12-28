package com.example.assignment3.Localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void updateUser(String userIn) {
        // Delete the entire table
        database.delete(DatabaseHelper.TABLE_USER, null, null);
        
        // Insert the new user string
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.USER_EMAIL, userIn);
        database.insert(DatabaseHelper.TABLE_USER, null, contentValue);
    }

    public void deleteUser() {
        database.delete(DatabaseHelper.TABLE_USER, null, null);
    }

    public Cursor selectAllUsers() {
        String[] columns = new String[]{
                DatabaseHelper.USER_EMAIL
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_USER, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
