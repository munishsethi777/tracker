package in.satyainfopages.geotrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * Created by DalbirSingh on 17-12-2014.
 */
public class InviteActivity extends BaseActivity {
    private static final String TAG = "in.satya.inivtation";
   // ListView listView = null;
    long groupSeq;
  //  private List<Contact> contacts = null;
   // private InviteFriendsTask inviteFriendsTask = null;
   // private View mFormView;
  //  private View mProgressView;
  //  private ProgressBar progressBar1;
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");
        groupSeq = bundle.getLong("GROUP_SEQ");
//        progressBar1 = (ProgressBar) findViewById(R.id.invitation_progress);
//        progressBar1.setVisibility(View.GONE);
        setupTabs();

    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        TabListener<AllContactsTab> t1 = new TabListener<AllContactsTab>(this,
                "All Contacts", AllContactsTab.class);
        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("All Contacts")
                .setTabListener(t1);

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        TabListener<GeoContactsTab> t2 = new TabListener<GeoContactsTab>(this,
                "GeoTrack Members", GeoContactsTab.class);
        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("GeoTrack Members")
                .setTabListener(t2);
        actionBar.addTab(tab2);
    }


//    private void sendInvitation() {
//        if (inviteFriendsTask != null) {
//            return;
//        }
//
//        String numbers = "";
//        int len = listView.getCount();
//        SparseBooleanArray checked = listView.getCheckedItemPositions();
//        for (int i = 0; i < len; i++)
//            if (checked.get(i)) {
//
//                Contact item = contacts.get(i);
//                if (numbers == "") {
//                    numbers = item.getNumber();
//                } else {
//                    numbers = numbers + "," + item.getNumber();
//                }
//
//
//            }
//        Toast.makeText(this, "Sending invitation to - " + numbers, Toast.LENGTH_SHORT).show();
//        User user = ApiDependency.getOwner(this, false);
//        //  showProgress(true);
//        inviteFriendsTask = new InviteFriendsTask(numbers, user.getUserSeq(), groupSeq, this, progressBar1);//email, password, mobile_no, full_name);
//        inviteFriendsTask.showProgress(true);
//        inviteFriendsTask.execute((Void) null);
//    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

    }


//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public void showProgress(final boolean show) {

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

//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

    private class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                mFragment.setArguments(getIntent().getBundleExtra("BUNDLE"));
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }

    }


}
