package project.cs442.team9.georeminder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


public class CreateReminder extends ActionBarActivity implements OnItemClickListener{

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4";

    String selected_locations = "Select Location from Map";
    TextView selected_location;
    TextView selected_location1;
    TextView edittext;
    CheckBox ll;
    int sil = 0;
    String adr;
    String ph = "NOT";
    Button b;
    LatLng la;
    int n;
    boolean f;
    int i = 0;
    int in=0;
    int done=0;
    String msg="Hello";
    //To create Geofence
    GoogleApiClient mGoogleApiClient;
    List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private static PendingIntent mGeofencePendingIntent;
    ArrayList<Geofence> mGeofences;
    ArrayList<LatLng> mGeofenceCoordinates;
    ArrayList<Integer> mGeofenceRadius;
    private GeofenceStore mGeofenceStore;


    //SQL Database
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);


        selected_location1 = (TextView) findViewById(R.id.autoCompleteTextView);
        edittext = (TextView) findViewById(R.id.editText);
        selected_location = (TextView) findViewById(R.id.textView);
        selected_location.setText(selected_locations);

        selected_location1.setSingleLine(false);
        edittext.setSingleLine(false);

        ll = (CheckBox) findViewById(R.id.checkBox);
        ll.setVisibility(View.GONE);

        b = (Button) findViewById(R.id.button2);
        b.setVisibility(View.GONE);

        mGeofences = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();

       // mGeofenceStore = new GeofenceStore(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String l;
        Log.d("Request_code", String.valueOf(requestCode));
        Log.d("---------", String.valueOf(resultCode));

        if (resultCode == -1) {

            String s = data.getStringExtra("address_of_location");
            Log.d("-------->Address:", s);
            l = data.getStringExtra("lat");
            la = new LatLng(Double.parseDouble(l), Double.parseDouble(data.getStringExtra("long")));
            selected_location.setText(s);
            selected_location1.setText(s);


        }
        else if(resultCode == 2)
        {
            ph=data.getStringExtra("phn");
            msg=data.getStringExtra("msg");
            Toast.makeText(this,ph,Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_reminder_save, menu);
        //getActionBar().setIcon(R.drawable.ic_launcher);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == R.id.action_example) {
            Save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setloc(View view) {

        f = haveNetworkConnection();
        if (f == true) {
            Toast.makeText(this, " INTERNET CONNECTION IS AVAILABLE ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MapView.class);
            startActivityForResult(intent, 0);

        } else {
            Toast.makeText(this, " NO INTERNET CONNECTION ", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void Save() {

        String Msg = String.valueOf(edittext.getText()); // Reading Value from Textfield
        String Msg2 = String.valueOf(selected_location1.getText()); // Location adr

        if (Msg.matches("^$")) {
            Toast.makeText(this, " Please Enter REMINDER TITLE ", Toast.LENGTH_LONG).show();
            edittext.setText("");
        } else if (Msg2.matches("^$")) {
            Toast.makeText(this, " Please Select a Location ", Toast.LENGTH_LONG).show();
            selected_location1.setText("");
        } else {
            if(ll.isChecked())
            {
                sil= 1;
            }
            Log.d("silent value", String.valueOf(sil));
            String s = (String) selected_location.getText();
            Intent intent = new Intent();
            /*
            mGeofenceCoordinates.add(la);
            mGeofenceRadius.add(100);
            mGeofences.add(new Geofence.Builder()
                    .setRequestId(Msg)
                            // The coordinates of the center of the geofence and the radius in meters.
                    .setCircularRegion(mGeofenceCoordinates.get(0).latitude, mGeofenceCoordinates.get(0).longitude, mGeofenceRadius.get(0).intValue())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(
                            Geofence.GEOFENCE_TRANSITION_ENTER
                                    | Geofence.GEOFENCE_TRANSITION_EXIT).build());            */
            // Add the geofences to the GeofenceStore object.

            //    mydb = openOrCreateDatabase("arafaliy",MODE_PRIVATE,null);
            //    mydb.execSQL("CREATE TABLE IF NOT EXISTS ToDoList(Todo VARCHAR);");
            //    mydb.execSQL("INSERT INTO ToDoList VALUES('"+Msg+"');");
            intent.putExtra("location", Msg);
            intent.putExtra("adr",Msg2);

            DBO dbo = new DBO(this);
            //float f= (float) la.latitude;
            dbo.insert(dbo, Msg,  la.latitude,la.longitude, ph, sil, Msg2,msg,done,in);
            setResult(RESULT_OK, intent);
            //DBO mDbHelper = new DBO(getActi);
            // /  DBO db=new DBO(this);
            finish();
        }

    }

    public void advanced(View v) {

        if (n == 0) {
            ll.setVisibility(View.VISIBLE);
            b.setVisibility(View.VISIBLE);
            n = 1;
        } else {
            ll.setVisibility(View.GONE);
            b.setVisibility(View.GONE);
            n = 0;
        }
    }

    public void phone(View view) {
        Intent intent = new Intent(CreateReminder.this, Phone.class);
        //startActivityForResult(intent,0);
        CreateReminder.this.startActivityForResult(intent,0);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //sb.append("&components=country:in");
            sb.append("&components=country:usa");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}

