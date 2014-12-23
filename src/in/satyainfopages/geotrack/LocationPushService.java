package in.satyainfopages.geotrack;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Config;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrack.model.UserLocation;
import in.satyainfopages.geotrackbase.sqllite.MySQLiteHelper;
import in.satyainfopages.geotrackbase.util.DateUtil;
import in.satyainfopages.geotrackbase.util.HttpUtil;


public class LocationPushService extends Handler {
    private static final String TAG = "in.satya.loc_push_service";

    Context ctxt = null;
    MySQLiteHelper db = null;

    public LocationPushService(Looper looper, Context ctxt) {
        super(looper);
        this.ctxt = ctxt;
        db = new MySQLiteHelper(ctxt, ApiDependency.getDBContext(false));
    }

    @Override
    public void handleMessage(Message msg) {
        User user = null;
        while (true) {
            if (user == null) {
                user = ApiDependency.getOwner(ctxt, false);
            }

            String LastLocSeq = Config.getConfigVal(
                    ctxt, IConstants.USER_LAST_PUSHED_LOC_SEQ);

            int gap = 20;

            if (gap <= 0) {
                gap = 20;
            }
            try {
                Thread.sleep(gap * 1000);

                if (user != null) {
                    if (HttpUtil.isInternetOn(ctxt)) {
                        long locSeq = Long
                                .parseLong(LastLocSeq == null ? "0" : LastLocSeq);
                        UserLocation.deleteLocationsBySeq(ctxt, locSeq);
                        List<UserLocation> locs = UserLocation.getAllUserLocs(ctxt, user.getUserSeq(), 20, locSeq);
                        int isSuccess = 0;
                        String message = "";
                        for (UserLocation ul : locs) {
                            isSuccess = 0;
                            message = "";
                            JSONObject jsLoc = new JSONObject();
                            jsLoc.put("long", ul.getLng());
                            jsLoc.put("lat", ul.getLat());
                            jsLoc.put("dated",
                                    DateUtil.getDateTime(ul.getStampDate()));
                            JSONArray jsArr = new JSONArray();
                            jsArr.put(jsLoc);

                            JSONObject json = new JSONObject();
                            json.put("useq", ul.getUserseq());

                            json.put("locations", jsArr);

                            String url = IConstants.SET_TRACKING_URL;
                            url = url + URLEncoder.encode(json.toString());
                            String response = "0";
                            try {

                                response = HttpUtil.hitURL(url);
                                JSONObject resJson = new JSONObject(response);
                                isSuccess = resJson.getInt("success");
                                message = resJson.getString("message");
                                if (isSuccess == 1) {
                                    Config.SaveConfig(ctxt,
                                            IConstants.USER_LAST_PUSHED_LOC_SEQ,
                                            String.valueOf(ul.getId()), true);
                                } else {

                                    break;
                                }


                            } catch (Exception e) {
                                Log.e(TAG,
                                        "Error uploading locations.", e);
                            }
                        }
                    }
                }

            } catch (Throwable e) {
                Log.e(TAG,
                        "Error while uploading location (outer handler)..", e);
            }
        }

    }
}