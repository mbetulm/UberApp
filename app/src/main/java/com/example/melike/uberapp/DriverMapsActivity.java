package com.example.melike.uberapp;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverMapsActivity extends BaseActivity{

    private BaseActivity gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    public static Double Dx,Dy;
    public static Double Px,Py;

    ArgbEvaluator argbEvaluator;

    Handler mHandler;

    public static String available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);

        new GetCall().execute("http://192.168.151.160/Service1.svc/GetCall");

        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        argbEvaluator = new ArgbEvaluator();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int devHeight = displayMetrics.heightPixels;
        int devWidth = displayMetrics.widthPixels;

        this.mHandler = new Handler();
        m_Runnable.run();

    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(DriverMapsActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            new GetCall().execute("http://192.168.151.160/Service1.svc/GetCall");
            DriverMapsActivity.this.mHandler.postDelayed(m_Runnable,2000);
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.switchId);
        item.setActionView(R.layout.driver_available);
        Switch switchAB = item
                .getActionView().findViewById(R.id.switchAB);
        switchAB.setChecked(false);

        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplication(), "ON", Toast.LENGTH_SHORT)
                            .show();
                    available="YES";
                    new AvailableDrivers().execute("http://192.168.151.160/Service1.svc/IsAvailableDriver"+"/"+LoginActivity.girenDriver+"/"+available);

                } else {
                    Toast.makeText(getApplication(), "OFF", Toast.LENGTH_SHORT)
                            .show();

                    available="NO";
                    new AvailableDrivers().execute("http://192.168.151.160/Service1.svc/IsAvailableDriver"+"/"+LoginActivity.girenDriver+"/"+available);
                }
            }
        });
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        super.onMapReady(googleMap);

    }

    @Override
    protected void setUpPolyLine() {

    }

    protected void DrawLine() {
        //LatLng source = new LatLng(getUserLocation().getLatitude(), getUserLocation().getLongitude());
        LatLng source = new LatLng(39.9815652, 32.8070391);
        LatLng destination = new LatLng(39.881320099999996,32.698371099999996);

        if (source != null && destination != null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            getPolyline polyline = retrofit.create(getPolyline.class);

            String a=polyline.toString();
            Log.i("info",a);
            polyline.getPolylineData(source.latitude + "," + source.longitude, destination.latitude + "," + destination.longitude)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                            JsonObject gson = new JsonParser().parse(response.body().toString()).getAsJsonObject();
                            try {

                                Single.just(parse(new JSONObject(gson.toString())))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<List<List<HashMap<String, String>>>>() {
                                            @Override
                                            public void accept(List<List<HashMap<String, String>>> lists) throws Exception {

                                                drawPolyline(lists);
                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, Throwable t) {

                        }
                    });
        } else
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }



    class GetCall extends AsyncTask<String, Void, String> {
        String status = null;


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... connUrl) {
            HttpURLConnection conn;
            BufferedReader reader;

            try {
                final URL url = new URL(connUrl[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("GET");
                int result = conn.getResponseCode();
                if (result == 200) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        status = line;
                    }
                }

            } catch (Exception ex) {

                System.out.print(ex);


            }
            return status;
        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

                try {

                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String pId = jsonArray.getJSONObject(i).getString("passenger_id").trim();
                        String dId = jsonArray.getJSONObject(i).getString("driver_id").trim();
                        String pLat = jsonArray.getJSONObject(i).getString("passenger_lat").trim();
                        String pLang = jsonArray.getJSONObject(i).getString("passenger_lang").trim();
                        String dLat = jsonArray.getJSONObject(i).getString("destination_lat").trim();
                        String dLang = jsonArray.getJSONObject(i).getString("destionation_lang").trim();


                         Px = new Double(pLang);
                         Py = new Double(pLat);

                         Dx = new Double(dLang);
                         Dy = new Double(dLat);

                        if(dId.equals(LoginActivity.girenDriver)){
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(DriverMapsActivity.this);

                            Intent intent = new Intent(DriverMapsActivity.this,DriverMapsActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(DriverMapsActivity.this, 0, intent, 0);

                            mBuilder.setContentIntent(pendingIntent);
                            mBuilder.setSmallIcon(R.drawable.car);
                            mBuilder.setContentTitle("Uber Request");
                            mBuilder.setContentText("There is a passenger waiting for you. Please reach the specified position as soon as possible. Good trips..");

                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            mNotificationManager.notify(001, mBuilder.build());

                            mMap.addMarker(new MarkerOptions().position(new LatLng(Py, Px)).title("passenger").icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Dy, Dx)).title("destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));


                            /*Polyline polyline1 = mMap.addPolyline((new PolylineOptions())
                                    .clickable(true)
                                    .add(new LatLng(getUserLocation().getLatitude(), getUserLocation().getLongitude()),
                                            new LatLng(Py, Px)
                                    ));*/

                            //DrawLine();

                        }

                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    class AvailableDrivers extends AsyncTask<String, Void, String> {
        String status = null;
        Activity context;


        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... connUrl) {
            HttpURLConnection conn;
            BufferedReader reader;


            try {
                final URL url = new URL(connUrl[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.addRequestProperty("Content-Type", "application/json");


                JSONObject j = new JSONObject();
                j.put("driver_id",Integer.parseInt(LoginActivity.girenDriver));
                j.put("available",available);



                OutputStream out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                writer.write(j.toString());
                writer.flush();
                writer.close();
                out.close();
                conn.connect();

                int result = conn.getResponseCode();
                if (result == 200) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        status = line;
                    }
                }

            } catch (Exception ex) {

                System.out.print(ex);


            }
            return status;


        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                //Toast.makeText(DriverMapsActivity.this, "Doctor updated successfully ", Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(DriverMapsActivity.this, "Doctor does not update successfully ", Toast.LENGTH_LONG).show();
            }
        }
    }
}

