package com.example.manfredi.glicemy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        mHelper = new TaskDbHelper(this);

        Calendar cal = Calendar.getInstance();

        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());

        String currentTime = hourofday + ":" + minute;

        spinner = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Prima", "Dopo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);


        glicemyEditText = (EditText)findViewById(R.id.glicemyEditText);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        timeEditText = (EditText)findViewById(R.id.timeEditText);
        Button addButton = (Button)findViewById(R.id.addButton);

        timeEditText.setText(currentTime);
        dateEditText.setText(currentDate);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertValue(glicemyEditText.getText(), dateEditText.getText(), timeEditText.getText(), spinner.getSelectedItem());
                Intent intent = new Intent(AddValueActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
}
