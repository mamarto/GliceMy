package com.example.manfredi.glicemy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manfredi.glicemy.db.TaskContract;
import com.example.manfredi.glicemy.db.TaskDbHelper;
import com.example.manfredi.glicemy.db.model.Property;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView mListView;
    private TaskDbHelper mHelper;
    private ArrayAdapter<Property> adapter;
    private ImageView mAddButton;
    private TextView mActionBarTitle;

    private Property obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);


        mHelper = new TaskDbHelper(this);

        mListView = (ListView)findViewById(R.id.listView);
        mAddButton = (ImageView)findViewById(R.id.imageView);
        mActionBarTitle = (TextView)findViewById(R.id.actionBarTitle);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/myfont.ttf");
        mActionBarTitle.setTypeface(tf);

        registerForContextMenu(mListView);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddValueActivity.class);
                startActivity(intent);
            }
        });

        updateUI();
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
        Collections.reverse(itemList);

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

    private void deleteItem(Property item) {
        String itemValue = String.valueOf(item.getValue());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.GLI_VALUE + " = ?",
                new String[]{itemValue});
        db.close();
        updateUI();
    }

    private void modifyItem(Property item) {
        // create a new item with the same values
        Intent intent = new Intent(MainActivity.this, AddValueActivity.class);
        intent.putExtra("value", item.getValue());
        intent.putExtra("date", item.getDate());
        intent.putExtra("time", item.getTime());
        intent.putExtra("meal", item.getMeal());
        startActivity(intent);

        deleteItem(item);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Elimina") {
            deleteItem(obj);
            Toast.makeText(this, "Eliminato", Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Modifica") {
            modifyItem(obj);
        }
        else {
            return false;
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            obj = (Property) lv.getItemAtPosition(acmi.position);

            menu.add(0, v.getId(), 0, "Elimina");
            menu.add(0, v.getId(), 0, "Modifica");
        }
    }
}
