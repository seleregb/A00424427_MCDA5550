package com.example.mcda5550.bmiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mcda5550.blankapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BMIListAdapter extends ArrayAdapter<BMIResult> {

    public BMIListAdapter(@NonNull Context context, int resource, @NonNull List<BMIResult> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("position is  " +position);
        BMIResult result = getItem(position);
        if(convertView == null)  {convertView = LayoutInflater.from(getContext()).inflate(R.layout.bmi_history_row_layout, parent, false);}

        TextView dateRecord = (TextView) convertView.findViewById(R.id.bmi_history_date);
        TextView weightRecord = (TextView) convertView.findViewById(R.id.bmi_history_weight);
        TextView bmiRecord = (TextView) convertView.findViewById(R.id.bmi_history_bmi);

        Date date = new Date(result.getDate());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        dateRecord.setText("Date: " +  dateFormat.format(date));
        weightRecord.setText("Weight: " + String.valueOf(result.getWeight()));

        DecimalFormat df = new DecimalFormat("####0.00");
        String formattedResult = df.format(result.getBmi());
        bmiRecord.setText("BMI: " + formattedResult);

        return convertView;
    }
}
