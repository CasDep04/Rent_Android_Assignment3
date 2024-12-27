package com.example.assignment3.Localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment2.Entity.Account;
import com.example.assignment2.Entity.DonationRecords;
import com.example.assignment2.Entity.Donor;
import com.example.assignment2.Entity.DonorLocation;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper  = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertAccount(Account accIn){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ACC_EMAIL, accIn.getEmail());
        contentValue.put(DatabaseHelper.ACC_PASSWORD, accIn.getPassword());
        contentValue.put(DatabaseHelper.ACC_ROLE, accIn.getRole());
        database.insert(DatabaseHelper.TABLE_ACC,null,contentValue);
    }
    public void insertDonor(Donor donorIn){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DONOR_ACC_ID, donorIn.getId());
        contentValue.put(DatabaseHelper.DONOR_NAME, donorIn.getName());
        contentValue.put(DatabaseHelper.DONOR_AGE, donorIn.getAge());
        contentValue.put(DatabaseHelper.DONOR_GENDER, donorIn.getGender());
        contentValue.put(DatabaseHelper.DONOR_PHONE, donorIn.getPhone());
        contentValue.put(DatabaseHelper.DONOR_BLOOD_TYPE, donorIn.getBloodType());
        contentValue.put(DatabaseHelper.DONOR_LAST_DONATION_DATE, donorIn.getLastDonationDate());
        database.insert(DatabaseHelper.TABLE_DONOR,null,contentValue);
    }

    public void insertRecords(DonationRecords recordsIn){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.RECORD_DATE, recordsIn.getDate());
        contentValue.put(DatabaseHelper.RECORD_DONOR_ID, recordsIn.getDonorID());
        contentValue.put(DatabaseHelper.RECORD_AMOUNT, recordsIn.getAmount());
        contentValue.put(DatabaseHelper.RECORD_LOCATION_ID,recordsIn.getLocationID());
        contentValue.put(DatabaseHelper.RECORD_BLOOD_TYPE, recordsIn.getBloodType());
        contentValue.put(DatabaseHelper.RECORD_NOTE, recordsIn.getNote());
        contentValue.put(DatabaseHelper.RECORD_RESULT, recordsIn.getResult());
        database.insert(DatabaseHelper.TABLE_DONATION_RECORDS,null,contentValue);
    }

    public void insertLocation(DonorLocation locationIn){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.LOCATION_NAME, locationIn.getName());
        contentValue.put(DatabaseHelper.LOCATION_ADDRESS, locationIn.getAddress());
        contentValue.put(DatabaseHelper.LOCATION_LATITUDE, locationIn.getLatitude());
        contentValue.put(DatabaseHelper.LOCATION_LONGITUDE, locationIn.getLongtitude());
        contentValue.put(DatabaseHelper.LOCATION_PHONE, locationIn.getPhone());
        contentValue.put(DatabaseHelper.LOCATION_STATUS, locationIn.getStatus());
        database.insert(DatabaseHelper.TABLE_LOCATION,null,contentValue);
    }

    public int updateAccount(Account newAcc){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ACC_EMAIL,newAcc.getEmail());
        contentValue.put(DatabaseHelper.ACC_PASSWORD, newAcc.getPassword());
        contentValue.put(DatabaseHelper.ACC_ROLE, newAcc.getRole());
        return database.update(DatabaseHelper.TABLE_ACC, contentValue, DatabaseHelper.ACC_ID + " =" + newAcc.getId(), null);
    }

    public int updateDonor(Donor newDonor){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DONOR_NAME,newDonor.getName());
        contentValue.put(DatabaseHelper.DONOR_AGE,newDonor.getAge());
        contentValue.put(DatabaseHelper.DONOR_GENDER,newDonor.getGender());
        contentValue.put(DatabaseHelper.DONOR_PHONE,newDonor.getPhone());
        contentValue.put(DatabaseHelper.DONOR_BLOOD_TYPE,newDonor.getBloodType());
        contentValue.put(DatabaseHelper.DONOR_LAST_DONATION_DATE,newDonor.getLastDonationDate());
        contentValue.put(DatabaseHelper.DONOR_ACC_ID,newDonor.getId());

        return database.update(DatabaseHelper.TABLE_DONOR, contentValue, DatabaseHelper.DONOR_ID + " =" + newDonor.getId(), null);
    }

    public int updateRecord(DonationRecords newRecords){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.RECORD_DATE,newRecords.getDate());
        contentValue.put(DatabaseHelper.RECORD_DONOR_ID,newRecords.getDonorID());
        contentValue.put(DatabaseHelper.RECORD_AMOUNT,newRecords.getAmount());
        contentValue.put(DatabaseHelper.RECORD_LOCATION_ID,newRecords.getLocationID());
        contentValue.put(DatabaseHelper.RECORD_BLOOD_TYPE,newRecords.getBloodType());
        contentValue.put(DatabaseHelper.RECORD_NOTE,newRecords.getNote());
        contentValue.put(DatabaseHelper.RECORD_RESULT,newRecords.getResult());

        return database.update(DatabaseHelper.TABLE_DONATION_RECORDS, contentValue, DatabaseHelper.RECORD_ID + " =" + newRecords.getId(), null);
    }

    public int updateLocation(DonorLocation newLocation){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.LOCATION_NAME,newLocation.getName());
        contentValue.put(DatabaseHelper.LOCATION_ADDRESS,newLocation.getAddress());
        contentValue.put(DatabaseHelper.LOCATION_LATITUDE,newLocation.getLatitude());
        contentValue.put(DatabaseHelper.LOCATION_LONGITUDE,newLocation.getLongtitude());
        contentValue.put(DatabaseHelper.LOCATION_PHONE,newLocation.getPhone());
        contentValue.put(DatabaseHelper.LOCATION_STATUS,newLocation.getStatus());
        return database.update(DatabaseHelper.TABLE_LOCATION, contentValue, DatabaseHelper.LOCATION_ID + " =" + newLocation.getId(), null);

    }
    public void deleteLocation(long _id){
        database.delete(DatabaseHelper.TABLE_LOCATION, DatabaseHelper.LOCATION_ID + " =" + _id, null);
    }
    public void deleteRecord(long _id){
        database.delete(DatabaseHelper.TABLE_DONATION_RECORDS, DatabaseHelper.RECORD_ID + " =" + _id, null);
    }
    public void deleteAccount(long _id) {
        database.delete(DatabaseHelper.TABLE_ACC, DatabaseHelper.ACC_ID + " =" + _id, null);
        database.delete(DatabaseHelper.TABLE_DONOR, DatabaseHelper.DONOR_ACC_ID + " =" + _id, null);

        // When deleting or adding rows with AUTOINCREMENT, you can use this to reserve the biggest primary key in the table
        //database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[] {DatabaseHelper.TABLE_NAME});
    }

    public Cursor selectAllAcc() {
        String [] columns = new String[] {
                DatabaseHelper.ACC_ID,
                DatabaseHelper.ACC_EMAIL,
                DatabaseHelper.ACC_PASSWORD,
                DatabaseHelper.ACC_ROLE
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_ACC, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectAllDonor() {
        String [] columns = new String[]{
                DatabaseHelper.DONOR_ID,
                DatabaseHelper.DONOR_NAME,
                DatabaseHelper.DONOR_AGE,
                DatabaseHelper.DONOR_GENDER,
                DatabaseHelper.DONOR_BLOOD_TYPE,
                DatabaseHelper.DONOR_PHONE,
                DatabaseHelper.DONOR_LAST_DONATION_DATE
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_DONOR, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectAllRecords() {
        String [] columns = new String[]{
                DatabaseHelper.RECORD_ID,
                DatabaseHelper.RECORD_DATE,
                DatabaseHelper.RECORD_DONOR_ID,
                DatabaseHelper.RECORD_AMOUNT,
                DatabaseHelper.RECORD_LOCATION_ID,
                DatabaseHelper.RECORD_BLOOD_TYPE,
                DatabaseHelper.RECORD_NOTE,
                DatabaseHelper.RECORD_RESULT,
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_DONATION_RECORDS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor selectRecordsByDonorId(int donorId) {
        String[] columns = new String[]{
                DatabaseHelper.RECORD_ID,
                DatabaseHelper.RECORD_DATE,
                DatabaseHelper.RECORD_DONOR_ID,
                DatabaseHelper.RECORD_AMOUNT,
                DatabaseHelper.RECORD_LOCATION_ID,
                DatabaseHelper.RECORD_BLOOD_TYPE,
                DatabaseHelper.RECORD_NOTE,
                DatabaseHelper.RECORD_RESULT,
        };

        String selection = DatabaseHelper.RECORD_DONOR_ID + " = ?";
        String[] selectionArgs = { String.valueOf(donorId) };

        Cursor cursor = database.query(DatabaseHelper.TABLE_DONATION_RECORDS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor selectAllLocation() {
        String [] columns = new String[]{
                DatabaseHelper.LOCATION_ID,
                DatabaseHelper.LOCATION_NAME,
                DatabaseHelper.LOCATION_ADDRESS,
                DatabaseHelper.LOCATION_PHONE,
                DatabaseHelper.LOCATION_STATUS,
                DatabaseHelper.LOCATION_LATITUDE,
                DatabaseHelper.LOCATION_LONGITUDE
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_LOCATION, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //search
    public Account findAccountByID(int id){
        try (Cursor cursor = selectAllAcc()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ACC_ID));
                    String tempEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACC_EMAIL));
                    String tempPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACC_PASSWORD));
                    String tempRole = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACC_ROLE));
                    if (id == tempID) {
                        return new Account(tempID,tempEmail,tempPassword,tempRole);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Donor findDonorByID(int id){
        try (Cursor cursor = selectAllDonor()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_ID));
                    String tempName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_NAME));
                    int tempAge = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_AGE));
                    String tempGender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_GENDER));
                    String tempPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_PHONE));
                    String tempBloodType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_BLOOD_TYPE));
                    String tempLastDonationDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DONOR_LAST_DONATION_DATE));

                    if (id == tempID) {
                        Account tempAcc = findAccountByID(tempID);
                        return new Donor(tempAcc,tempName,tempAge,tempGender,tempPhone,tempBloodType,tempLastDonationDate);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DonorLocation findLocationByID(int locationID) {
        try (Cursor cursor = selectAllLocation()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_ID));
                    String tempName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_NAME));
                    String tempAddress = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_ADDRESS));
                    String tempPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_PHONE));
                    int tempStatus = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_STATUS));
                    double tempLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_LATITUDE));
                    double tempLongtitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION_LONGITUDE));

                    if (locationID == tempID) {

                        return new DonorLocation(tempID,tempName,tempAddress,tempPhone,tempLongtitude,tempLatitude,tempStatus);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DonationRecords findRecordsByID(int recordID){
        try (Cursor cursor = selectAllRecords()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int tempID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_ID));
                    String tempDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_DATE));
                    int tempDonorID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_DONOR_ID));
                    double tempAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_AMOUNT));
                    String tempBloodType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_BLOOD_TYPE));
                    int tempLocationID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_LOCATION_ID));
                    String tempResult = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_RESULT));
                    String tempNote = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RECORD_NOTE));

                    if (recordID == tempID) {

                        return new DonationRecords(tempID,tempDate,tempDonorID,tempAmount, tempBloodType,tempLocationID,tempResult,tempNote);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
