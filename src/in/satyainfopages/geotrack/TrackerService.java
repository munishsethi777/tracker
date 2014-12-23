package in.satyainfopages.geotrack;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserLocation;
import in.satyainfopages.geotrackbase.util.GeoLocationMgr;


public class TrackerService extends Service {
    private static final String TAG = "in.satya.trackerservice";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {
        Log.i(TAG, "service creating..");

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();


        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper,
                getApplicationContext());
        Message msg = mServiceHandler.obtainMessage();

        mServiceHandler.sendMessage(msg);

        HandlerThread thread2 = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread2.start();
        Looper mServiceLooper2 = thread2.getLooper();
        LocationPushService pushSrvcHandler = new LocationPushService(
                mServiceLooper2, getApplicationContext());
        pushSrvcHandler.sendMessage(pushSrvcHandler.obtainMessage());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "service starting..");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "service Destroyed..");
    }


    private final class ServiceHandler extends Handler {

        Context ctxt = null;

        public ServiceHandler(Looper looper, Context ctxt) {
            super(looper);
            this.ctxt = ctxt;
        }

        @Override
        public void handleMessage(Message msg) {
            boolean gps_enabled = false, network_enabled = false;
            LocationManager mlocManager = null;
            User user = null;
            while (true) {
                if (user == null) {
                    user = ApiDependency.getOwner(ctxt, false);
                }

                try {
                    Thread.sleep(10 * 1000);

                    if (mlocManager == null)
                        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    try {
                        gps_enabled = mlocManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }
                    try {
                        network_enabled = mlocManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    if (!gps_enabled && !network_enabled) {
                        String netMsg = "Location Service is not enabled.";
                        Log.w(TAG, netMsg);
                        Toast.makeText(ctxt, netMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        LocationListener mlocListener = new GeoLocationMgr();
                        mlocManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 0, 0,
                                mlocListener);
                        Location loc = mlocManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {

                            if (user != null) {
                                UserLocation ul = new UserLocation();
                                ul.setUserseq(user.getUserSeq());
                                ul.setLat(loc.getLatitude());
                                ul.setLng(loc.getLongitude());
                                ul.setStampDate(new Date());
                                try {
                                    ul.Save(ctxt);
                                } catch (Exception e) {
                                    Log.e(TAG,
                                            "Error Saving locations.", e);
                                }
                            }

                        } else {
                            Log.w(TAG, "Location not grabbed.");
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error while getting location..",
                            e);

                }

            }

        }
    }
}