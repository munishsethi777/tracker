package in.satyainfopages.geotrack.model;

import android.database.sqlite.SQLiteDatabase;

import in.satyainfopages.geotrackbase.sqllite.ISqlLiteDB;

/**
 * Created by DalbirSingh on 17-12-2014.
 */
public class SqlLiteDB implements ISqlLiteDB {


    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "geo_tracker";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(User.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserLocation.CREATE_TABLE);
        sqLiteDatabase.execSQL(Group.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserGroup.CREATE_TABLE);
        sqLiteDatabase.execSQL(Config.CREATE_TABLE);
        sqLiteDatabase.execSQL(GroupRequest.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        if (i == 1 && i2 == 2) {
            sqLiteDatabase.execSQL(GroupRequest.CREATE_TABLE);
        }
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}
