package com.example.worktimecalculator;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.worktimecalculator.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    public static ReentrantLock lock = new ReentrantLock();

    final String WORKHOURS = "08:15";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        EditText starttime = findViewById(R.id.starting_time);
        EditText lunchtime = findViewById(R.id.lunch_time);
        EditText endtime = findViewById(R.id.end_time);
        EditText overtime = findViewById(R.id.overtime);
        TimePickerDialog timePickerDialog;

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTimeIntoField(view, starttime, lunchtime, endtime, overtime, "starttime");
            }
        });
        starttime.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    updateFields(starttime, lunchtime, endtime, overtime, false);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lunchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTimeIntoField(view, starttime, lunchtime, endtime, overtime, "lunchtime");
            }
        });
        lunchtime.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    updateFields(starttime, lunchtime, endtime, overtime, false);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTimeIntoField(view, starttime, lunchtime, endtime, overtime, "endtime");
            }
        });
        endtime.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    updateFields(starttime, lunchtime, endtime, overtime, true);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTimeIntoField(view, starttime, lunchtime, endtime, overtime, "overtime");
            }
        });
        overtime.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    updateFields(starttime, lunchtime, endtime, overtime, false);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void inputTimeIntoField(View view, EditText starttime, EditText lunchtime, EditText endtime, EditText overtime, String inputField) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                switch (inputField){
                    case "starttime":
                        starttime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        break;

                    case "lunchtime":
                        lunchtime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        break;

                    case "endtime":
                        endtime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        break;

                    case "overtime":
                        overtime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        break;
                }
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }

    private void updateFields(EditText starttime, EditText lunchtime, EditText endtime, EditText overtime, boolean calcOver) throws ParseException {
        if ((starttime.getText().toString().equals("00:01"))
                && !(endtime.getText().toString().equals("00:01"))
                && !(overtime.getText().toString().equals("00:01"))){

            calculateStartTime(starttime, lunchtime, endtime, overtime);
        } else if (!(starttime.getText().toString().equals("00:01"))
                && (endtime.getText().toString().equals("00:01"))
                && !(overtime.getText().toString().equals("00:01"))){

            calculateEndTime(starttime, lunchtime, endtime, overtime);
        } else if (!(starttime.getText().toString().equals("00:01"))
                && !(endtime.getText().toString().equals("00:01"))
                && (overtime.getText().toString().equals("00:01"))){

            calculateOverTime(starttime, lunchtime, endtime, overtime);
        } else if (!(starttime.getText().toString().equals("00:01"))
                && !(endtime.getText().toString().equals("00:01"))
                && !(overtime.getText().toString().equals("00:01"))){
            if (calcOver) {
                calculateOverTime(starttime, lunchtime, endtime, overtime);
            }
            if (!calcOver){
                calculateEndTime(starttime, lunchtime, endtime, overtime);
            }
        }
    }

    String convertSecondsToHMmSs(long ms) {
        long seconds = ms / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d",h,m);
    }
    private void calculateStartTime(EditText starttime, EditText lunchtime, EditText endtime, EditText overtime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(String.valueOf(starttime.getText()));
        Date lunch = format.parse(String.valueOf(lunchtime.getText()));
        Date end = format.parse(String.valueOf(endtime.getText()));
        Date over = format.parse(String.valueOf(overtime.getText()));
        Date workhours = format.parse(WORKHOURS);
        Date midnight = format.parse("00:00");

        long ot = over.getTime() - midnight.getTime();
        long lt = lunch.getTime() - midnight.getTime();
        long wt = workhours.getTime() - midnight.getTime();
        long worktime = wt + ot + lt;
        long difference = end.getTime() - worktime;

        starttime.setText(convertSecondsToHMmSs(difference));
    }

    private void calculateEndTime(EditText starttime, EditText lunchtime, EditText endtime, EditText overtime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(String.valueOf(starttime.getText()));
        Date lunch = format.parse(String.valueOf(lunchtime.getText()));
        Date end = format.parse(String.valueOf(endtime.getText()));
        Date over = format.parse(String.valueOf(overtime.getText()));
        Date workhours = format.parse(WORKHOURS);
        Date midnight = format.parse("00:00");


        long ot = over.getTime() - midnight.getTime();
        long lt = lunch.getTime() - midnight.getTime();
        long wt = workhours.getTime() - midnight.getTime();
        long worktime = wt + ot + lt;
        long difference = start.getTime() + worktime;

        endtime.setText(convertSecondsToHMmSs(difference));
    }

    private void calculateOverTime(EditText starttime, EditText lunchtime, EditText endtime, EditText overtime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(String.valueOf(starttime.getText()));
        Date lunch = format.parse(String.valueOf(lunchtime.getText()));
        Date end = format.parse(String.valueOf(endtime.getText()));
        Date over = format.parse(String.valueOf(overtime.getText()));
        Date workhours = format.parse(WORKHOURS);
        Date midnight = format.parse("00:00");

        long difference = end.getTime() - start.getTime() - (lunch.getTime()- midnight.getTime());

        //overtime.setText(convertSecondsToHMmSs(difference));
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
}