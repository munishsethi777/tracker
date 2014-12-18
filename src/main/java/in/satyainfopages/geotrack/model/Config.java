package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import in.satyainfopages.geotrackbase.sqllite.ICursorToObject;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class Config implements ICursorToObject {

    public static final String TABLE_NAME = "appconfig";

    private static final String COLUMN_CONFIG_KEY = "cfgkey";
    private static final String COLUMN_CONFIG_VAL = "cfgval";

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + "(" + COLUMN_CONFIG_KEY + " TEXT , "
            + COLUMN_CONFIG_VAL + " TEXT);";

    public static final String[] CONFIG_COLUMNS = {COLUMN_CONFIG_KEY,
            COLUMN_CONFIG_VAL};

    public static String getConfigVal(Context context, String key) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = mydb.getReadableDatabase();
        Object obj = mydb.get(new Config(), Config.TABLE_NAME, CONFIG_COLUMNS, Config.COLUMN_CONFIG_KEY + " = ?",
                new String[]{String.valueOf(key)}, null, null, null,null);
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;

//            if (cursor.moveToFirst()) {
//                do {
//                    String value = cursor.getString(1);
//                    return value;
//                } while (cursor.moveToNext());
//            }
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//        return null;
    }

    public static void SaveConfig(Context context, String key, String Val, boolean isUpdate) {
        boolean isExist = false;
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

        if (isUpdate) {
            String exist = getConfigVal(context, key);

            if (exist != null) {
                isExist = true;

                ContentValues values = new ContentValues();
                values.put(COLUMN_CONFIG_VAL, Val);
                mydb.update(Config.TABLE_NAME, values,
                        COLUMN_CONFIG_KEY + " = ?", new String[]{key});


            }

            if (!isExist) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_CONFIG_KEY, key);
                values.put(COLUMN_CONFIG_VAL, Val);
                mydb.insert(Config.TABLE_NAME, null, values);
            }


        }

    }

    @Override
    public Object parseCursor(Cursor cursor) {
        String value = cursor.getString(1);
        return value;
    }
}
