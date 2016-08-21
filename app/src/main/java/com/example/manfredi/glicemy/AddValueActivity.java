package com.example.manfredi.glicemy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.manfredi.glicemy.db.TaskContract;
import com.example.manfredi.glicemy.db.TaskDbHelper;

public class AddValueActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    private EditText glicemyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        mHelper = new TaskDbHelper(this);

        glicemyEditText = (EditText)findViewById(R.id.glicemyEditText);
        Button addButton = (Button)findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertValue(glicemyEditText.getText());
                Intent intent = new Intent(AddValueActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertValue(Editable value) {

        String task = String.valueOf(value);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
