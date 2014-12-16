package in.satyainfopages.geotrack.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by DalbirSingh on 11-12-2014.
 */
public class HttpUtil extends AsyncTask<String, String, String> {

    private static final String TAG = "in.satya.http";

    public static String readUrlFile(String url) {
        String response = "";

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Uri.decode(url));
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                if (!response.equals("")) {
                    response += "\n";
                }
                response += s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String hitURL(String url) {
        String response = "";
        try {
            Log.i(TAG, "Making HTTP call to URL: " + url);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);// Uri.decode(url));
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                if (!response.equals("")) {
                    response += "\n";
                }
                response += s;
            }
            Log.i(TAG, "HTTP Registration Call result "
                    + response);
        } catch (Exception e) {
            String err = (e.getMessage() == null) ? "Exception in HTTP call for registration"
                    : e.getMessage();
            Log.e(TAG, err, e);
        }
        return response;
    }

    public static final boolean isInternetOn(Context ctxt) {
        ConnectivityManager connec = (ConnectivityManager) ctxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getState() == NetworkInfo.State.CONNECTED) {

                return true;
            } else if (activeNetwork.getState() == NetworkInfo.State.DISCONNECTED) {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        for (String url : urls) {
            Log.i(TAG, "Making HTTP call to URL: " + url);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Uri.encode(url));
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    if (!response.equals("")) {
                        response += "\n";
                    }
                    response += s;
                }

                Log.i(TAG, "HTTP Registration Call result "
                        + response);
            } catch (Exception e) {
                String err = (e.getMessage() == null) ? "Exception in HTTP call for registration"
                        : e.getMessage();
                Log.e(TAG, err, e);
            }
        }
        return response;
    }

    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
