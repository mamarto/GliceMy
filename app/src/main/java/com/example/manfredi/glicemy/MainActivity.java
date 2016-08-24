package com.example.manfredi.glicemy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.manfredi.glicemy.db.TaskContract;
import com.example.manfredi.glicemy.db.TaskDbHelper;
import com.example.manfredi.glicemy.db.model.Property;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView mListView;
    private TaskDbHelper mHelper;
    private ArrayAdapter<Property> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);

        mListView = (ListView)findViewById(R.id.listView);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(MainActivity.this, AddValueActivity.class);
                startActivity(intent);
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {

        ArrayList<Property> itemList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.GLI_VALUE, TaskContract.TaskEntry.DATE, TaskContract.TaskEntry.TIME, TaskContract.TaskEntry.MEAL},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int valueIdx = cursor.getColumnIndex(TaskContract.TaskEntry.GLI_VALUE);
            int dateIdx = cursor.getColumnIndex(TaskContract.TaskEntry.DATE);
            int timeIdx = cursor.getColumnIndex(TaskContract.TaskEntry.TIME);
            int mealIdx = cursor.getColumnIndex(TaskContract.TaskEntry.MEAL);
            itemList.add(new Property(cursor.getString(valueIdx), cursor.getString(dateIdx), cursor.getString(timeIdx), cursor.getString(mealIdx)));
        }

        if (adapter == null) {
            adapter = new ItemArrayAdapter(this, 0, itemList);
            mListView.setAdapter(adapter);
        }
        else {
            adapter.clear();
            adapter.addAll(itemList);
            adapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}
