package in.satyainfopages.geotrack.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.satyainfopages.geotrack.model.Config;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserGroup;
import in.satyainfopages.geotrack.model.UserLocation;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "in.satya.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "geo_tracker";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private HashMap<String, String> ht = null;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public synchronized static String getDateTime(Date date) {
        // Date date = new Date();
        return dateFormat.format(date);
    }

    public synchronized static Date ConvertStrToDate(String str) {
        if (str != null) {
            try {
                // SimpleDateFormat sdf = new SimpleDateFormat(
                // "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dt = dateFormat.parse(str);
                return dt;
            } catch (Exception e) {
                Log.e(TAG, "Error while converting date", e);
            }
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(UserLocation.CREATE_TABLE);
        db.execSQL(Group.CREATE_TABLE);
        db.execSQL(UserGroup.CREATE_TABLE);
        db.execSQL(Config.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older books table if existed
//        db.execSQL(UserLocation.DROP_TABLE);
//        db.execSQL(Config.getDropSql());
//        // create fresh books table
//        this.onCreate(db);
    }

//    public User LoadUser() {
//        User user = new User();
//        String val = this.getConfigVal(IConstants.USER_SEQ, true);
//        if (val != null) {
//            user.setUserSeq(Long.parseLong(val));
//        }
//        val = this.getConfigVal(IConstants.USER_EMAIL);
//        if (val != null) {
//            user.setEmail(val);
//        }
//        val = this.getConfigVal(IConstants.USER_FULL_NAME);
//        if (val != null) {
//            user.setFullName(val);
//        }
//        val = this.getConfigVal(IConstants.USER_MOBILE);
//        if (val != null) {
//            user.setMobileNo(val);
//        }
//        ApiDependency.setUser(user);
//        return user;
//    }

//    private void deleteKey(String key) {
//
//        // / 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 2. delete
//        db.delete(CONFIG_TABLE_NAME, // table name
//                COLUMN_CONFIG_KEY + " = ?", // selections
//                new String[]{key}); // selections
//        // args
//
//        // 3. close
//        db.close();
//
//        // log
//        Log.d("deleteKey", key);
//
//    }

//    public void SaveConfig(String key, String val) {
//        SaveConfig(key, val, false);
//    }
//
//    public void SaveConfig(String Key, String Val, boolean isUpdate) {
//        boolean isExist = false;
//
//        if (isUpdate) {
//            String exist = getConfigVal(Key, true);
//            SQLiteDatabase db = this.getWritableDatabase();
//            if (exist != null) {
//                isExist = true;
//                // 2. create ContentValues to add key "column"/value
//                ContentValues values = new ContentValues();
//                values.put(COLUMN_CONFIG_VAL, Val); // get title
//
//                // 3. updating row
//                db.update(CONFIG_TABLE_NAME, values,
//                        COLUMN_CONFIG_KEY + " = ?", new String[]{Key});
//
//                // 4. close
//                db.close();
//            }
//        }
//        if (!isExist) {
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_CONFIG_KEY, Key);
//            values.put(COLUMN_CONFIG_VAL, Val);
//
//            db.insert(CONFIG_TABLE_NAME, null, values);
//
//            db.close();
//        }
//
//    }
//
//    public String getConfigVal(String key) {
//        return getConfigVal(key, false);
//    }
//
//    public String getConfigVal(String Key, boolean reload) {
//        if (ht == null || reload) {
//            ht = new HashMap<String, String>();
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = null;
//            try {
//
//                cursor = db.query(CONFIG_TABLE_NAME, CONFIG_COLUMNS, null,
//                        null, null, null, null);
//
//                if (cursor.moveToFirst()) {
//                    do {
//                        String key = cursor.getString(0);
//                        String value = cursor.getString(1);
//                        ht.put(key, value);
//                    } while (cursor.moveToNext());
//                }
//
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//
//        }
//        if (ht.containsKey(Key)) {
//            return ht.get(Key);
//        }
//        return null;
//    }






}
