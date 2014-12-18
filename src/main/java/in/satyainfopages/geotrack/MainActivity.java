package in.satyainfopages.geotrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Contact;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.User;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener {
    private static final String TAG = "in.satya.mainactivity";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    // private CharSequence mTitle;
    private ShareActionProvider mShareActionProvider;
    private SpinnerAdapter mSpinnerAdapter;
    private List<Group> groups = null;
    private Group group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = ApiDependency.getOwner(getBaseContext(), true);

        // String mobileNO = db.getConfigVal(IConstants.USER_MOBILE, true);
        if (user == null || user.getMobileNo() == null) {
            Intent regIntent = new Intent(this, RegistrationActivity.class);
            startActivityForResult(regIntent, 1);
        } else {
            startUserActivity();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle.getBoolean("EXIST")) {

                        Intent loginIntent = new Intent(this, LoginActivity.class);
                        loginIntent.putExtra("bundle", bundle);
                        startActivityForResult(loginIntent, 2);
                    }
                } else {
                    startUserActivity();
                }

            } else {
                finish();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                // startUserActivity();
                restoreActionBar();
            }
        }
    }

    private void startUserActivity() {
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        restoreActionBar();

        try {
            this.startService(new Intent(this, TrackerService.class));

        } catch (Exception e) {

            Log.e(TAG, "Starting service err..", e);
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                //     mTitle = getString(R.string.title_section1);
                break;
            case 2:
                //       mTitle = getString(R.string.title_section2);
                break;
            case 3:
                //      mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        User user = ApiDependency.getOwner(getApplicationContext(), false);
        if (user != null) {
            groups = user.getGroups(getApplicationContext());
            Group tmpGroup = new Group(-1, "Add New Group");
            groups.add(0, tmpGroup);
            mSpinnerAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, groups);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(mSpinnerAdapter, MainActivity.this);
            actionBar.setSelectedNavigationItem(1);
        }


        //  int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");

        // If you're using sherlock, in older versions of android you are not supposed to get a reference to android.R.id.action_bar_title, So here's little hack for that.
//        if (titleId == 0) {
//            titleId =in.satyainfopages.geotrack.R.id.abs__action_bar_title;
//
//        }

        //  View titleView = findViewById(titleId);
        // attach listener to this spinnerView for handling spinner selection change
        //   Spinner spinnerView = (Spinner) getLayoutInflater().inflate(R.layout.spinner_layout, null);

        //source of ViewGroupUtils class is given at the end of this post.
        //  ViewGroupUtil.replaceView(titleView, spinnerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // if (mNavigationDrawerFragment == null || !mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getSharedIntent());
        restoreActionBar();
        // }
        return true;

    }

    private Intent getSharedIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "message");
        return intent;
    }

    private void inviteFriends() {
        List<Contact> contacts = ApiDependency.getFilteredContacts(this, true);
        if (contacts.size() > 0) {
            if (group != null && group.getGroupSeq() > -1) {
                Bundle bundle=new Bundle();
                bundle.putLong("GROUP_SEQ", group.getGroupSeq());
                Intent invIntent = new Intent(this, InviteActivity.class);
                invIntent.putExtra("BUNDLE", bundle);
                startActivity(invIntent);
            } else {

            }

        } else {
            Intent shareIntent = new Intent(this, ShareActivity.class);
            startActivity(shareIntent);
        }
    }

    private void syncContacts() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_invites:
                inviteFriends();
                return true;
            case R.id.action_sync_contacts:
                syncContacts();
                return true;
            case R.id.action_settings:
                // composeMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        if (groups != null) {
            group = groups.get(i);
            if (group.getGroupSeq() == -1) {
                Intent groupIntent = new Intent(this, GroupActivity.class);
                startActivityForResult(groupIntent, 3);
            }
            //   Toast.makeText(this.getApplicationContext(), group.getGroupName(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

//    public static class ListContentFragment extends Fragment {
//        private String mText;
//
//        @Override
//        public void onAttach(Activity activity) {
//            // This is the first callback received; here we can set the text for
//            // the fragment as defined by the tag specified during the fragment
//            // transaction
//            super.onAttach(activity);
//            mText = getTag();
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // This is called to define the layout for the fragment;
//            // we just create a TextView and set its text to be the fragment tag
//            TextView text = new TextView(getActivity());
//            text.setText(mText);
//            return text;
//        }
//    }

}
