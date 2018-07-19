package com.example.mcda5550.bmiapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.mcda5550.blankapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private InClassDatabaseHelper dbHelper;
    private static int CURRENT_USER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dbHelper = new InClassDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //run a query
        Cursor cursor = db.query(InClassDatabaseHelper.PERSON_TABLE_NAME,
                new String[] {"_id","NAME","PASSWORD","HEALTH_CARD_NUMB","DATE_OF_BIRTH"},
                null,null,null,null,null);

        if (cursor.moveToFirst()) {
            CURRENT_USER = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            String healthCardNumber = cursor.getString(3);

            Date date = new Date(cursor.getInt(4));
            DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault());
            String dateOfBirth = dateFormat.format(date);

            EditText firstNameField = findViewById(R.id.nameTextField);
            firstNameField.setText(name);
            EditText passwordField = findViewById(R.id.passTextField);
            passwordField.setText(password);
            EditText healthCardField = findViewById(R.id.healthCardTextField);
            healthCardField.setText(healthCardNumber);
            EditText dateOfBirthField = findViewById(R.id.dobTextField);
            dateOfBirthField.setText(dateOfBirth);
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoginButtonClick(View view) {

        Intent intent = new Intent(this, activity_calculate_bmi.class);
        Log.d("ID OF USER", String.valueOf(CURRENT_USER));
        intent.putExtra("current_user", String.valueOf(CURRENT_USER));
        startActivity(intent);
    }

    public void onAddNewButtonClick(View view) {
        EditText firstNameField = findViewById(R.id.nameTextField);
        String fullName = firstNameField.toString();
        EditText passwordField = findViewById(R.id.passTextField);
        String password = passwordField.toString();
        EditText healthCardField = findViewById(R.id.healthCardTextField);
        String healthCardNumber = healthCardField.toString();
        EditText dateOfBirthField = findViewById(R.id.dobTextField);
        String dateOfBirth = dateOfBirthField.toString();

        dbHelper.insertPerson(fullName, password, healthCardNumber, dateOfBirth);

        Intent intent = new Intent(this, activity_calculate_bmi.class);
        startActivity(intent);
    }
}
