package in.satyainfopages.geotrack.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.satyainfopages.geotrackbase.util.HttpUtil;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public class ApiDependency {

    private static User user;
    private static SqlLiteDB sqlLiteDB;
    private static List<Contact> numbers = null;

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


    public static List<Contact> getFilteredContacts(Context context, boolean reload) {
        if (numbers == null || reload) {
            ArrayList<Contact> allNumbers = new ArrayList<Contact>();
            allNumbers = getContacts(context);
            numbers = filterList(allNumbers);
        }
        return numbers;
    }

    private static String commaSeparatedNumber(List<Contact> contacts) {
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

    private static List<Contact> filterList(List<Contact> contacts) {
        ArrayList<Contact> filteredList = new ArrayList<Contact>();
        if (contacts != null && contacts.size() > 0) {
            String str = commaSeparatedNumber(contacts);
            String contactsUrl = IConstants.MY_CONTACTS_URL;
            contactsUrl = contactsUrl + str;
            int isSuccess = 0;
            String message = "";
            String respNumbers = "";
            try {
                String response = new HttpUtil().execute(contactsUrl).get();//.hitURL(contactsUrl);
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
                Log.e("in.sataya.apidepenedency", "Error while filteringContacts", e);
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
                    tmpStr = tmpStr.substring(0);
                }
            }
            if (tmpStr.length() == 10) {
                tmpStr = "91" + tmpStr;
            }
        }
        return tmpStr;
    }

    private static ArrayList<Contact> getContacts(Context context) {
        ArrayList<Contact> allNumbers = new ArrayList<Contact>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);


        while (cursor.moveToNext()) {
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = cleanNumber(phoneNumber);
            allNumbers.add(new Contact(contactId, phoneNumber, name));
        }
        return allNumbers;
    }
}
