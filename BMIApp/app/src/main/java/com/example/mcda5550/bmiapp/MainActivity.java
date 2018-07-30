package com.example.mcda5550.bmiapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.mcda5550.blankapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private InClassDatabaseHelper dbHelper;
    private static int CURRENT_USER = 0;

    private Boolean isNewUser = false;

    private EditText fullNameField;
    private EditText healthCardField;
    private EditText passwordField;
    private EditText dateOfBirthField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        CURRENT_USER = b.getInt("current_user");

        dbHelper = new InClassDatabaseHelper(this);

        isNewUser = b.getBoolean("new_user");
        Button saveButton = (Button) findViewById(R.id.saveButton);

        if (!isNewUser) {
            retrieveUserDetails(CURRENT_USER);
            saveButton.setText("Update & Continue");
        }

        dateOfBirthField = findViewById(R.id.dobTextField);
        dateOfBirthField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


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


    public void onSaveButtonClick(View view) {

        fullNameField = (EditText) findViewById(R.id.nameTextField);
        String fullName = fullNameField.getText().toString();
        passwordField = (EditText) findViewById(R.id.passTextField);
        String password = passwordField.getText().toString();
        healthCardField = (EditText) findViewById(R.id.healthCardTextField);
        String healthCardNumber = healthCardField.getText().toString();
        dateOfBirthField = (EditText) findViewById(R.id.dobTextField);
        String dateOfBirth = dateOfBirthField.getText().toString();

        validateInput(fullName, password, healthCardNumber, dateOfBirth);

        // If not a new user just update the current users information
        if (!isNewUser) {

            dbHelper.updateUser(CURRENT_USER, fullName, password, healthCardNumber, dateOfBirth);

            Intent intent = new Intent(this, activity_calculate_bmi.class);
            Bundle b = new Bundle();
            b.putInt("current_user", CURRENT_USER);
            intent.putExtras(b);
            startActivity(intent);
        } else {
            dbHelper.insertUser(fullName, password, healthCardNumber, dateOfBirth);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void retrieveUserDetails(int currentUserId){
        //run a query to retrieve existing user details

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(InClassDatabaseHelper.USERS_TABLE_NAME,
                new String[] {"_id","NAME","PASSWORD","HEALTH_CARD_NUMB","DATE_OF_BIRTH"},
                "_id=?",new String[] {String.valueOf(currentUserId)},null,null,null);

        if (cursor.moveToFirst()) {
            CURRENT_USER = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            String healthCardNumber = cursor.getString(3);

            Date date = new Date(cursor.getLong(4));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateOfBirth = dateFormat.format(date);

            fullNameField = findViewById(R.id.nameTextField);
            fullNameField.setText(name);
            passwordField = findViewById(R.id.passTextField);
            passwordField.setText(password);
            healthCardField = findViewById(R.id.healthCardTextField);
            healthCardField.setText(healthCardNumber);
            dateOfBirthField = findViewById(R.id.dobTextField);
            dateOfBirthField.setText(dateOfBirth);
        }

        cursor.close();
        db.close();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        EditText dateOfBirthField = findViewById(R.id.dobTextField);
        dateOfBirthField.setText(currentDateString);
    }

    private void validateInput(String fullName, String dateOfBirth, String healthCardNumber, String password) {

        boolean isInvalid = false;
        View focusView = null;

        if (TextUtils.isEmpty(fullName)) {
            fullNameField.setError(getString(R.string.error_field_required));
            focusView = fullNameField;
            isInvalid = true;
        }

        if (TextUtils.isEmpty(dateOfBirth)) {
            dateOfBirthField.setError(getString(R.string.error_field_required));
            focusView = dateOfBirthField;
            isInvalid = true;
        }

        if (TextUtils.isEmpty(healthCardNumber)) {
            healthCardField.setError(getString(R.string.error_field_required));
            focusView = healthCardField;
            isInvalid = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError(getString(R.string.error_field_required));
            focusView = passwordField;
            isInvalid = true;
        }

        if (isInvalid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }
}
