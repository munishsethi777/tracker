package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import in.satyainfopages.geotrackbase.sqllite.ICursorToObject;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 12-12-2014.
 */
public class Group implements ICursorToObject {
    //  private static final String GROUP_SEPARATOR = ";";
    //  private static final String GROUP_FIELD_SEPARATOR = "|";

    public static final String TABLE_NAME = "geogroup";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    public static final String COLUMN_SEQ = "seq";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADMIN = "admin";
    public static final String COLUMN_IS_DEFAULT = "isdeafult";
    public static final String SELECT_ALL = "SELECT " + COLUMN_SEQ + ","
            + COLUMN_NAME + "," + COLUMN_ADMIN + "," + COLUMN_IS_DEFAULT + " FROM " + TABLE_NAME;
    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + COLUMN_SEQ + " INTEGER, " + COLUMN_NAME + " TEXT, "
            + COLUMN_ADMIN + " INTEGER, " + COLUMN_IS_DEFAULT + " NUMERIC);";
    public static final String[] COLUMNS = {COLUMN_SEQ, COLUMN_NAME,
            COLUMN_ADMIN, COLUMN_IS_DEFAULT};
    private static ArrayList<Group> userGroups;
    private long groupSeq;
    private long groupAdmin;
    private String groupName;
    private boolean isDefault;

    public Group() {

    }

    public Group(long groupSeq, String groupName) {
        this.groupSeq = groupSeq;
        this.groupName = groupName;
    }

//    public static ArrayList<Group> getUserGroups() {
//        return userGroups;
//    }

    public static List<Group> getGroupsByUserSeq(Context context, long userSeq) {
        List<Group> groups = null;//new ArrayList<Group>();

        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
//
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = mydb.getReadableDatabase();
        groups = mydb.<Group>getList(new Group(), Group.TABLE_NAME,
                Group.COLUMNS,
                Group.COLUMN_ADMIN + " = ?",
                new String[]{String.valueOf(userSeq)},
                null,
                null,
                null,null);

//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                do {
//                    groups.add(cursorToGroup(cursor));
//                } while (cursor.moveToNext());
//
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }

        return groups;
    }

    private static Group cursorToGroup(Cursor cursor) {
        Group group = new Group();
        group.setGroupSeq(cursor.getLong(0));
        group.setGroupName(cursor.getString(1));
        group.setGroupAdmin(cursor.getLong(2));
        if (!cursor.isNull(3)) {
            group.setDefault(Boolean.parseBoolean(cursor.getString(3)));
        }
        return group;
    }

//    public static String encode() {
//        String str = "";
//        for (UserGroup ug : userGroups) {
//            String tmp = ug.getGroupSeq() + GROUP_FIELD_SEPARATOR + ug.getGroupName() + GROUP_FIELD_SEPARATOR + ug.isDefault();
//            if (str == "") {
//                str = tmp;
//            } else {
//                str = str + GROUP_SEPARATOR + tmp;
//            }
//        }
//        return str;
//    }

    public long getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(long groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    //    private static void setUserGroups(ArrayList<UserGroup> userGroups) {
//        UserGroup.userGroups = userGroups;
//    }
    public void addToList() {
        if (userGroups == null) {
            userGroups = new ArrayList<Group>();
        }
        {
            userGroups.add(this);
        }
    }

    public long getGroupSeq() {
        return groupSeq;
    }

    public void setGroupSeq(long groupSeq) {
        this.groupSeq = groupSeq;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void save(Context context) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

//        SQLiteDatabase db = null;
//        try {
//            db = mydb.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Group.COLUMN_SEQ, this.getGroupSeq());
            values.put(Group.COLUMN_NAME, this.getGroupName());
            values.put(Group.COLUMN_ADMIN, this.getGroupAdmin());
            values.put(Group.COLUMN_IS_DEFAULT, this.isDefault());
            mydb.insert(Group.TABLE_NAME,
                    null,
                    values);

//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }

    }

    @Override
    public String toString() {
        return this.getGroupName();
    }

    @Override
    public Object parseCursor(Cursor cursor) {
        Group group = new Group();
        group.setGroupSeq(cursor.getLong(0));
        group.setGroupName(cursor.getString(1));
        group.setGroupAdmin(cursor.getLong(2));
        if (!cursor.isNull(3)) {
            group.setDefault(Boolean.parseBoolean(cursor.getString(3)));
        }
        return group;
    }

//    public ArrayList<UserGroup> parseString(String encodedStr) {
//        if (userGroups == null) {
//            userGroups = new ArrayList<UserGroup>();
//        } else {
//            userGroups.clear();
//        }
//        if (encodedStr != null && encodedStr.isEmpty()) {
//            String[] groups = encodedStr.split(GROUP_SEPARATOR);
//            for (String group : groups) {
//                String[] str = group.split(GROUP_FIELD_SEPARATOR);
//                UserGroup ug = new UserGroup();
//                ug.setGroupSeq(Long.parseLong(str[0]));
//                ug.setGroupName(str[1]);
//                ug.setDefault(Boolean.parseBoolean(str[2]));
//                ug.addToList();
//            }
//        }
//        return userGroups;
//    }
}
