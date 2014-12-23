package in.satyainfopages.geotrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class GroupActivity extends Activity implements ITaskHandler<JSONObject> {

    private static final String TAG = "in.satya.groupactivity";
    private TaskHandler<Void, Void> taskHandler = null;
    private EditText txtGroup;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        txtGroup = (EditText) findViewById(R.id.txtGroup);
        Button btnDone = (Button) findViewById(R.id.done_button);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroup();
            }
        });
    }

    public void saveGroup() {
        if (taskHandler != null) {
            return;
        }


        txtGroup.setError(null);

        String groupName = txtGroup.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(groupName)) {
            txtGroup.setError(getString(R.string.error_field_required));
            focusView = txtGroup;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {


            group = new Group();
            User user = ApiDependency.getOwner(getApplicationContext(), false);
            group.setGroupAdmin(user.getUserSeq());
            group.setGroupName(groupName);

            String groupUrl = IConstants.CREATE_GROUP_URL;
            groupUrl = MessageFormat.format(groupUrl, URLEncoder.encode(group.getGroupName()), group.getGroupAdmin());
            taskHandler = new TaskHandler<Void, Void>(groupUrl, this, this);
            taskHandler.showProgress(true, "");
            taskHandler.execute((Void) null);
        }
    }

    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        String errMessage = "We are unable to update group due to some issue.Please retry after sometime. ";
        taskHandler.showProgress(false, "");
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Group..");
            builder.setMessage(errMessage);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });
            builder.setNegativeButton(R.string.exit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (jsonObject != null) {
                try {
                    isSuccess = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (isSuccess == 1) {
                        group.setGroupSeq(jsonObject.getLong("seq"));
                        group.save(getApplicationContext());
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error while parsing response...",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void TaskCancel() {
        taskHandler.showProgress(false, "");
    }

}



