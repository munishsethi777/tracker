package in.satyainfopages.geotrack.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import in.satyainfopages.geotrackbase.sqllite.ICursorToObject;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;

/**
 * Created by DalbirSingh on 27-12-2014.
 */
public class GroupRequest implements ICursorToObject {

    public static final String TABLE_NAME = "geogrouprequest";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GROUP_SEQ = "groupseq";
    public static final String COLUMN_GROUP_NAME = "groupname";
    public static final String COLUMN_REQUEST_FROM = "requestfrom";
    public static final String COLUMN_REQUEST_STATUS = "requeststatus";

    public static final String SELECT_ALL = "SELECT " + COLUMN_ID + ","
            + COLUMN_GROUP_SEQ + "," + COLUMN_GROUP_NAME + "," + COLUMN_REQUEST_FROM + "," + COLUMN_REQUEST_STATUS + " FROM " + TABLE_NAME;

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROUP_SEQ + " INTEGER, " + COLUMN_GROUP_NAME + " TEXT, "
            + COLUMN_REQUEST_FROM + " TEXT, " + COLUMN_REQUEST_STATUS + " TEXT);";

    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_GROUP_SEQ, COLUMN_GROUP_NAME,
            COLUMN_REQUEST_FROM, COLUMN_REQUEST_STATUS};

    public static final String PENDING_STATUS = "PENDING";
    public static final String REJECTED_STATUS = "REJECTED";
    public static final String ACCEPTED_STATUS = "ACCEPTED";

    private long id;
    private long groupSeq;
    private String groupName;
    private String reqFrom;
    private String reqStatus; // PENDING,REJECTED,ACCEPTED

    public GroupRequest() {
    }

    public static List<GroupRequest> getPendingRequests(Context context) {
        List<GroupRequest> groupRequests = null;

        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));

        groupRequests = mydb.<GroupRequest>getList(new GroupRequest(), GroupRequest.TABLE_NAME,
                GroupRequest.COLUMNS,
                GroupRequest.COLUMN_REQUEST_STATUS + " = ?",
                new String[]{GroupRequest.PENDING_STATUS},
                null,
                null,
                GroupRequest.COLUMN_ID + " DESC", null);
        return groupRequests;
    }

    public void update(Context context) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
        ContentValues values = new ContentValues();
        values.put(GroupRequest.COLUMN_GROUP_SEQ, this.getGroupSeq());
        values.put(GroupRequest.COLUMN_GROUP_NAME, this.getGroupName());
        values.put(GroupRequest.COLUMN_REQUEST_FROM, this.getReqFrom());
        values.put(GroupRequest.COLUMN_REQUEST_STATUS, this.getReqStatus());
        mydb.update(GroupRequest.TABLE_NAME, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(this.getId())});

    }


//    public static List<GroupRequest> getGroupsByUserSeq(Context context, long userSeq) {
//        List<GroupRequest> groups = null;
//
//        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
//
//        groups = mydb.<GroupRequest>getList(new GroupRequest(), GroupRequest.TABLE_NAME,
//                GroupRequest.COLUMNS,
//                Group.COLUMN_ADMIN + " = ?",
//                new String[]{String.valueOf(userSeq)},
//                null,
//                null,
//                null, null);
//        return groups;
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReqFrom() {
        return reqFrom;
    }

    public void setReqFrom(String reqFrom) {
        this.reqFrom = reqFrom;
    }

//    public Group(long groupSeq, String groupName) {
//        this.groupSeq = groupSeq;
//        this.groupName = groupName;
//    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
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


    public void save(Context context) {
        MySQLiteHelper mydb = new MySQLiteHelper(context, ApiDependency.getDBContext(false));
        ContentValues values = new ContentValues();
        values.put(GroupRequest.COLUMN_GROUP_SEQ, this.getGroupSeq());
        values.put(GroupRequest.COLUMN_GROUP_NAME, this.getGroupName());
        values.put(GroupRequest.COLUMN_REQUEST_FROM, this.getReqFrom());
        values.put(GroupRequest.COLUMN_REQUEST_STATUS, this.getReqStatus());
        mydb.insert(GroupRequest.TABLE_NAME,
                null,
                values);
    }

    @Override
    public String toString() {
        return reqFrom + " want you to add into group " + groupName;
    }

    @Override
    public Object parseCursor(Cursor cursor) {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(cursor.getLong(0));
        groupRequest.setGroupSeq(cursor.getLong(1));
        groupRequest.setGroupName(cursor.getString(2));
        groupRequest.setReqFrom(cursor.getString(3));
        if (!cursor.isNull(4)) {
            groupRequest.setReqStatus(cursor.getString(4));
        }
        return groupRequest;
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && other instanceof GroupRequest && ((GroupRequest) other).getId() == getId());
    }

}
