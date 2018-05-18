package com.example.melike.uberapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.List;
/**
 * Created by Melike on 19.04.2018.
 */
public class CarsPagerAdapter extends PagerAdapter {
    List<Integer> dataList;
    ImageView imgEco, imgX, imgXL, imgHire;
    public static String secilenDriver;
    public static String ecoDriver, xDriver, xLDriver,hireDriver;
    public static String ecoPhone, xPhone, xLPhone, hirePhone;
    public static String availableDriverGO ="false", availableDriverX, availableDriverXL, availableDriverHIRE ;
    public static  String DriverID;

    Handler mHandler;


    public CarsPagerAdapter(List<Integer> dataList) {
        this.dataList = dataList;


    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        View view;
        if (dataList.get(position) == 0) {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.uber_economy, container, false);

            new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");

            imgEco = (ImageView) view.findViewById(R.id.id_uberGo);
            imgEco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");

                    for(int i=0;i<PassengerMapsActivity.list.size();i++){
                        if(PassengerMapsActivity.list.get(i).carType.equals("UberGo")){
                            secilenDriver = Integer.toString(PassengerMapsActivity.list.get(i).id);
                            ecoDriver=PassengerMapsActivity.list.get(i).name;
                            ecoPhone=PassengerMapsActivity.list.get(i).phoneNumber;
                        }
                    }
                    new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");

                    AlertDialog.Builder alert = new AlertDialog.Builder(container.getContext());
                    alert.setTitle("UberGo");
                    alert.setMessage("Available: "+ availableDriverGO+"\nDriver Name:" + ecoDriver + "\nPhone Number:" + ecoPhone + "\nPrice:" + BaseActivity.eco.substring(0, 5) + " ₺");
                    alert.setCancelable(false);
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CallDrivers().execute("http://192.168.151.160/Service1.svc/CallDriver");
                            // Toast.makeText(container.getContext(), "Aracınız en kısa zamanda size yönledirilecektir",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(availableDriverGO.equals("NO")) {
                        button.setEnabled(false);
                    }


                }
            });
        } else {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.uber_premium, container, false);
            imgX = view.findViewById(R.id.img_uberx);
            imgX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0;i<PassengerMapsActivity.list.size();i++){

                        if(PassengerMapsActivity.list.get(i).carType.equals("UberX")){
                            secilenDriver = Integer.toString(PassengerMapsActivity.list.get(i).id);
                            xDriver=PassengerMapsActivity.list.get(i).name;
                            xPhone=PassengerMapsActivity.list.get(i).phoneNumber;
                        }
                    }
                    new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");


                    AlertDialog.Builder alert = new AlertDialog.Builder(container.getContext());
                    alert.setTitle("UberX");
                    alert.setMessage("Available: "+ availableDriverX+"\nDriver Name:" + xDriver + "\nPhone Number:" + xPhone + "\nPrice:" + BaseActivity.x.substring(0, 5) + " ₺");
                    alert.setCancelable(false);
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CallDrivers().execute("http://192.168.151.160/Service1.svc/CallDriver");

                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(availableDriverX.equals("NO")) {
                        button.setEnabled(false);
                    }
                }
            });
            imgXL = view.findViewById(R.id.img_uberxl);
            imgXL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    for(int i=0;i<PassengerMapsActivity.list.size();i++){

                        if(PassengerMapsActivity.list.get(i).carType.equals("UberXL")){
                            secilenDriver = Integer.toString(PassengerMapsActivity.list.get(i).id);
                            xLDriver=PassengerMapsActivity.list.get(i).name;
                            xLPhone=PassengerMapsActivity.list.get(i).phoneNumber;
                        }
                    }
                    new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");

                    AlertDialog.Builder alert = new AlertDialog.Builder(container.getContext());
                    alert.setTitle("UberXL");
                    alert.setMessage("Available: "+ availableDriverXL+"\nDriver Name:" + xLDriver + "\nPhone Number:" + xLPhone + "\nPrice:" + BaseActivity.xl.substring(0, 5) + " ₺");
                    alert.setCancelable(false);
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CallDrivers().execute("http://192.168.151.160/Service1.svc/CallDriver");
                            // Toast.makeText(container.getContext(), "Aracınız en kısa zamanda size yönledirilecektir",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(availableDriverXL.equals("NO")) {
                        button.setEnabled(false);
                    }
                }
            });

            imgHire = view.findViewById(R.id.img_uberhire);
            imgHire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0;i<PassengerMapsActivity.list.size();i++){

                        if(PassengerMapsActivity.list.get(i).carType.equals("UberHIRE")){
                            secilenDriver = Integer.toString(PassengerMapsActivity.list.get(i).id);
                            hireDriver=PassengerMapsActivity.list.get(i).name;
                            hirePhone=PassengerMapsActivity.list.get(i).phoneNumber;
                        }
                    }
                    new GetAvailableDrivers().execute("http://192.168.151.160/Service1.svc/GetAvailableDrivers");

                    AlertDialog.Builder alert = new AlertDialog.Builder(container.getContext());
                    alert.setTitle("UberHIRE");
                    alert.setMessage("Available: "+ availableDriverHIRE+"\nDriver Name:" + hireDriver + "\nPhone Number:" + hirePhone + "\nPrice:" + BaseActivity.hıre.substring(0, 5) + " ₺");
                    alert.setCancelable(false);
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CallDrivers().execute("http://192.168.151.160/Service1.svc/CallDriver");
                            // Toast.makeText(container.getContext(), "Aracınız en kısa zamanda size yönledirilecektir",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(availableDriverHIRE.equals("NO")) {
                        button.setEnabled(false);
                    }
                }
            });
        }
        container.addView(view);
        return view;
    }








    class GetAvailableDrivers extends AsyncTask<String, Void, String> {
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
                         String id = jsonArray.getJSONObject(i).getString("driver_id").trim();
                         String available = jsonArray.getJSONObject(i).getString("available").trim();

                         if (id.equals("7")){
                             availableDriverGO=available;
                         }  if (id.equals("8")){
                            availableDriverX=available;
                        }  if (id.equals("9")){
                            availableDriverXL=available;
                        }  if (id.equals("10")){
                            availableDriverHIRE=available;
                        }



                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class CallDrivers extends AsyncTask<String, Void, String> {
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
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setChunkedStreamingMode(0);
                conn.addRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");

                JSONObject j = new JSONObject();
                j.put("passenger_id", LoginActivity.girenPassenger);
                j.put("driver_id", secilenDriver);
                j.put("passenger_lat", String.valueOf(BaseActivity.userLocation.getLatitude()));
                j.put("passenger_lang", String.valueOf(BaseActivity.userLocation.getLongitude()));
                j.put("destination_lat", String.valueOf(BaseActivity.destination.latitude));
                j.put("destionation_lang", String.valueOf(BaseActivity.destination.longitude));


                OutputStream out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
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


        }
    }
}