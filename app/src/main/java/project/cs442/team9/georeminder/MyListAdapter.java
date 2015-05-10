package project.cs442.team9.georeminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash on 5/8/15.
 */
public class MyListAdapter extends ArrayAdapter<ListItem1> {
    Context mcontext;
    ListItem1 li;
    List<ListItem1> l2;

    public MyListAdapter(Context context, List<ListItem1> l) {

        super(context, R.layout.customlist, l);
       mcontext=context;
        l2=l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=convertView;
        if(rowView==null)
{      LayoutInflater inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         rowView = inflater.inflate(R.layout.customlist, parent, false);}
        TextView textView = (TextView) rowView.findViewById(R.id.textView6);
        //TextView textView2 = (TextView) rowView.findViewById(R.id.textView7);
        TextView textView3= (TextView) rowView.findViewById(R.id.textView8);

        li=l2.get(position);
        textView.setText(li.listname);
        textView3.setText(li.address);

        return rowView;

    }
}
