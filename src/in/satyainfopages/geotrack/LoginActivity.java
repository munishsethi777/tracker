package in.satyainfopages.geotrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.IConstants;
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

                            String mobileNo = jsonObject.getString("mobile");
                            String email = jsonObject.getString("email");
                            //                MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
//                db.SaveConfig(IConstants.USER_MOBILE, mobileNo);
//                db.SaveConfig(IConstants.USER_SEQ, userSeq);
//                db.SaveConfig(IConstants.USER_EMAIL, email);
//                db.SaveConfig(IConstants.USER_FULL_NAME, mFullName);
//                setResult(RESULT_OK);
                            // finish();

                        } else if (matches == 0) {
                            Toast.makeText(this, "R.string.error_incorrect_password",
                                    Toast.LENGTH_LONG).show();

                        }
                    }

//
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



