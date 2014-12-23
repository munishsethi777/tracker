package in.satyainfopages.geotrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.MessageFormat;

import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserGroup;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;


public class RegistrationActivity extends Activity implements ITaskHandler<JSONObject> {

    private static final String TAG = "in.satya.registration";
    // private UserRegisterTask mAuthTask = null;

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mMobileNoView;
    private EditText mFullNameView;
    private TaskHandler<Void, Void> taskHandler = null;
    //    private View mProgressView;
//    private View mLoginFormView;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mMobileNoView = (EditText) findViewById(R.id.mobile);
        mFullNameView = (EditText) findViewById(R.id.full_name);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }


    public void attemptRegistration() {
        if (taskHandler != null) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);


        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String mobile_no = mMobileNoView.getText().toString();
        String full_name = mFullNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


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

            focusView.requestFocus();
        } else {
//            showProgress(true);
            user = new User(email, password, mobile_no, full_name);
//            mAuthTask = new UserRegisterTask(user);//email, password, mobile_no, full_name);
//            mAuthTask.execute((Void) null);
            String regUrl = IConstants.REGISTRATION_URL;
            regUrl = MessageFormat.format(regUrl, mobile_no, email, password, URLEncoder.encode(full_name));
            taskHandler = new TaskHandler<Void, Void>(regUrl, this, this);
            taskHandler.showProgress(true, "");
            taskHandler.execute((Void) null);


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

    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        String errMessage = "We are unable to register due to some issue.Please retry after sometime. ";
        taskHandler.showProgress(false, "");
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Registration..");
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
                            setResult(RESULT_CANCELED);
                            finish();
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

                        user.setUserSeq(jsonObject.getLong("seq"));
                        int isExists = jsonObject.getInt("isexists");
                        if (isExists == 1) {
                            user.setFullName(jsonObject.getString("fullname"));
                            Bundle bundle = new Bundle();

                            bundle.putLong("userSeq", user.getUserSeq());
                            bundle.putString("fullName", user.getFullName());
                            bundle.putBoolean("EXIST", true);
                            Intent i = new Intent();

                            i.putExtras(bundle);
                            setResult(RESULT_OK, i);
                            finish();
                        } else if (isExists == 0) {
                            long groupSeq = jsonObject.getLong("groupseq");
                            String groupName = jsonObject.getString("groupname");
                            user.setOwner(true);
                            user.save(getApplicationContext());
                            Group ug = new Group();
                            ug.setGroupSeq(groupSeq);
                            ug.setGroupName(groupName);
                            ug.setGroupAdmin(user.getUserSeq());
                            ug.setDefault(true);
                            ug.save(getApplicationContext());
                            UserGroup.save(getApplicationContext(), ug.getGroupSeq(), user.getUserSeq());

                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Error while parsing and saving response...",
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

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public void showProgress(final boolean show) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }


//    public class UserRegisterTask extends AsyncTask<Void, Void, String> {
//
//        User user;
//
//
//        UserRegisterTask(User user) {
//            this.user = user;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String errMessage = "We are unable to register due to some issue.Please retry after sometime. ";
//
//            if (!HttpUtil.isInternetOn(getApplicationContext())) {
//                Toast.makeText(getApplicationContext(), R.string.error_internet_connection,
//                        Toast.LENGTH_LONG).show();
//            }
//            try {
//
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "Error while registration", e);
//                return errMessage;
//            }
//
//            String regUrl = IConstants.REGISTRATION_URL;
//            regUrl = MessageFormat.format(regUrl, user.getMobileNo(), user.getEmail(), user.getPassword(), URLEncoder.encode(user.getFullName()));


//   int isSuccess = 0;
//            String message = "";
//            int isExists = 0;
//
//            Long groupSeq = 0L;
//            String groupName = "";
//
//
//            try {
//                String response = new HttpUtil().hitURL(regUrl);
//                if (response.isEmpty()) {
//
//                } else {
//                    JSONObject json = new JSONObject(response);
//                    isSuccess = json.getInt("success");
//                    message = json.getString("message");
//                    if (isSuccess == 1) {
//
//                        user.setUserSeq(json.getLong("seq"));
//                        isExists = json.getInt("isexists");
//                        if (isExists == 1) {
//                            user.setFullName(json.getString("fullname"));
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
//                    }
//                    errMessage = message;
//                }
//
//            } catch (Exception e) {
//                isSuccess = 0;
//                Log.e(TAG, "Error while registration", e);
//            }
//            try {
//                if (isSuccess == 0) {
//                    return errMessage;
//                } else if (isSuccess == 1) {
//                    user.setOwner(true);
//                    user.save(getApplicationContext());
//                    Group ug = new Group();
//                    ug.setGroupSeq(groupSeq);
//                    ug.setGroupName(groupName);
//                    ug.setGroupAdmin(user.getUserSeq());
//                    ug.setDefault(true);
//                    ug.save(getApplicationContext());
//                    UserGroup.save(getApplicationContext(), ug.getGroupSeq(), user.getUserSeq());
//
//                    setResult(RESULT_OK);
//
//                }
//            } catch (Exception e) {
//                setResult(RESULT_CANCELED);
//                Log.e(TAG, "Error while saving user in db..", e);
//            }
//
//            return "";
//        }

//        @Override
//        protected void onPostExecute(final String success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success == "") {
//                finish();
//            } else {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(mLoginFormView.getContext());
//
//                builder.setTitle("Registration..");
//
//                builder.setMessage(success);
//
//                builder.setPositiveButton(R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                return;
//                            }
//                        });
//                builder.setNegativeButton(R.string.exit,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                setResult(RESULT_CANCELED);
//                                finish();
//                            }
//                        });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//}
//}



