package in.satyainfopages.geotrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Contact;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrackbase.util.HttpUtil;


/**
 * Created by DalbirSingh on 17-12-2014.
 */
public class InviteActivity extends BaseActivity {
    private static final String TAG = "in.satya.inivtation";
    ListView listView = null;
    long groupSeq;
    private List<Contact> contacts = null;
    private InviteFriendsTask inviteFriendsTask = null;
    private View mFormView;
    private View mProgressView;
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_view);

        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");
//        if (bundle == null) {
//            bundle = this.getIntent().getBundleExtra("bundle");
//        }
//        if (savedInstanceState != null) {
//            userSeq = savedInstanceState.getString("userSeq");
//            fullName = savedInstanceState.getString("fullName");
//        } else

        groupSeq = bundle.getLong("GROUP_SEQ");
//        mFormView = findViewById(R.id.invitation_form);
//        mProgressView = findViewById(R.id.invitation_progress);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        // Create a progress bar to display while the list loads
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//        progressBar.setIndeterminate(true);
        listView = (ListView) findViewById(R.id.listContacts);//getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);
        // listView.setEmptyView(progressBar);
        Button btnSend = new Button(this);
        btnSend.setText("Send");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitation();
            }
        });
        listView.addFooterView(btnSend);
//        // Must add the progress bar to the root of the layout
//        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
//        root.addView(progressBar);

        contacts = ApiDependency.getFilteredContacts(this, false);
        ArrayAdapter<Contact> ada = new ArrayAdapter<Contact>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, contacts);
        listView.setAdapter(ada);


    }

    private void sendInvitation() {
        if (inviteFriendsTask != null) {
            return;
        }
        // ListView listView = getListView();
        String numbers = "";
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < len; i++)
            if (checked.get(i)) {

                Contact item = contacts.get(i);
                if (numbers == "") {
                    numbers = item.getNumber();
                } else {
                    numbers = numbers + "," + item.getNumber();
                }

                  /* do whatever you want with the checked item */
            }
        Toast.makeText(this, "Sending invitation to - " + numbers, Toast.LENGTH_SHORT).show();
        User user = ApiDependency.getOwner(this, false);
        //  showProgress(true);
        inviteFriendsTask = new InviteFriendsTask(numbers, user.getUserSeq(), groupSeq, this);//email, password, mobile_no, full_name);
        inviteFriendsTask.execute((Void) null);
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        //---toggle the check displayed next to the item---

    }


    /**
     * Shows the progress UI and hides the login form.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

    public class InviteFriendsTask extends AsyncTask<Void, Void, String> {

        String numbers;
        long userSeq;
        long groupSeq;
        Context context;

        InviteFriendsTask(String numbers, long userSeq, long groupSeq, Context context) {
            this.numbers = numbers;
            this.userSeq = userSeq;
            this.groupSeq = groupSeq;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String errMessage = "We are unable to send invitation due to some issue.Please retry after sometime. ";

            if (!HttpUtil.isInternetOn(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), R.string.error_internet_connection,
                        Toast.LENGTH_LONG).show();
            }
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error while sending invitation", e);
                return errMessage;
            }
            // Object[] args = {mMobileNo, mEmail, mPassword, mFullName};
            String url = IConstants.GROUP_REQUEST_URL;
            url = MessageFormat.format(url, userSeq, numbers, groupSeq);

            int isSuccess = 0;
            String message = "";
            int isExists = 0;
            //  String userSeq = "";
            Long groupSeq = 0L;
            String groupName = "";


            try {
                String response = new HttpUtil().hitURL(url);
                if (response.isEmpty()) {

                } else {
                    JSONObject json = new JSONObject(response);
                    isSuccess = json.getInt("success");
                    message = json.getString("message");
                    if (isSuccess == 1) {
//                        //  userSeq = String.valueOf(json.get("seq"));
//                        user.setUserSeq(json.getLong("seq"));
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
                Log.e(TAG, "Error while sending invitation", e);
            }
            try {
                if (isSuccess == 0) {
                    return errMessage;
                } else if (isSuccess == 1) {
//                    user.setOwner(true);
//                    user.save(getApplicationContext());
//                    Group ug = new Group();
//                    ug.setGroupSeq(groupSeq);
//                    ug.setGroupName(groupName);
//                    ug.setGroupAdmin(user.getUserSeq());
//                    ug.setDefault(true);
//                    ug.save(getApplicationContext());
//                    UserGroup.save(getApplicationContext(), ug.getGroupSeq(), user.getUserSeq());
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
            inviteFriendsTask = null;
            //   showProgress(false);

            if (success == "") {
                Toast.makeText(context, R.string.invitation_success_message,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
//
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Invitation..");

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
            inviteFriendsTask = null;
            //   showProgress(false);
        }
    }


}