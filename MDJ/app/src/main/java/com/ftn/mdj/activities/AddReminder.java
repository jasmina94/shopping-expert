package com.ftn.mdj.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ftn.mdj.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddReminder extends AppCompatActivity {

    public static AddReminder instance;

    private EditText textTime;
    private EditText textDate;
    private Button btnAdd;
    private Button btnRemove;
    private Toolbar mToolbar;
    Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_add_reminder);

        //poziv getremnider

        initViews();

    }

    private void initViews() {
        textTime = findViewById(R.id.showTime);
        textDate = findViewById(R.id.showDate);

        btnAdd = findViewById(R.id.add_reminder);
        btnRemove = findViewById(R.id.remove_reminder);

        mToolbar = findViewById(R.id.toolbar_trash);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerActions();
    }

    private void registerActions() {

        TimePickerDialog.OnTimeSetListener time = (timePicker, hour, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hour);
            myCalendar.set(Calendar.MINUTE, minute);

            updateLabelTime();
        };

        textTime.setOnClickListener(v -> {
            new TimePickerDialog(AddReminder.this ,time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        });


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        textDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(AddReminder.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabelTime() {
        textTime.setText(myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE));
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textDate.setText(sdf.format(myCalendar.getTime()));
    }
}
