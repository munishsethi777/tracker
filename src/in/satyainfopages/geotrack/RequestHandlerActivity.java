package in.satyainfopages.geotrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.GroupRequest;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserGroup;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;

/**
 * Created by DalbirSingh on 27-12-2014.
 */
public class RequestHandlerActivity extends BaseActivity implements CustomListAdapter.IAdapterActions<GroupRequest>, ITaskHandler<JSONObject> {
    public static final String ACCEPT_ACTION = "Accept";
    public static final String REJECT_ACTION = "Reject";
    public static final int NOTIFICATION_ID = 1471;
    private static final String TAG = "in.satya.RequestHandlerActivity";
    private TaskHandler<Void, Void> taskHandler = null;
    private GroupRequest groupRequest;
    private CustomListAdapter<GroupRequest> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_handler);

        List<GroupRequest> groupRequests = GroupRequest.getPendingRequests(getApplicationContext());
        adapter = new CustomListAdapter<GroupRequest>(groupRequests, this, this);
        ListView lView = (ListView) findViewById(R.id.listPendingRequest);
        lView.setAdapter(adapter);
    }

    public void handleAction(GroupRequest groupRequest, int isAccepted) {
        if (taskHandler != null) {
            return;
        }
        String groupRequestUrl = IConstants.RESPOND_GROUP_REQUEST_URL;
        User user = ApiDependency.getOwner(getApplicationContext(), false);
        groupRequestUrl = MessageFormat.format(groupRequestUrl, user.getUserSeq(), groupRequest.getGroupSeq(), isAccepted);
        taskHandler = new TaskHandler<Void, Void>(groupRequestUrl, this, this);
        taskHandler.showProgress(true, "");
        taskHandler.execute((Void) null);

    }

    @Override
    public void positiveAction(GroupRequest obj) {
        obj.setReqStatus(GroupRequest.ACCEPTED_STATUS);
        this.groupRequest = obj;
        handleAction(obj, 1);
        Toast.makeText(this, obj.toString() + " accept ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void negativeAction(GroupRequest obj) {
        obj.setReqStatus(GroupRequest.REJECTED_STATUS);
        this.groupRequest = obj;
        handleAction(obj, 0);
        Toast.makeText(this, obj.toString() + " reject ...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        GroupRequest groupRequest = this.groupRequest;
        String errMessage = "We are unable to respond to your request this time due to some issue.Please retry after sometime. ";
        taskHandler.showProgress(false, "");
        taskHandler = null;
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Group Request..");
            builder.setMessage(errMessage);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });
//            builder.setNegativeButton(R.string.exit,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (jsonObject != null) {
                try {
                    isSuccess = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (isSuccess == 1) {
                        groupRequest.update(getApplicationContext());
                        if (groupRequest.getReqStatus() == GroupRequest.ACCEPTED_STATUS) {
                            Group group = new Group();
                            group.setGroupSeq(groupRequest.getGroupSeq());
                            group.setGroupName(groupRequest.getGroupName());
                            User user = ApiDependency.getOwner(getApplicationContext(), false);
                            group.setGroupAdmin(user.getUserSeq());
                            group.save(getApplicationContext());
                            UserGroup.save(getApplicationContext(), group.getGroupSeq(), user.getUserSeq());
                            adapter.removeItem(groupRequest);
                        } else if (groupRequest.getReqStatus() == GroupRequest.REJECTED_STATUS) {
                            adapter.removeItem(groupRequest);
                        }
//                        setResult(RESULT_OK);
//                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error while parsing response...",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error while parsing response...", e);
                }
            }
        }
    }

    @Override
    public void TaskCancel() {
        taskHandler.showProgress(false, "");
        taskHandler = null;
    }
}
