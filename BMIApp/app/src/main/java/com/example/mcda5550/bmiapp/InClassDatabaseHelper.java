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
    public static final String PERSON_TABLE_NAME = "PERSONS"; //name of the table for persons
    public static final String BMI_TABLE_NAME = "BMI_HISTORY"; // name of the table for bmi history

    public InClassDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); // null is for cursor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BMI_TABLE_NAME);


        db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME +
                " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " +
                "PASSWORD TEXT, " +
                "" + "HEALTH_CARD_NUMB TEXT, " + "DATE_OF_BIRTH INTEGER);");

        db.execSQL("CREATE TABLE " + BMI_TABLE_NAME +
                " (" + "personID INTEGER not null, " + "HEIGHT REAL, " + "WEIGHT REAL, "
                + "BMI REAL," + "DATE INTEGER, " +
                "FOREIGN KEY (personID) REFERENCES " + PERSON_TABLE_NAME + "_id);");

        Date today = new Date();

        ContentValues personValues = new ContentValues();
        personValues.put("NAME","Sammy");
        personValues.put("PASSWORD","123456");
        personValues.put("HEALTH_CARD_NUMB", "1234 5678 9101");
        personValues.put("DATE_OF_BIRTH", today.getTime());

        db.insert(PERSON_TABLE_NAME, null, personValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertPerson(String fullName, String password, String healthCardInfo, String dateOfBirth) {

        if (getPersonId(fullName) > 1) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues personValues = new ContentValues();
        personValues.put("NAME", fullName);
        personValues.put("PASSWORD", password);
        personValues.put("HEALTH_CARD_NUMB", healthCardInfo);

        Date convertedDate = convertDate(dateOfBirth);
        personValues.put("DATE_OF_BIRTH", convertedDate.getTime());

        long result = db.insert(PERSON_TABLE_NAME, null, personValues);

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

    public int getPersonId(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select _id from " + PERSON_TABLE_NAME + "where NAME="+fullName+"", null );

        int personId = 0;
        if (cursor.moveToFirst()) {
            personId = cursor.getInt(0);
        }
        cursor.close();
        return personId;
    }

    public ArrayList<Person> getAllUsers() {
        ArrayList<Person> array_list = new ArrayList<Person>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(InClassDatabaseHelper.PERSON_TABLE_NAME,
                new String[] {"_id","NAME","PASSWORD","HEALTH_CARD_NUMB","DATE_OF_BIRTH"},
                null,null,null,null,null);

//        Cursor cursor =  db.rawQuery( "select * from " + BMI_TABLE_NAME + "where personId="+personId+"", null );

        while (cursor.moveToNext()) {
            Person userRecord = new Person();
            userRecord.id = cursor.getInt(0);
            userRecord.name = cursor.getString(1);
            userRecord.password = cursor.getString(2);
            userRecord.healthCardNumber = cursor.getString(3);
            userRecord.dateOfBirth = cursor.getInt(4);
            array_list.add(userRecord);
        }

        cursor.close();

        return array_list;

    }

    public ArrayList<BMIResult> getBMIResultsByPersonId(int personId) {
        ArrayList<BMIResult> array_list = new ArrayList<BMIResult>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(InClassDatabaseHelper.BMI_TABLE_NAME,
                new String[] {"HEIGHT", "WEIGHT", "BMI", "DATE"},
                "personId = ?",new String[] {String.valueOf(personId)},null,null,null);

        while (cursor.moveToNext()) {
            BMIResult bmiRecord = new BMIResult();
            bmiRecord.setHeight(cursor.getDouble(0));
            bmiRecord.setWeight(cursor.getDouble(1));
            bmiRecord.setDate(cursor.getInt(2));
            array_list.add(bmiRecord);
        }

        cursor.close();

        return array_list;
    }

    private Date convertDate(String dateOfBirth) {
        DateFormat formatter ;
        Date date = new Date();
        formatter = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault());
        try {
            date = (Date)formatter.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
