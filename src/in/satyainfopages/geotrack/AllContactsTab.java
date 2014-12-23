package in.satyainfopages.geotrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

import in.satyainfopages.geotrack.model.ApiDependency;
import in.satyainfopages.geotrack.model.Contact;
import in.satyainfopages.geotrack.model.IConstants;
import in.satyainfopages.geotrack.model.User;
import in.satyainfopages.geotrackbase.util.ITaskHandler;
import in.satyainfopages.geotrackbase.util.TaskHandler;

/**
 * Created by DalbirSingh on 19-12-2014.
 */
public class AllContactsTab extends Fragment implements ITaskHandler<JSONObject> {
    private static final String TAG = "in.satya.AllContactsTab";
    private ListView listView = null;
    private long groupSeq;
    private View v;
    private List<Contact> contacts = null;

    private TaskHandler<Void, Void> taskHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getBundleExtra("BUNDLE");
        groupSeq = bundle.getLong("GROUP_SEQ");
    }

    private void sendInvitation() {

        String numbers = "";
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < len; i++)
            if (checked.get(i)) {

                Contact item = contacts.get(i);
                if (numbers == "") {
                    numbers = item.getNumber();
                } else {
                    numbers = numbers + "," + item.getNumber();
                }


            }
        Toast.makeText(v.getContext(), "Sending invitation to - " + numbers, Toast.LENGTH_SHORT).show();
        User user = ApiDependency.getOwner(v.getContext(), false);

        String url = IConstants.GROUP_REQUEST_URL;
        url = MessageFormat.format(url, user.getUserSeq(), numbers, groupSeq);
        taskHandler = new TaskHandler<Void, Void>(url, this, getActivity());
        taskHandler.showProgress(true, "Sending....");
        taskHandler.execute((Void) null);

    }

    private void uncheckedAll() {
        int len = listView.getCount();
        for (int i = 0; i < len; i++) {
            try {
                listView.setItemChecked(i, false);
            } catch (Exception e) {
                Log.e(TAG, "Error while unchecked all...", e);
            }
        }
    }

    private void checkNos(String selNos) {
        String[] nos = selNos.split(",");

        for (String no : nos) {
            try {
                listView.setItemChecked(Integer.valueOf(no), true);
            } catch (Exception e) {
                Log.e(TAG, "Error while selecting numbers...", e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contacts_list_view, container, false);
        taskHandler = null;
        listView = (ListView) v.findViewById(R.id.listContacts);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);

        Button btnSend = new Button(v.getContext());
        btnSend.setText("Send");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitation();
            }
        });
        listView.addFooterView(btnSend);
        contacts = ApiDependency.getAllContacts(v.getContext(), false);//getFilteredContacts(v.getContext(), false);
        ArrayAdapter<Contact> ada = new ArrayAdapter<Contact>(v.getContext(), R.layout.contacts_list_item, contacts);
        listView.setAdapter(ada);
        return v;
    }


    @Override
    public void TaskComplete(JSONObject jsonObject, Throwable throwable) {
        String errMessage = "We are unable to send invitation due to some issue.Please retry after sometime. ";
        taskHandler.showProgress(false, "");
        int isSuccess = 0;
        if (throwable != null || jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Invitation..");

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
                        String result = "Request Sent Successfully..";
                        String already = "";
                        String fail = "";
                        String indexes = "";
                        if (jsonObject.has("alreadysent")) {
                            already = jsonObject.getString("alreadysent");
                        }
                        if (jsonObject.has("failure")) {
                            fail = jsonObject.getString("failure");
                        }
                        if (already == "" && fail == "") {
                            Toast.makeText(getActivity(), result,
                                    Toast.LENGTH_LONG).show();
                        } else if (fail != "") {
                            String[] nos = fail.split(",");
                            String name = "";
                            int index = 0;
                            for (String str : nos) {
                                index = 0;
                                for (Contact contact : contacts) {
                                    if (contact.getNumber().equalsIgnoreCase(str)) {
                                        if (name == "") {
                                            name = contact.getName();
                                            indexes = "" + index;
                                        } else {
                                            name = name + "," + contact.getName();
                                            indexes = indexes + "," + index;
                                        }
                                    }
                                    index = index + 1;
                                }
                            }
                            uncheckedAll();
                            checkNos(indexes);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Invitation..");
                            builder.setMessage("Failed to send request to " + name + ". Do you want to resend the request?");

                            builder.setPositiveButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sendInvitation();
                                            return;
                                        }
                                    });
                            builder.setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            getActivity().finish();
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        } else if (already != "") {
//TODO: already part....
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error while parsing response...",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void TaskCancel() {
        taskHandler.showProgress(false, "");
    }
}