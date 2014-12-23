package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import in.satyainfopages.geotrackbase.sqllite.ICursorToObject;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class UserGroup implements ICursorToObject {

    public static final String TABLE_NAME = "usergroup";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public static final String COLUMN_USER_SEQ = "userseq";
    public static final String COLUMN_GROUP_SEQ = "groupseq";

    public static final String SELECT_ALL = "SELECT " + COLUMN_USER_SEQ + ","
            + COLUMN_GROUP_SEQ + " FROM " + TABLE_NAME;
    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + COLUMN_USER_SEQ + " INTEGER, " + COLUMN_GROUP_SEQ + " INTEGER);";
    public static final String[] COLUMNS = {COLUMN_USER_SEQ, COLUMN_GROUP_SEQ};

    public static void save(Context context, long groupSeq, long userSeq) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
        ContentValues values = new ContentValues();
        values.put(UserGroup.COLUMN_GROUP_SEQ, groupSeq);
        values.put(UserGroup.COLUMN_USER_SEQ, userSeq);
        mydb.insert(UserGroup.TABLE_NAME,
                null,
                values);
    }

    @Override
    public Object parseCursor(Cursor cursor) {
        return null;
    }
}
