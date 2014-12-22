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
import in.satyainfopages.geotrackbase.util.HttpUtil;


public class LoginActivity extends Activity {

    private static final String TAG = "in.satya.login";

    private UserLoginTask mAuthTask = null;


    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String userSeq;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = this.getIntent().getBundleExtra("bundle");

        if (bundle != null) {
            userSeq = bundle.getString("userSeq");
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
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void forgotPasswordAction() {

    }

    public void attemptLogin() {
        if (mAuthTask != null) {
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

            showProgress(true);
            mAuthTask = new UserLoginTask(userSeq, password, fullName);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

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

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mUserSeq;
        private final String mPassword;
        private final String mFullName;

        UserLoginTask(String userSeq, String password, String fullName) {
            mUserSeq = userSeq;
            mPassword = password;
            mFullName = fullName;
        }

        @Override
        protected String doInBackground(Void... params) {
            String errMessage = "We are unable to process login due to some issue. Please retry after sometime. ";

            if (!HttpUtil.isInternetOn(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), R.string.error_internet_connection,
                        Toast.LENGTH_LONG).show();
            }
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error while Login", e);
                return errMessage;
            }
            String loginUrl = IConstants.LOGIN_URL;
            loginUrl = MessageFormat.format(loginUrl, mUserSeq, mPassword);

            int isSuccess = 0;
            String message = "";
            int matches = 0;
            String mobileNo = "";
            String email = "";
            try {
                String response = new HttpUtil().hitURL(loginUrl);
                if (response.isEmpty()) {

                } else {
                    JSONObject json = new JSONObject(response);
                    isSuccess = json.getInt("success");
                    message = json.getString("message");
                    if (isSuccess == 1) {

                        matches = json.getInt("matches");
                        if (matches == 1) {

                            mobileNo = json.getString("mobile");
                            email = json.getString("email");

                        } else if (matches == 0) {
                            return getString(R.string.error_incorrect_password);
                        }
                    }
                    errMessage = message;
                }

            } catch (Exception e) {
                isSuccess = 0;
                Log.e(TAG, "Error while Login", e);
            }
            if (isSuccess == 0) {
                return errMessage;
            } else if (isSuccess == 1) {
//                MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
//                db.SaveConfig(IConstants.USER_MOBILE, mobileNo);
//                db.SaveConfig(IConstants.USER_SEQ, userSeq);
//                db.SaveConfig(IConstants.USER_EMAIL, email);
//                db.SaveConfig(IConstants.USER_FULL_NAME, mFullName);
//                setResult(RESULT_OK);
                // finish();
            }


            return "";
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);

            if (success == "") {
                finish();
            } else {
                //  mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(mLoginFormView.getContext());

                builder.setTitle("Login..");

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

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



