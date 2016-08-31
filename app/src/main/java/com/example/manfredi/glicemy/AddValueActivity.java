package com.example.manfredi.glicemy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.manfredi.glicemy.db.TaskContract;
import com.example.manfredi.glicemy.db.TaskDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddValueActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    private EditText glicemyEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private Spinner spinner;
    private TextView mActionBarTitle;

    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        mActionBarTitle = (TextView)findViewById(R.id.actionBarTitle);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/myfont.ttf");
        mActionBarTitle.setTypeface(tf);

        mHelper = new TaskDbHelper(this);

        cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());

        String currentTime = hourofday + ":" + minute;

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Prima", "Dopo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        glicemyEditText = (EditText) findViewById(R.id.glicemyEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        ImageView addImage = (ImageView) findViewById(R.id.addValueImageView);

        timeEditText.setText(currentTime);
        timeEditText.setTextColor(Color.BLACK);

        dateEditText.setText(currentDate);
        dateEditText.setTextColor(Color.BLACK);

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddValueActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Seleziona Orario");
                mTimePicker.show();

            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddValueActivity.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable glicemyValue = glicemyEditText.getText();
                Editable dateValue = dateEditText.getText();
                Editable timeValue = timeEditText.getText();
                Object spinnerValue = spinner.getSelectedItem();

                if (!String.valueOf(glicemyValue).equals("")) {
                    insertValue(glicemyValue, dateValue, timeValue, spinnerValue);
                    Intent intent = new Intent(AddValueActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    new AlertDialog.Builder(AddValueActivity.this)
                            .setTitle("Manca il valore")
                            .setMessage("completa il campo glicemia")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });



        if ( getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String meal = intent.getExtras().getString("meal");
            String value = intent.getExtras().getString("value");
            String time = intent.getExtras().getString("time");
            String date = intent.getExtras().getString("date");

            glicemyEditText.setText(value);
            dateEditText.setText(date);
            timeEditText.setText(time);
            if (meal.equals("Prima")) {
                spinner.setSelection(0);
            }
            else if (meal.equals("Dopo")) {
                spinner.setSelection(1);
            }
        }
    }

    private void insertValue(Editable value, Editable date, Editable time, Object spinnerValue) {
        String glicemyValue = String.valueOf(value);
        String dateValue = String.valueOf(date);
        String timeValue = String.valueOf(time);
        String spinnerText = spinnerValue.toString();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.GLI_VALUE, glicemyValue);
        values.put(TaskContract.TaskEntry.DATE, dateValue);
        values.put(TaskContract.TaskEntry.TIME, timeValue);
        values.put(TaskContract.TaskEntry.MEAL, spinnerText);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dateEditText.setText(sdf.format(cal.getTime()));
    }
}
