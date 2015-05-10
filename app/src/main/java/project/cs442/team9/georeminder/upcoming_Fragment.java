package project.cs442.team9.georeminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Datta on 4/4/2015.
 */
public class upcoming_Fragment extends Fragment {

    public Button btn;
    View rootview;
    ArrayList<String> values;
    List<ListItem1> listItemList;
    ListView listview;
    ArrayAdapter<String> adapter;
    private SQLiteDatabase mydb;
    DBO dbo;
    ArrayList<String> AUTHOR_ARRAYLIST;
    ArrayAdapter<ListItem1> ad;
    Cursor cr;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Create Table
        Log.d("Table", "start");
        //SqlDatabase dbEntry = new SqlDatabase(this);
        //     mydb = openOrCreateDatabase("arafaliy",null);
        //     mydb.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Todo VARCHAR);");
        dbo=new DBO(getActivity());
        listItemList=new ArrayList<ListItem1>();
        Log.d("Table", "Success");


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showGlobalContextActionBar();
        rootview = inflater.inflate(R.layout.upcoming_events, container, false);


           //mydb = openOrCreateDatabase("arafaliy",null);
        listview = (ListView) rootview.findViewById(R.id.listView);
        final ArrayList<String> values = new ArrayList<String>();
           // values.add("R");
      cr=dbo.getdata(dbo);
        while(cr.moveToNext())
        {
            //ToDoItem td=new ToDoItem(resultSet.getString(0));
            // Toast.makeText(this, "hi " + resultSet.getString(0), Toast.LENGTH_LONG).show();
            //      values.add(cr.getString(0));
            if(cr.getInt(7)==0) {
                listItemList.add(new ListItem1(cr.getString(0), 0, cr.getString(5), "Date"));
            }
            }
            // aa.notifyDataSetChanged();
        btn = (Button) rootview.findViewById(R.id.button);

        ad=new MyListAdapter(upcoming_Fragment.this.getActivity(),listItemList);
        //adapter = new ArrayAdapter<String>(upcoming_Fragment.this.getActivity(), android.R.layout.simple_list_item_1, values);
        listview.setAdapter(ad);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getView().setContentView(R.layout.activity_create_reminder);
                Intent intent = new Intent(upcoming_Fragment.this.getActivity(), CreateReminder.class);
                //startActivityForResult(intent,0);
                upcoming_Fragment.this.startActivityForResult(intent, 0);
            }
        });


        //List Item Long click listener

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



        // add data from DB
        ad.notifyDataSetChanged();


        return rootview;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(re)
        Log.d("Result_code", String.valueOf(resultCode));
        if (resultCode == -1) {
            Log.d("Result_code", String.valueOf(resultCode));
            String s = data.getStringExtra("location");
            String adr=data.getStringExtra("adr");
            Log.d("--->DATA LIST:", s);
            // values[3]=s;
            //adapter.add(new String(s));
            ad.add(new ListItem1(s,0,adr,"5-8-2015"));
            ad.notifyDataSetChanged();
            //  listview.setAdapter(adapter);
            Log.d("-------->Address:", s);
            // selected_location.setText(s);

        }
    }


    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("Upcoming");
        actionBar.setIcon(R.drawable.ic_launcher);


    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }




}
