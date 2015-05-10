package project.cs442.team9.georeminder;

/**
 * Created by akash on 4/18/15.
 */

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;


public class Mapv2 extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    GoogleApiClient mGoogleApiClient;
    Location current, pointer;
    LatLng c;
    Marker currentM, select, now;
    String addressP = "...";
    GoogleMap mapc;
    MapFragment mapFragment;
    CircleOptions circleOptions;



    static final LatLng TutorialsPoint = new LatLng(21 , 57);
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
            mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
       //     mapFragment.getMapAsync(this);
            //  Toast.makeText(this,current.toString()+"asdasdasd",Toast.LENGTH_LONG).show();

            mapc = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            mapc.setMyLocationEnabled(true);
            DBO db=new DBO(this);
            Cursor cr=db.getdata(db);
            double lat,lon;
            while(cr.moveToNext())
            {
                lat=cr.getDouble(1);
                lon=cr.getDouble(2);
                LatLng l=new LatLng(lat,lon);
                if(cr.getInt(7)==1){
                circleOptions=new CircleOptions().center(l).radius(100).fillColor(0x40ff0000)
                        .strokeColor(Color.TRANSPARENT).strokeWidth(2);
            }
                else{
                   circleOptions=new CircleOptions().center(l).radius(100).fillColor(0x400000ff)
                            .strokeColor(Color.TRANSPARENT).strokeWidth(2);
                }
                mapc.addCircle(circleOptions);
                mapc.addMarker(new MarkerOptions()
                       .position(l)
                       .title(cr.getString(0)));
            }
           // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
         //   Marker TP = googleMap.addMarker(new MarkerOptions().
           //         position(TutorialsPoint).title("TutorialsPoint"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}