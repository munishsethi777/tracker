package in.satyainfopages.geotrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrackbase.util.HttpUtil;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class GroupActivity extends Activity {

    private static final String TAG = "in.satya.groupactivity";
    private GroupSavingTask groupSavingTask = null;

    // UI references.

    private EditText txtGroup;
    private View mProgressView;
    private View mLoginFormView;

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

        mLoginFormView = findViewById(R.id.group_form);
        mProgressView = findViewById(R.id.group_progress);
    }


    public void saveGroup() {
        if (groupSavingTask != null) {
            return;
        }

        // Reset errors.
        txtGroup.setError(null);

        // Store values at the time of the login attempt.

        String groupName = txtGroup.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(groupName)) {
            txtGroup.setError(getString(R.string.error_field_required));
            focusView = txtGroup;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Group group = new Group();
            User user = ApiDependency.getOwner(getApplicationContext(), false);
            group.setGroupAdmin(user.getUserSeq());
            group.setGroupName(groupName);

            groupSavingTask = new GroupSavingTask(group);
            groupSavingTask.execute((Void) null);


        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() >= 11;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GroupSavingTask extends AsyncTask<Void, Void, String> {

        Group group;

        GroupSavingTask(Group group) {
            this.group = group;
        }

        @Override
        protected String doInBackground(Void... params) {
            String errMessage = "We are unable to update group due to some issue.Please retry after sometime. ";

            if (!HttpUtil.isInternetOn(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), R.string.error_internet_connection,
                        Toast.LENGTH_LONG).show();
            }
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error while registration", e);
                return errMessage;
            }
            // Object[] args = {mMobileNo, mEmail, mPassword, mFullName};
            String regUrl = IConstants.CREATE_GROUP_URL;
            regUrl = MessageFormat.format(regUrl, group.getGroupName(), group.getGroupAdmin()); //mMobileNo, mEmail, mPassword, mFullName);

            int isSuccess = 0;
            String message = "";
//            int isExists = 0;
//            //  String userSeq = "";
//            Long groupSeq = 0L;
//            String groupName = "";


            try {
                String response = new HttpUtil().hitURL(regUrl);
                if (response.isEmpty()) {

                } else {
                    JSONObject json = new JSONObject(response);
                    isSuccess = json.getInt("success");
                    message = json.getString("message");
                    if (isSuccess == 1) {
                        //  userSeq = String.valueOf(json.get("seq"));
                        group.setGroupSeq(json.getLong("seq"));
//                        isExists = json.getInt("isexists");
//                        if (isExists == 1) {
//                            user.setFullName(json.getString("fullname"));
//                            // mFullName = json.getString("fullname");
//                            Bundle bundle = new Bundle();
//
//                            bundle.putLong("userSeq", user.getUserSeq());
//                            bundle.putString("fullName", user.getFullName());
//                            bundle.putBoolean("EXIST", true);
//                            Intent i = new Intent();
//
//                            i.putExtras(bundle);
//                            setResult(RESULT_OK, i);
//                            return "";
//                        } else if (isExists == 0) {
//                            groupSeq = json.getLong("groupseq");
//                            groupName = json.getString("groupname");
//                        }
                    }
                    errMessage = message;
                }

            } catch (Exception e) {
                isSuccess = 0;
                Log.e(TAG, "Error while creating group on server..", e);
            }
            try {
                if (isSuccess == 0) {
                    return errMessage;
                } else if (isSuccess == 1) {
                    group.save(getApplicationContext());
                    setResult(RESULT_OK);
                    // finish();
                }
            } catch (Exception e) {
                setResult(RESULT_CANCELED);
                Log.e(TAG, "Error while saving group in db..", e);
            }

            return "";
        }

        @Override
        protected void onPostExecute(final String success) {
            groupSavingTask = null;
            showProgress(false);

            if (success == "") {
                finish();
            } else {
//
                AlertDialog.Builder builder = new AlertDialog.Builder(mLoginFormView.getContext());

                builder.setTitle("Group..");

                builder.setMessage(success);
                // Add the buttons
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            groupSavingTask = null;
            showProgress(false);
        }
    }
}



