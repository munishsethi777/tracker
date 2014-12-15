package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import in.satyainfopages.geotrack.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 13-12-2014.
 */

public class User {


    public static final String TABLE_NAME = "geouser";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public static final String COLUMN_SEQ = "seq";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ISOWNER = "isowner";

    public static final String SELECT_ALL = "SELECT " + COLUMN_SEQ + ","
            + COLUMN_NAME + "," + COLUMN_MOBILE + "," + COLUMN_EMAIL + ","
            + COLUMN_PASSWORD + ","
            + COLUMN_ISOWNER + " FROM " + TABLE_NAME;

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + COLUMN_SEQ + " INTEGER, " + COLUMN_NAME + " TEXT, "
            + COLUMN_MOBILE + " TEXT, " + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT, " + COLUMN_ISOWNER + " NUMERIC);";

    public static final String[] COLUMNS = {COLUMN_SEQ, COLUMN_NAME,
            COLUMN_MOBILE, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_ISOWNER};

    private Long userSeq;
    private String fullName;
    private String email;
    private String mobileNo;
    private String password;
    private boolean isOwner;

    public User() {

    }

    public User(String email, String password, String mobileNumber, String fullName) {
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNumber;
        this.fullName = fullName;
    }

    public static User getOwner(Context context) {
        MySQLiteHelper mydb = new MySQLiteHelper(context);

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mydb.getReadableDatabase();

            cursor = db.query(User.TABLE_NAME,
                    User.COLUMNS,
                    User.COLUMN_ISOWNER + " = ?",
                    new String[]{String.valueOf(1)},
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursorToUser(cursor);
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

    private static User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setUserSeq(cursor.getLong(0));
        user.setFullName(cursor.getString(1));
        user.setMobileNo(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setPassword(cursor.getString(4));
        user.setOwner(Boolean.parseBoolean(cursor.getString(5)));
        return user;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void save(Context context) {
        MySQLiteHelper mydb = new MySQLiteHelper(context);

        SQLiteDatabase db = null;
        try {
            db = mydb.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(User.COLUMN_SEQ, this.getUserSeq());
            values.put(User.COLUMN_MOBILE, this.getMobileNo());
            values.put(User.COLUMN_EMAIL, this.getEmail());
            values.put(User.COLUMN_PASSWORD, this.getPassword());
            values.put(User.COLUMN_NAME, this.getFullName());
            values.put(User.COLUMN_ISOWNER, this.isOwner());

            db.insert(User.TABLE_NAME,
                    null,
                    values);

        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    public List<Group> getGroups(Context context) {
        return Group.getGroupsByUserSeq(context, this.getUserSeq());
    }
}
