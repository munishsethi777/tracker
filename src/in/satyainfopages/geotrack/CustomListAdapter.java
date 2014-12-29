package in.satyainfopages.geotrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DalbirSingh on 27-12-2014.
 */
public class CustomListAdapter<T> extends BaseAdapter implements ListAdapter {
    private List<T> list = new ArrayList<T>();
    private Context context;
    private IAdapterActions<T> iAdapterActions;
    private Button rejectBtn = null;
    private Button acceptBtn = null;

    public CustomListAdapter(List<T> list, Context context, IAdapterActions<T> iAdapterActions) {
        this.list = list;
        this.context = context;
        this.iAdapterActions = iAdapterActions;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {

        return 0L;
        //  return list.get(pos);
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_request_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        rejectBtn = (Button) view.findViewById(R.id.reject_btn);
        acceptBtn = (Button) view.findViewById(R.id.accept_btn);

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterActions.negativeAction(list.get(position));
//                    notifyDataSetChanged();
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterActions.positiveAction(list.get(position));
//                    notifyDataSetChanged();
            }
        });

        return view;
    }

    public void removeItem(T obj) {
        list.remove(obj);
        notifyDataSetChanged();
    }

    public interface IAdapterActions<T> {
        void positiveAction(T obj);

        void negativeAction(T obj);
    }
}