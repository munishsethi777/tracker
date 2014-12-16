package in.satyainfopages.geotrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserGroup;
import in.satyainfopages.geotrack.util.HttpUtil;


public class RegistrationActivity extends Activity {

    private static final String TAG = "in.satya.registration";
    private UserRegisterTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mMobileNoView;
    private EditText mFullNameView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mMobileNoView = (EditText) findViewById(R.id.mobile);
        mFullNameView = (EditText) findViewById(R.id.full_name);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptRegistration();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String mobile_no = mMobileNoView.getText().toString();
        String full_name = mFullNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid mobile.
        if (TextUtils.isEmpty(mobile_no)) {
            mMobileNoView.setError(getString(R.string.error_field_required));
            focusView = mMobileNoView;
            cancel = true;
        } else if (!isMobileValid(mobile_no)) {
            mMobileNoView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileNoView;
            cancel = true;
        }
        if (TextUtils.isEmpty(full_name)) {
            mFullNameView.setError(getString(R.string.error_field_required));
            focusView = mFullNameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            String st = "";

            showProgress(true);
            User user = new User(email, password, mobile_no, full_name);
            mAuthTask = new UserRegisterTask(user);//email, password, mobile_no, full_name);
            mAuthTask.execute((Void) null);


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

    /**
     * Shows the progress UI and hides the login form.
     */
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
    public class UserRegisterTask extends AsyncTask<Void, Void, String> {

        //        private final String mEmail;
//        private final String mPassword;
//        private final String mMobileNo;
//        private String mFullName;
        User user;
//        UserRegisterTask(String email, String password, String mobileNumber, String fullName) {
//            mEmail = email;
//            mPassword = password;
//            mMobileNo = mobileNumber;
//            mFullName = fullName;
//        }

        UserRegisterTask(User user) {
            this.user = user;
        }

        @Override
        protected String doInBackground(Void... params) {
            String errMessage = "We are unable to register due to some issue.Please retry after sometime. ";

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
            String regUrl = IConstants.REGISTRATION_URL;
            regUrl = MessageFormat.format(regUrl, user.getMobileNo(), user.getEmail(), user.getPassword(), user.getFullName()); //mMobileNo, mEmail, mPassword, mFullName);

            int isSuccess = 0;
            String message = "";
            int isExists = 0;
            //  String userSeq = "";
            Long groupSeq = 0L;
            String groupName = "";


            try {
                String response = new HttpUtil().hitURL(regUrl);
                if (response.isEmpty()) {

                } else {
                    JSONObject json = new JSONObject(response);
                    isSuccess = json.getInt("success");
                    message = json.getString("message");
                    if (isSuccess == 1) {
                        //  userSeq = String.valueOf(json.get("seq"));
                        user.setUserSeq(json.getLong("seq"));
                        isExists = json.getInt("isexists");
                        if (isExists == 1) {
                            user.setFullName(json.getString("fullname"));
                            // mFullName = json.getString("fullname");
                            Bundle bundle = new Bundle();

                            bundle.putLong("userSeq", user.getUserSeq());
                            bundle.putString("fullName", user.getFullName());
                            bundle.putBoolean("EXIST", true);
                            Intent i = new Intent();

                            i.putExtras(bundle);
                            setResult(RESULT_OK, i);
                            return "";
                        } else if (isExists == 0) {
                            groupSeq = json.getLong("groupseq");
                            groupName = json.getString("groupname");
                        }
                    }
                    errMessage = message;
                }

            } catch (Exception e) {
                isSuccess = 0;
                Log.e(TAG, "Error while registration", e);
            }
            try {
                if (isSuccess == 0) {
                    return errMessage;
                } else if (isSuccess == 1) {
                    user.setOwner(true);
                    user.save(getApplicationContext());
                    Group ug = new Group();
                    ug.setGroupSeq(groupSeq);
                    ug.setGroupName(groupName);
                    ug.setGroupAdmin(user.getUserSeq());
                    ug.setDefault(true);
                    ug.save(getApplicationContext());
                    UserGroup.save(getApplicationContext(), ug.getGroupSeq(), user.getUserSeq());
//                MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
//                db.SaveConfig(IConstants.USER_MOBILE, user.getMobileNo());
//                db.SaveConfig(IConstants.USER_SEQ, String.valueOf(user.getUserSeq()));
//                db.SaveConfig(IConstants.USER_EMAIL, user.getEmail());
//                db.SaveConfig(IConstants.USER_FULL_NAME, user.getFullName());
//                Group ug = new Group();
//                ug.setGroupSeq(groupSeq);
//                ug.setGroupName(groupName);
//                ug.setDefault(true);
//                ug.addToList();
//                db.SaveConfig(IConstants.USER_GROUP_INFO,
//                        Group.encode());
                    setResult(RESULT_OK);
                    // finish();
                }
            } catch (Exception e) {
                setResult(RESULT_CANCELED);
                Log.e(TAG, "Error while saving user in db..", e);
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
//
                AlertDialog.Builder builder = new AlertDialog.Builder(mLoginFormView.getContext());

                builder.setTitle("Registration..");

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
            mAuthTask = null;
            showProgress(false);
        }
    }
}



