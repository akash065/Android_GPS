package project.cs442.team9.georeminder;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class MapView extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnMapReadyCallback {
    GoogleApiClient mGoogleApiClient;
    Location current, pointer;
    LatLng c;
    Marker currentM, select, now;
    String addressP = "...";
    GoogleMap mapc;


    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        //  TextView tv= (TextView) findViewById(R.id.tv2);
        // tv.setText("Map_View");


        buildGoogleApiClient();
        // current=this.getcurrentlocation();
        Log.d("-------->dasdsadsad", "get_LOcation");

        mGoogleApiClient.connect();
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  Toast.makeText(this,current.toString()+"asdasdasd",Toast.LENGTH_LONG).show();

        mapc = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        final Context cm = this.getBaseContext();
        mapc.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                // lstLatLngs.add(point);
                //       mapc = ((MapFragment) getFragmentManager()
                //              .findFragmentById(R.id.map)).getMap();

//                pointer.setLatitude(point.latitude);
                //              pointer.setLongitude(point.longitude);
                //Toast.makeText(this,point.toString(),Toast.LENGTH_LONG).show();
                Log.d("Location Touch:", point.toString());

                //  mapc.addMarker(new MarkerOptions().position(point).title("Tap to Select"));
                if (select != null) {
                    select.remove();
                }
                Context cc = getBaseContext();
                pointer = new Location(LocationManager.NETWORK_PROVIDER);
                pointer.setLatitude(point.latitude);
                pointer.setLongitude(point.longitude);
                select = mapc.addMarker(new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("Click to add in List"));
                currentM = select;
                (new Getaddress(cc)).execute(pointer);

            }
        });

        mapc.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (!marker.equals(now)) {
                    String s = marker.getSnippet();
                    // Toast.makeText(this,s, LENGTH_LONG).show();
                    Log.d("on_MAP---->ADDRESS:", s);
                    Intent intent = new Intent();
                    intent.putExtra("address_of_location", s);
                    intent.putExtra("lat",String.valueOf(pointer.getLatitude()));
                    intent.putExtra("long",String.valueOf(pointer.getLongitude()));
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_view, menu);
        return true;
    }

    Location getcurrentlocation() {
        Location l;
        l = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        l.getAccuracy();

        //     LocationClient mLocationClient;
        return l;
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {


        current = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (current != null) {

            Log.d("-------->La:::", String.valueOf(current.getLatitude()));
            // mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            // mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            (new Getaddress(this)).execute(current);
            mapFragment.getMapAsync(this);

        }

    }


    //   current= this.getcurrentlocation();
    //   String s;
    //   s=current.toString();
    //   Toast.makeText(this,s,Toast.LENGTH_LONG).show();


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d("connection", "faild");

    }

    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("map ready Log:", "map is ready");
        if (current != null) {
            now = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(current.getLatitude(), current.getLongitude()))
                    .title("You are here"));
            // c=new LatLng(current.getLatitude(),current.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current.getLatitude(), current.getLongitude()), 13));
            currentM = now;
        }
        Toast.makeText(this, "Map is ready", LENGTH_LONG).show();
    }

    private class Getaddress extends AsyncTask<Location, Void, String> {
        Context mContext;

        Getaddress(Context c) {
            super();
            mContext = c;
        }

        protected void onPostExecute(String address) {
            // Display the current address in the UI
            addressP = address;

            // Toast.makeText(this,addressP,Toast.LENGTH_LONG).show();
            Log.d("current_asddress", address);
            currentM.setSnippet(address);
            currentM.showInfoWindow();
            //   addressLabel.setText(address);
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
            /*
            * Format the first line of address (if available),
            * city, and country name.
            */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }


            // return null;
        }
    }


}
