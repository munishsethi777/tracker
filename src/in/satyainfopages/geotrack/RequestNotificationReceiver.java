package in.satyainfopages.geotrack;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by DalbirSingh on 27-12-2014.
 */
public class RequestNotificationReceiver extends BroadcastReceiver {

    public RequestNotificationReceiver() {

    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String action = arg1.getAction();

        if (RequestHandlerActivity.ACCEPT_ACTION.equals(action)) {
            Toast.makeText(arg0, "in the broadcast.", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);
            notificationManager.cancel(RequestHandlerActivity.NOTIFICATION_ID);
        }
    }
}