package com.example.mcda5550.blankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class activity_calculate_bmi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmi);
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
        Double calc = (weightAsInt / (heightAsInt * heightAsInt));
        EditText result = (EditText) findViewById(R.id.resultTextField);

        // place in the result text field
        result.setText(String.format(calc.toString(), "0.##"));
    }
}
