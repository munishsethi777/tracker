package in.satyainfopages.geotrack.model;

import android.content.Context;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class ApiDependency {

    private static User user;

    public static User getOwner(Context context, boolean reload) {
        if (user == null || reload) {
            user = User.getOwner(context);
        }
        return user;
    }


}
