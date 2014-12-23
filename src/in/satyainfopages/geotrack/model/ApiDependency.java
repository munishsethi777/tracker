package in.satyainfopages.geotrack.model;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.satyainfopages.geotrack.R;
import in.satyainfopages.geotrackbase.util.HttpUtil;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class ApiDependency {
    private static final String TAG = "in.satya.apidepenedency";
    private static User user;
    private static SqlLiteDB sqlLiteDB;
    private static List<Contact> numbers = null;
    private static List<Contact> filteredNumbers = null;
    private static boolean IsTaskRunning = false;

    private static FilterContactsTask filterContactsTask = null;

    public static User getOwner(Context context, boolean reload) {
        if (user == null || reload) {
            user = User.getOwner(context);
        }
        return user;
    }

    public static SqlLiteDB getDBContext(boolean reload) {
        if (sqlLiteDB == null || reload) {
            sqlLiteDB = new SqlLiteDB();
        }
        return sqlLiteDB;
    }


    public static List<Contact> getAllContacts(Context context, boolean reload) {
        if (numbers == null || reload || numbers.size() == 0) {
            ArrayList<Contact> allNumbers = new ArrayList<Contact>();
            allNumbers = getContacts(context);
            numbers = allNumbers;// filterList(allNumbers);
        }
        return numbers;
    }

    public static void LoadFilteredProcess(Context context) {
        if (filterContactsTask != null) {
            return;
        }
        filterContactsTask = new FilterContactsTask(context, ApiDependency.getAllContacts(context, false));
        filterContactsTask.execute((Void) null);
        ApiDependency.IsTaskRunning = true;
    }

    public static List<Contact> getFilteredContacts(Context context, boolean reload) {
//        while (ApiDependency.IsTaskRunning) {
//            Log.i(TAG, "FilteringNumbers");
//        }
        if (filteredNumbers == null || reload || filteredNumbers.size() == 0) {

            List<Contact> allNumbers = getAllContacts(context, reload);
            filteredNumbers = filterList(allNumbers, false);
        }
        return filteredNumbers;
    }

    public static String commaSeparatedNumber(List<Contact> contacts) {
        String str = "";
        if (contacts != null && contacts.size() > 0) {
            for (Contact contact : contacts) {
                if (str == "") {
                    str = contact.getNumber();
                } else {
                    str = str + "," + contact.getNumber();
                }
            }
        }
        return str;
    }

    public static List<Contact> filterList(List<Contact> contacts, boolean isFromTask) {
        ArrayList<Contact> filteredList = new ArrayList<Contact>();
        if (contacts != null && contacts.size() > 0) {
            String str = commaSeparatedNumber(contacts);
            String contactsUrl = IConstants.MY_CONTACTS_URL;
            contactsUrl = contactsUrl + str;//URLEncoder.encode(str);
            int isSuccess = 0;
            String message = "";
            String respNumbers = "";
            try {
                String response = "";
                HttpUtil httpUtil = new HttpUtil();
                if (isFromTask) {
                    response = httpUtil.hitURL(contactsUrl);
                } else {
                    response = httpUtil.execute(contactsUrl).get();
                }
                if (response.isEmpty()) {

                } else {
                    JSONObject json = new JSONObject(response);
                    isSuccess = json.getInt("success");
                    message = json.getString("message");
                    if (isSuccess == 1) {
                        //  userSeq = String.valueOf(json.get("seq"));
                        respNumbers = json.getString("numbers");
                        String[] nos = respNumbers.split(",");
                        for (String tmp : nos) {
                            for (Contact contact : contacts) {
                                if (contact.getNumber().equalsIgnoreCase(tmp)) {
                                    filteredList.add(contact);
                                    break;
                                }
                            }
                        }

                    }


                }

            } catch (Exception e) {
                isSuccess = 0;
                Log.e(TAG, "Error while filteringContacts", e);
            }

        }
        return filteredList;
    }

    private static String cleanNumber(String str) {
        String tmpStr = str;
        if (tmpStr != null) {
            tmpStr = tmpStr.replace("(", "").replace(")", "").replace(" ", "").replace("-", "").replace("+", "");

            if (tmpStr.length() > 10) {
                while (tmpStr.startsWith("0") && tmpStr.length() > 1) {
                    tmpStr = tmpStr.substring(1);
                }
            }
            if (tmpStr.length() == 10) {
                tmpStr = "91" + tmpStr;
            }
        }
        return tmpStr;
    }

    public static ArrayList<Contact> getContacts(Context context) {
        ArrayList<Contact> allNumbers = new ArrayList<Contact>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);


        while (cursor.moveToNext()) {


            int phone_type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            switch (phone_type) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:

                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    String contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (!phoneNumber.contains("@")) {
                        phoneNumber = cleanNumber(phoneNumber);
                        allNumbers.add(new Contact(contactId, phoneNumber, name));
                    }


                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:

            }

        }
        return allNumbers;
    }

    private static boolean getIsTaskRunning() {
        return ApiDependency.IsTaskRunning;
    }

    private static class FilterContactsTask extends AsyncTask<Void, Void, List<Contact>> {

        Context context;
        List<Contact> contacts;

        FilterContactsTask(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        @Override
        protected List<Contact> doInBackground(Void... params) {

            if (!HttpUtil.isInternetOn(context)) {
                Toast.makeText(context, R.string.error_internet_connection,
                        Toast.LENGTH_LONG).show();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error while filtering contacts", e);

            }
            filteredNumbers = new ArrayList<Contact>();
            List<Contact> tmpList = new ArrayList<Contact>();
            int i = 0;
            if (contacts != null && contacts.size() > 0) {
                for (Contact contact : contacts) {
                    i = i + 1;
                    tmpList.add(contact);
                    if (i == 300) {
                        List<Contact> tmpList1 = ApiDependency.filterList(tmpList, true);
                        if (tmpList1 != null && tmpList1.size() > 0) {
                            filteredNumbers.addAll(tmpList1);
                        }
                        tmpList.clear();
                        i = 0;
                    }
                }
                if (i != 0) {
                    List<Contact> tmpList1 = ApiDependency.filterList(tmpList, true);
                    if (tmpList1 != null && tmpList1.size() > 0) {
                        filteredNumbers.addAll(tmpList1);
                    }

                }
            }

           return filteredNumbers;
        }

        @Override
        protected void onPostExecute(final List<Contact> success) {
            filterContactsTask = null;
            filteredNumbers = success;
            ApiDependency.IsTaskRunning = false;
        }

        @Override
        protected void onCancelled() {
            filterContactsTask = null;
            ApiDependency.IsTaskRunning = false;

        }
    }


}
