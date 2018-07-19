package com.example.mcda5550.bmiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mcda5550.blankapp.R;

import java.text.DecimalFormat;

public class activity_calculate_bmi extends AppCompatActivity {

    private InClassDatabaseHelper dbHelper;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmi);

        currentUserId = Integer.parseInt(getIntent().getStringExtra("current_user"));

        // instantiate the db helper class
        dbHelper = new InClassDatabaseHelper(this);

    }

    public void onCalculateClick(View view) {
        calculate();
    }

    public void calculate(){
        // gets the height
        EditText height = (EditText) findViewById(R.id.heightTextField);
        String heightValue = height.getText().toString();
        Double heightAsInt = Double.parseDouble(heightValue);

        // gets the weight
        EditText weight = (EditText) findViewById(R.id.weightTextField);
        String weightValue = weight.getText().toString();
        Double weightAsInt = Double.parseDouble(weightValue);

        // calcuate the bmi value
        Double bmiCalculation = (weightAsInt / (heightAsInt * heightAsInt));
        EditText result = (EditText) findViewById(R.id.resultTextField);

        // place in the result text field
        DecimalFormat df = new DecimalFormat("####0.00");
        result.setText(df.format(bmiCalculation));

        // gets the the user name
//        String nameValue = currentUser;

        // save bmi information
        dbHelper.insertBMI(currentUserId, heightAsInt, weightAsInt, bmiCalculation);
    }

    public void onHistoryClick(View view) {
        Intent intent = new Intent(this, activity_bmi_list.class);
        startActivity(intent);
    }

}
