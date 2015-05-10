package project.cs442.team9.georeminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Datta on 4/4/2015.
 */
public class completed_Fragment extends Fragment {

    View rootview;
    public Button btn;
    GeofenceStore gs;
    DBO dbo;
    List<ListItem1> listItemList;
    Cursor cr;
    ListView listview;
    ArrayAdapter<ListItem1> ad;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        showGlobalContextActionBar();
        rootview= inflater.inflate(R.layout.completed_events,container,false);
        listview = (ListView) rootview.findViewById(R.id.listView2);
        btn = (Button)rootview.findViewById(R.id.button4);
        gs=new GeofenceStore(completed_Fragment.this.getActivity());

        dbo=new DBO(getActivity());
        listItemList=new ArrayList<ListItem1>();

        cr=dbo.getdata(dbo);
        while(cr.moveToNext())
        {
            //ToDoItem td=new ToDoItem(resultSet.getString(0));
            // Toast.makeText(this, "hi " + resultSet.getString(0), Toast.LENGTH_LONG).show();
            //      values.add(cr.getString(0));
       if(cr.getInt(7)==1) {

           listItemList.add(new ListItem1(cr.getString(0), 0, cr.getString(5), "Date"));
       }
          //  listview.setAdapter(ad);
       }
        ad=new MyListAdapter(completed_Fragment.this.getActivity(),listItemList);
        listview.setAdapter(ad);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ListItem1 l1=ad.getItem(position);


                dbo.delete(dbo,l1.listname);
                ad.remove(ad.getItem(position));
                // dbo.delete(dbo,adapter.getItem(position));
                // Log.d("---->",adapter.getItem(position));
                //  adapter.notifyDataSetChanged();
                ad.notifyDataSetChanged();
                // dbo.delete(dbo,adapter.getItem(position));
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(completed_Fragment.this.getActivity(),Mapv2.class);
                //startActivityForResult(intent,0);
                startActivity(intent);

                //getView().setContentView(R.layout.activity_create_reminder);
//                Intent intent = new Intent(completed_Fragment.this.getActivity(),CreateReminder.class);
                //startActivityForResult(intent,0);
  //              completed_Fragment.this.startActivity(intent);
            }
        });
        return rootview;




    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("Completed");
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

}
