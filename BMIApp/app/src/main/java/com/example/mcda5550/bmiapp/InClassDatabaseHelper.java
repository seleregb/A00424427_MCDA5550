package com.example.mcda5550.bmiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InClassDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="inclass"; // name of the DB
    private static final int DB_VERSION = 1; // version of the DB
    public static final String USERS_TABLE_NAME = "USERS"; //name of the table for persons
    public static final String BMI_TABLE_NAME = "BMI_HISTORY"; // name of the table for bmi history

    public InClassDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); // null is for cursor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BMI_TABLE_NAME);


        db.execSQL("CREATE TABLE " + USERS_TABLE_NAME +
                " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " +
                "PASSWORD TEXT, " +
                "" + "HEALTH_CARD_NUMB TEXT, " + "DATE_OF_BIRTH LONG);");

        db.execSQL("CREATE TABLE " + BMI_TABLE_NAME +
                " (" + "personID INTEGER not null, " + "HEIGHT REAL, " + "WEIGHT REAL, "
                + "BMI REAL," + "DATE LONG, " +
                "FOREIGN KEY (personID) REFERENCES " + USERS_TABLE_NAME + "_id);");

        Date today = new Date();

        ContentValues personValues = new ContentValues();
        personValues.put("NAME","Sammy");
        personValues.put("PASSWORD","123456");
        personValues.put("HEALTH_CARD_NUMB", "1234 5678 9101");
        personValues.put("DATE_OF_BIRTH", today.getTime());

        db.insert(USERS_TABLE_NAME, null, personValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean logInUser(String fullName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor =  db.rawQuery( "select _id from " + USERS_TABLE_NAME + "where NAME="+fullName+"", null );

        Cursor cursor = db.query(InClassDatabaseHelper.USERS_TABLE_NAME,
                null,
                "NAME=? AND PASSWORD=?", new String[]{fullName, password},null,null,null);

        if (cursor.getCount() > 0) {
            Log.d("log On", "logInUser: Successful logon");
            return true;
        }
        cursor.close();
        Log.d("log On", "logInUser: Unsuccessful logon");

        return false;
    }

    public boolean insertUser(String fullName, String password, String healthCardInfo, String dateOfBirth) {

        if (getUserId(fullName) > 1) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues personValues = new ContentValues();
        personValues.put("NAME", fullName);
        personValues.put("PASSWORD", password);
        personValues.put("HEALTH_CARD_NUMB", healthCardInfo);

        Date convertedDate = convertDate(dateOfBirth);
        personValues.put("DATE_OF_BIRTH", convertedDate.getTime());

        long result = db.insert(USERS_TABLE_NAME, null, personValues);

        if ( result != -1) {
            Log.d("MESSAGE", "Inserted new person record Successfully!");
            return true;
        }

        return false;
    }

    public boolean insertBMI(int personId, Double heightValue, Double weightValue, Double bmiValue) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues bmiValues = new ContentValues();
        bmiValues.put("personID", personId);
        bmiValues.put("HEIGHT", heightValue);
        bmiValues.put("WEIGHT", weightValue);
        bmiValues.put("BMI", bmiValue);

        Date today = new Date();
        bmiValues.put("DATE", today.getTime());

        long result = db.insert(BMI_TABLE_NAME, null, bmiValues);

        if ( result != -1) {
            Log.d("MESSAGE", "Inserted new BMI record Successfully!");
            return true;
        }

        return false;
    }

    public int getUserId(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(InClassDatabaseHelper.USERS_TABLE_NAME,
                new String[] {"_id"},
                "NAME=?", new String[]{fullName},null,null,null);

        int personId = 0;
        if (cursor.moveToFirst()) {
            personId = cursor.getInt(0);
        }
        cursor.close();
        return personId;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> array_list = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(InClassDatabaseHelper.USERS_TABLE_NAME,
                new String[] {"_id","NAME","PASSWORD","HEALTH_CARD_NUMB","DATE_OF_BIRTH"},
                null,null,null,null,null);

//        Cursor cursor =  db.rawQuery( "select * from " + BMI_TABLE_NAME + "where personId="+personId+"", null );

        while (cursor.moveToNext()) {
            User userRecord = new User();
            userRecord.id = cursor.getInt(0);
            userRecord.name = cursor.getString(1);
            userRecord.password = cursor.getString(2);
            userRecord.healthCardNumber = cursor.getString(3);
            userRecord.dateOfBirth = cursor.getLong(4);
            array_list.add(userRecord);
        }

        cursor.close();

        return array_list;

    }

    public ArrayList<BMIResult> getBMIResultsByPersonId(int userId) {
        ArrayList<BMIResult> array_list = new ArrayList<BMIResult>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(InClassDatabaseHelper.BMI_TABLE_NAME,
                new String[] {"HEIGHT", "WEIGHT", "BMI", "DATE"},
                "personId = ?",new String[] {String.valueOf(userId)},null,null,null);

        while (cursor.moveToNext()) {
            BMIResult bmiRecord = new BMIResult();
            bmiRecord.setHeight(cursor.getDouble(0));
            bmiRecord.setWeight(cursor.getDouble(1));
            bmiRecord.setBMI(cursor.getDouble(2));
            bmiRecord.setDate(cursor.getLong(3));
            array_list.add(bmiRecord);
        }

        cursor.close();

        return array_list;
    }

    private Date convertDate(String dateOfBirth) {
        DateFormat formatter ;
        Date date = new Date();
        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            date = (Date)formatter.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void updateUser(int currentUser, String fullName, String password, String healthCardNumber, String dateOfBirth) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues personValues = new ContentValues();
        personValues.put("NAME", fullName);
        personValues.put("PASSWORD",password);
        personValues.put("HEALTH_CARD_NUMB", healthCardNumber);

        Date convertedDate = convertDate(dateOfBirth);
        personValues.put("DATE_OF_BIRTH", convertedDate.getTime());


        int count = db.update(InClassDatabaseHelper.USERS_TABLE_NAME, personValues,
                "_id LIKE ?", new String[] {String.valueOf(currentUser)});

        if (count >= 1) {
            Log.d("update", "updateUser: updated user successfully");
        }
    }
}
