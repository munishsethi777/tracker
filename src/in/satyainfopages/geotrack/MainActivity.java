package in.satyainfopages.geotrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Group;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserLocation;
import in.satyainfopages.geotrackbase.util.DateUtil;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener, OnMapReadyCallback, ITaskHandler<JSONObject> {
    private static final String TAG = "in.satya.mainactivity";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TaskHandler<Void, Void> taskHandler = null;
    private ShareActionProvider mShareActionProvider;
    private SpinnerAdapter mSpinnerAdapter;
    private List<Group> groups = null;
    private Group group = null;
    private List<UserLocation> userLocations = null;
    private GoogleMap googleMap = null;

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

        }
    }

    private void startUserActivity() {
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        restoreActionBar();

        try {
            this.startService(new Intent(this, TrackerService.class));

        } catch (Exception e) {

            Log.e(TAG, "Starting service err..", e);
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (userLocations != null) {
            UserLocation userLocation = userLocations.get(position);
            if (googleMap != null) {
                LatLng latLng = new LatLng(userLocation.getLat(), userLocation.getLng());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            }
        }

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
            groups.add(groups.size(), tmpGroup);
            mSpinnerAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, groups);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(mSpinnerAdapter, MainActivity.this);
            //  actionBar.setSelectedNavigationItem(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getSharedIntent());

        // restoreActionBar();
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

    private void requestAction() {
        Intent requestIntent = new Intent(this, RequestHandlerActivity.class);
        startActivity(requestIntent);


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
            case R.id.action_requests:
                requestAction();
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
            } else {
                showGroupData(group);
            }

            return true;
        }
        return false;
    }

    private void showGroupData(Group group) {
        if (group != null) {
            String groupUrl = IConstants.GET_TRACKING_GROUP_URL;
            groupUrl = MessageFormat.format(groupUrl, group.getGroupSeq());
            taskHandler = new TaskHandler<Void, Void>(groupUrl, this, this);
            //  taskHandler.showProgress(true, "");
            taskHandler.execute((Void) null);
        }
    }

    private void showGroupMembers(List<UserLocation> userLocations) {
        if (googleMap != null) {
            googleMap.clear();
            if (userLocations != null && userLocations.size() > 0) {
                LatLng latLng = new LatLng(userLocations.get(0).getLat(), userLocations.get(0).getLng());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                for (UserLocation userLocation : userLocations) {
                    latLng = new LatLng(userLocation.getLat(), userLocation.getLng());
                    googleMap.addMarker(new MarkerOptions()
                            .title(userLocation.getUserName())
                            .snippet("Hi, I am here now .")
                            .position(latLng));
                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showGroupMembers(userLocations);
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        //   googleMap.setMyLocationEnabled(true);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        googleMap.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
    }

    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        String errMessage = "We are unable to get group info due to some issue.Please retry after sometime. ";
        // taskHandler.showProgress(false, "");
        if (userLocations != null) {
            userLocations.clear();
        } else {
            userLocations = new ArrayList<UserLocation>();
        }

        taskHandler = null;
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Group..");
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
                try {
                    isSuccess = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (isSuccess == 1) {
                        JSONArray arr = jsonObject.getJSONArray("data");
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject locData = (JSONObject) arr.getJSONObject(j);
                            long userSeq = locData.getLong("userseq");
                            String userName = locData.getString("username");
                            String locDate = locData.getString("dated");
                            String lng = locData.getString("longitude");
                            String lat = locData.getString("latitude");
                            UserLocation userLocation = new UserLocation();
                            userLocation.setLat(Double.parseDouble(lat));
                            userLocation.setLng(Double.parseDouble(lng));
                            userLocation.setStampDate(DateUtil.ConvertStrToDate(locDate));
                            userLocation.setUserName(userName);
                            userLocation.setUserseq(userSeq);

                            userLocations.add(userLocation);
                        }
                        mNavigationDrawerFragment.updateAdapter(userLocations);
                        showGroupMembers(userLocations);
//                        setResult(RESULT_OK);
//                        finish();
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
        //  taskHandler.showProgress(false, "");
        taskHandler = null;
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
