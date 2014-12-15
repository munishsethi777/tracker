package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import in.satyainfopages.geotrack.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class Config {

    public static final String TABLE_NAME = "appconfig";

    private static final String COLUMN_CONFIG_KEY = "cfgkey";
    private static final String COLUMN_CONFIG_VAL = "cfgval";

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + "(" + COLUMN_CONFIG_KEY + " TEXT , "
            + COLUMN_CONFIG_VAL + " TEXT);";

    public static final String[] CONFIG_COLUMNS = {COLUMN_CONFIG_KEY,
            COLUMN_CONFIG_VAL};

    public static String getConfigVal(Context context, String key) {
        MySQLiteHelper mydb = new MySQLiteHelper(context);

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mydb.getReadableDatabase();
            cursor = db.query(Config.TABLE_NAME, CONFIG_COLUMNS, Config.COLUMN_CONFIG_KEY + " = ?",
                    new String[]{String.valueOf(key)}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String value = cursor.getString(1);
                    return value;
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    public static void SaveConfig(Context context, String key, String Val, boolean isUpdate) {
        boolean isExist = false;
        MySQLiteHelper mydb = new MySQLiteHelper(context);

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mydb.getWritableDatabase();
            if (isUpdate) {
                String exist = getConfigVal(context, key);

                if (exist != null) {
                    isExist = true;

                    ContentValues values = new ContentValues();
                    values.put(COLUMN_CONFIG_VAL, Val);
                    db.update(Config.TABLE_NAME, values,
                            COLUMN_CONFIG_KEY + " = ?", new String[]{key});


                }
            }
            if (!isExist) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_CONFIG_KEY, key);
                values.put(COLUMN_CONFIG_VAL, Val);
                db.insert(Config.TABLE_NAME, null, values);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

}
