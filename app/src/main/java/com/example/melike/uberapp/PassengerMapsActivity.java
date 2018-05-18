package com.example.melike.uberapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PassengerMapsActivity  extends BaseActivity {

    public static List<Driver> list = new ArrayList<Driver>();

    @BindView(R.id.rootFrame)
    FrameLayout rootFrame;

    @BindView(R.id.rootll)
    LinearLayout rootll;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.rlwhere)
    RelativeLayout rlWhere;

    @BindView(R.id.ivHome)
    ImageView ivHome;

    @BindView(R.id.tvWhereTo)
    TextView tvWhereto;

    ArgbEvaluator argbEvaluator;

    private LatLng destination;


    public static ImageView ivHome2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_maps);

        ivHome2=findViewById(R.id.ivHome);



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

        new GetDrivers().execute("http://192.168.151.160/Service1.svc/GetDrivers");

        //new GetDrivers().execute("http://192.168.1.188/Service1.svc/GetDrivers");

        setUpPagerAdapter();
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(-devWidth / 2);

        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setPageTransformer(true, pageTransformer);

        ivHome2.setEnabled(false);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(x,y)).title(cartype).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

    }


    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {


            if (position < -1) { // [-Infinity,-1)


            } else if (position <= 1) { // [-1,1]

                if (position >= -1 && position < 0) {

                    LinearLayout uberEco = (LinearLayout) page.findViewById(R.id.lluberEconomy);
                    TextView uberEcoTv = (TextView) page.findViewById(R.id.tvuberEconomy);

                    if (uberEco != null && uberEcoTv != null) {

                        uberEcoTv.setTextColor((Integer) argbEvaluator.evaluate(-2 * position, getResources().getColor(R.color.black)
                                , getResources().getColor(R.color.grey)));

                        uberEcoTv.setTextSize(16 + 4 * position);
                        uberEco.setX((page.getWidth() * position));

                    }

                } else if (position >= 0 && position <= 1) {

                    TextView uberPreTv = (TextView) page.findViewById(R.id.tvuberPre);
                    LinearLayout uberPre = (LinearLayout) page.findViewById(R.id.llUberPre);

                    if (uberPreTv != null && uberPre != null) {

                        uberPreTv.setTextColor((Integer) new ArgbEvaluator().evaluate((1 - position), getResources().getColor(R.color.grey)
                                , getResources().getColor(R.color.black)));

                        uberPreTv.setTextSize(12 + 4 * (1 - position));
                        uberPre.setX(uberPre.getLeft() + (page.getWidth() * (position)));


                    }


                }

            }
        }
    };


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @OnClick(R.id.ivHome)
    void showViewPagerWithTransition() {

        TransitionManager.beginDelayedTransition(rootFrame);
        viewPager.setVisibility(View.VISIBLE);
        ivHome.setVisibility(View.INVISIBLE);
        rlWhere.setVisibility(View.INVISIBLE);

        mMap.setPadding(0, 0, 0, viewPager.getHeight());



    }


    @OnClick(R.id.rlwhere)
    void openPlacesView() {
        openPlaceAutoCompleteView();

    }


    void startRevealAnimation() {

        int cx = rootFrame.getMeasuredWidth() / 2;
        int cy = rootFrame.getMeasuredHeight() / 2;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(rootll, cx, cy, 50, rootFrame.getWidth());

        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator(2));
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                rlWhere.setVisibility(View.VISIBLE);
                ivHome.setVisibility(View.VISIBLE);
            }
        });

        anim.start();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        startRevealAnimation();


    }
    @Override
    protected void setUpPolyLine() {

        LatLng source = new LatLng(getUserLocation().getLatitude(), getUserLocation().getLongitude());
        LatLng destination = getDestinationLatLong();
        if (source != null && destination != null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            getPolyline polyline = retrofit.create(getPolyline.class);

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

    private void setUpPagerAdapter() {

        List<Integer> data = Arrays.asList(0, 1);
        CarsPagerAdapter adapter = new CarsPagerAdapter(data);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getVisibility() == View.VISIBLE) {

            TransitionManager.beginDelayedTransition(rootFrame);
            viewPager.setVisibility(View.INVISIBLE);
            mMap.setPadding(0, 0, 0, 0);
            ivHome.setVisibility(View.VISIBLE);
            rlWhere.setVisibility(View.VISIBLE);

            return;
        }

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    /*public static void sendNotification(View view) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);



        Intent intent = new Intent(this,DriverMapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.car);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello World!");

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
*/


    class GetDrivers extends AsyncTask<String, Void, String> {
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
                        String id = jsonArray.getJSONObject(i).getString("driverId").trim();
                        String lat = jsonArray.getJSONObject(i).getString("Latitude").trim();
                        String lang = jsonArray.getJSONObject(i).getString("Longitude").trim();
                        String cartype = jsonArray.getJSONObject(i).getString("CarType").trim();
                        String price = jsonArray.getJSONObject(i).getString("Price").trim();
                        String name= jsonArray.getJSONObject(i).getString("Name").trim();
                        String phoneNumber= jsonArray.getJSONObject(i).getString("PhoneNumber").trim();


                        Double x= new Double(lat);
                        Double y= new Double(lang);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(x,y)).title(cartype).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                        //Driver d= new Driver(Integer.parseInt(id),name,lat,lang,cartype,price,phoneNumber);
                       // list.add(d);
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }


}

class Driver{
    int id;
    String name;
    String lat;
    String lang;
    String carType;
    String price;
    String phoneNumber;

    /*public Driver(int id, String name, String lat, String lang, String carType, String price,String phoneNumber){
        id=this.id;
        name=this.name;
        lat=this.lat;
        lang=this.lang;
        carType=this.carType;
        price=this.price;
        phoneNumber=this.phoneNumber;*/

    }
