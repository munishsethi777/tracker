package in.satyainfopages.geotrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserGroup;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;


public class LoginActivity extends Activity implements ITaskHandler<JSONObject> {

    private static final String TAG = "in.satya.login";
    private TaskHandler<Void, Void> taskHandler = null;

    private EditText mPasswordView;
    private long userSeq;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = this.getIntent().getBundleExtra("bundle");

        if (bundle != null) {
            userSeq = bundle.getLong("userSeq");
            fullName = bundle.getString("fullName");
        }


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mForgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);
        mForgotPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordAction();
            }
        });
        TextView textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        String msg = "Welcome Back " + fullName;
        textViewWelcome.setText(msg);

    }

    public void forgotPasswordAction() {

    }

    public void attemptLogin() {
        if (taskHandler != null) {
            return;
        }


        mPasswordView.setError(null);


        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String loginUrl = IConstants.LOGIN_URL;
            loginUrl = MessageFormat.format(loginUrl, userSeq, password);
            taskHandler = new TaskHandler<Void, Void>(loginUrl, this, this);
            taskHandler.showProgress(true, "");
            taskHandler.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        String errMessage = "We are unable to update group due to some issue.Please retry after sometime. ";
        taskHandler.showProgress(false, "");
        taskHandler = null;
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login..");
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
                int matches = 0;
                try {

                    isSuccess = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (isSuccess == 1) {

                        matches = jsonObject.getInt("matches");
                        if (matches == 1) {
                            long userSeq = jsonObject.getLong("useq");
                            String mobileNo = jsonObject.getString("mobile");
                            String email = jsonObject.getString("email");
                            String fullName = jsonObject.getString("fullname");
                            JSONArray arr = jsonObject.getJSONArray("groups");
                            User user = new User();
                            user.setOwner(true);
                            user.setEmail(email);
                            user.setMobileNo(mobileNo);
                            user.setFullName(fullName);
                            user.setUserSeq(userSeq);
                            user.save(getApplicationContext());
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject groupData = (JSONObject) arr.getJSONObject(j);
                                long groupSeq = groupData.getLong("gseq");
                                String groupName = groupData.getString("gname");
                                Group ug = new Group();
                                ug.setGroupSeq(groupSeq);
                                ug.setGroupName(groupName);
                                ug.setGroupAdmin(user.getUserSeq());
                                if (j == 0) {
                                    ug.setDefault(true);
                                }
                                ug.save(getApplicationContext());
                                UserGroup.save(getApplicationContext(), ug.getGroupSeq(), user.getUserSeq());
                            }
                            setResult(RESULT_OK);
                            finish();

                        } else if (matches == 0) {
                            Toast.makeText(this, "R.string.error_incorrect_password",
                                    Toast.LENGTH_LONG).show();

                        }
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



