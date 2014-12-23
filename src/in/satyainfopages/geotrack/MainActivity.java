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
import android.widget.Toast;

import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.User;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener {
    private static final String TAG = "in.satya.mainactivity";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ShareActionProvider mShareActionProvider;
    private SpinnerAdapter mSpinnerAdapter;
    private List<Group> groups = null;
    private Group group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = ApiDependency.getOwner(getBaseContext(), true);

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
                startUserActivity();
            } else {
                finish();
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {

            }
            restoreActionBar();
        }
    }

    private void startUserActivity() {
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:

                break;
            case 2:

                break;
            case 3:

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
        ApiDependency.LoadFilteredProcess(this);
        if (group != null && group.getGroupSeq() > -1) {
            Bundle bundle = new Bundle();
            bundle.putLong("GROUP_SEQ", group.getGroupSeq());
            Intent invIntent = new Intent(this, InviteActivity.class);
            invIntent.putExtra("BUNDLE", bundle);
            startActivity(invIntent);
        }
    }

    private void syncContacts() {
        ApiDependency.getAllContacts(this, true);
        ApiDependency.LoadFilteredProcess(this);
        Toast.makeText(this, R.string.contact_sync_message,
                Toast.LENGTH_LONG).show();
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        if (groups != null) {
            group = groups.get(i);
            if (group.getGroupSeq() == -1) {
                Intent groupIntent = new Intent(this, GroupActivity.class);
                startActivityForResult(groupIntent, 3);
            }

            return true;
        }
        return false;
    }


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


}
