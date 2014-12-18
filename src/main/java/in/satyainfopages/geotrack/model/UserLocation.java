package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.satyainfopages.geotrackbase.sqllite.ICursorToObject;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;
import in.satyainfopages.geotrackbase.util.DateUtil;

public class UserLocation implements ICursorToObject {

    public static final String TABLE_NAME = "trackinglocations";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_SEQ = "userseq";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_STAMP_DATE = "stampdate";
    // Database creation sql statement
    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_SEQ + " integer, " + COLUMN_LAT + " double, "
            + COLUMN_LNG + " double, " + COLUMN_STAMP_DATE + " datetime);";
    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_USER_SEQ,
            COLUMN_LAT, COLUMN_LNG, COLUMN_STAMP_DATE};

    public static final String SELECT_ALL = "SELECT " + COLUMN_ID + ","
            + COLUMN_USER_SEQ + "," + COLUMN_LAT + "," + COLUMN_LNG + ","
            + COLUMN_STAMP_DATE + " FROM " + TABLE_NAME;

    private long id;
    private long userseq;
    private double lat;
    private double lng;
    private Date stampDate;

    public static List<UserLocation> getAllUserLocs(Context context, long userSeq, int limit, long lastSeq) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
        List<UserLocation> locs = new ArrayList<UserLocation>();

//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = mydb.getReadableDatabase();

        String condition = UserLocation.COLUMN_USER_SEQ + " = ?";
        String[] vals = null;
        if (lastSeq > 0) {
            condition = condition + " and " + UserLocation.COLUMN_ID
                    + " > ?";
            vals = new String[]{String.valueOf(userSeq),
                    String.valueOf(lastSeq)};
        } else {
            vals = new String[]{String.valueOf(userSeq)};
        }
        String lmt = "20";
        if (limit != 0) {
            lmt = String.valueOf(limit);
        }
        locs = mydb.<UserLocation>getList(new UserLocation(), UserLocation.TABLE_NAME,
                UserLocation.COLUMNS,
                condition,
                vals,
                null,
                null,
                null,
                lmt);


//            if (cursor.moveToFirst()) {
//                do {
//                    locs.add(UserLocation.cursorToUserLoc(cursor));
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

        return locs;
    }


    public static void deleteLocationsBySeq(Context context, long locSeq) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
        mydb.delete(TABLE_NAME,
                COLUMN_ID + " <= ?",
                new String[]{String.valueOf(locSeq)});


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getStampDate() {
        return stampDate;
    }

    public void setStampDate(Date stampDate) {
        this.stampDate = stampDate;
    }

    public long getUserseq() {
        return userseq;
    }

    public void setUserseq(long userseq) {
        this.userseq = userseq;
    }

    public synchronized void Save(Context context) throws Exception {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

//        SQLiteDatabase db = null;
//        try {
//            db = mydb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserLocation.COLUMN_USER_SEQ, this.getUserseq());
        values.put(UserLocation.COLUMN_LAT, this.getLat());
        values.put(UserLocation.COLUMN_LNG, this.getLng());
        values.put(UserLocation.COLUMN_STAMP_DATE,
                DateUtil.getDateTime(this.getStampDate()));


        mydb.insert(UserLocation.TABLE_NAME,
                null,
                values);

//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
    }

    public UserLocation getUserLoc(Context context, long id) {

        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = mydb.getReadableDatabase();
        mydb.get(this, UserLocation.TABLE_NAME,
                UserLocation.COLUMNS,
                UserLocation.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,null);


//            if (cursor != null) {
//                cursor.moveToFirst();
//                return UserLocation.cursorToUserLoc(cursor);
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }


        return null;

    }

    @Override
    public Object parseCursor(Cursor cursor) {
        UserLocation ul = new UserLocation();
        ul.setId(cursor.getLong(0));
        ul.setUserseq(cursor.getLong(1));
        ul.setLat(cursor.getDouble(2));
        ul.setLng(cursor.getDouble(3));
        try {
            ul.setStampDate(DateUtil.ConvertStrToDate(cursor.getString(4)));
        } catch (Exception e) {
            Log.e("in.satya.userlocation", "Error while parsing date", e);
        }
        return ul;
    }
}
