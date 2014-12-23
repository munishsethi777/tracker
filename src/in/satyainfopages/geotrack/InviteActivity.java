package in.satyainfopages.geotrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;


/**
 * Created by DalbirSingh on 17-12-2014.
 */
public class InviteActivity extends BaseActivity {
    private static final String TAG = "in.satya.inivtation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    private class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                mFragment.setArguments(getIntent().getBundleExtra("BUNDLE"));
                ft.add(android.R.id.content, mFragment, mTag);
            } else {

                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {

                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

    }


}
