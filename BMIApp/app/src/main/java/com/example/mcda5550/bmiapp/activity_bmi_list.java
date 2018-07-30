package com.example.mcda5550.bmiapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mcda5550.blankapp.R;

import java.util.ArrayList;


public class activity_bmi_list extends AppCompatActivity {

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmilist);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            currentUserId = b.getInt("current_user");
        }

        // instantiate the db helper class
        InClassDatabaseHelper dbHelper = new InClassDatabaseHelper(this);

        ArrayList<BMIResult> results = dbHelper.getBMIResultsByPersonId(currentUserId);

        if (results.size() != 0) {
            ListView bmiResultsList = (ListView)findViewById(R.id.bmi_history_list);

            BMIListAdapter listAdapter = new BMIListAdapter(this, 0, results);

            bmiResultsList.setAdapter(listAdapter);
        }
        else {
            Toast.makeText(this, "You have not saved any recent BMI results", Toast.LENGTH_SHORT).show();
        }

    }

    public void onListItemClick(ListView listView, View itemView, int position, long id) {
//        Log.d("Clicked", "onListItemClick: " + results[position].toString());
//        System.out.println("Clicked on " + results[position].toString());
    }




}
