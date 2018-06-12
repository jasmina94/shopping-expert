package com.ftn.mdj.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.AddReminderThread;
import com.ftn.mdj.threads.RemoveReminderThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddReminderActivity extends AppCompatActivity {

    public static AddReminderActivity instance;

    private EditText textTime;
    private EditText textDate;
    private Button btnAdd;
    private Button btnRemove;
    private Toolbar mToolbar;
    private ShoppingListDTO shoppingListDTO;
    private Calendar myCalendar = Calendar.getInstance();
    private boolean hasReminder;
    private String date;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_add_reminder);
        shoppingListDTO = (ShoppingListDTO) getIntent().getSerializableExtra("selectedShoppingList");
        hasReminder = shoppingListDTO.getDate() != null;

        initViews();

    }

    private void initViews() {
        textTime = findViewById(R.id.showTime);
        textDate = findViewById(R.id.showDate);

        btnAdd = findViewById(R.id.add_reminder);

        btnRemove = findViewById(R.id.remove_reminder);

        if(hasReminder) {
            btnAdd.setText("Update");
        }
        btnRemove.setVisibility(View.INVISIBLE);

        if(hasReminder) {
            date = shoppingListDTO.getDate();
            time = shoppingListDTO.getTime();
            textDate.setText(date);
            textTime.setText(time);
            setUpCalendar();
            btnRemove.setVisibility(View.VISIBLE);
        }

        mToolbar = findViewById(R.id.toolbar_trash);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerActions();
    }

    private void setUpCalendar() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            myCalendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
        myCalendar.set(Calendar.MINUTE, minute);
    }

    private void registerActions() {

        btnAdd.setOnClickListener(v -> {
            AddReminderThread addReminderThread = new AddReminderThread(shoppingListDTO.getId(), date, time);
            addReminderThread.start();
            Message msg = Message.obtain();
            addReminderThread.getHandler().sendMessage(msg);
        });

        btnRemove.setOnClickListener(v -> {
            RemoveReminderThread removeReminderThread= new RemoveReminderThread(shoppingListDTO.getId());
            removeReminderThread.start();
            Message msg = Message.obtain();
            removeReminderThread.getHandler().sendMessage(msg);
        });

        TimePickerDialog.OnTimeSetListener time = (timePicker, hour, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hour);
            myCalendar.set(Calendar.MINUTE, minute);

            updateLabelTime();
        };

        textTime.setOnClickListener(v -> new TimePickerDialog(AddReminderActivity.this ,time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show());


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        textDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(AddReminderActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabelTime() {
        time = myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE);
        textTime.setText(time);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date = sdf.format(myCalendar.getTime());
        textDate.setText(date);
    }
}
