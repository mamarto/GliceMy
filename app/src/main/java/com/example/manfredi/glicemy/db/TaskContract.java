package com.example.manfredi.glicemy.db;

import android.provider.BaseColumns;

/**
 * Created by Manfredi on 18/08/16.
 */
public class TaskContract {
    public static final String DB_NAME = "com.schicchitano.glicemy.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "glicemyItems";

        public static final String GLI_VALUE = "title";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String MEAL = "meal";
    }
}
