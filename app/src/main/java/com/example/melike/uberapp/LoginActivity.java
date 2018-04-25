package com.example.melike.uberapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signIn;
    TextView signUp;
    String Email, Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        signIn = (Button) findViewById(R.id.btnLogin);
        signUp = (TextView) findViewById(R.id.tvSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);


            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Pwd = password.getText().toString();

                if (MainActivity.isPassenger == true) {

                    new UserLogin().execute("http://192.168.151.160/Service1.svc/LoginGetPassenger");

                    //new UserLogin().execute("http://192.168.1.188/Service1.svc/LoginGetPassenger");

                } else {
                    new UserLogin().execute("http://192.168.151.160/Service1.svc/LoginGetDriver");

                    //new UserLogin().execute("http://192.168.1.188/Service1.svc/LoginGetDriver");
                }

            }
        });
    }

    class UserLogin extends AsyncTask<String, Void, String> {
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
                        String pwd = jsonArray.getJSONObject(i).getString("Password").trim();
                        String name = jsonArray.getJSONObject(i).getString("FullName").trim();
                        String email = jsonArray.getJSONObject(i).getString("Email").trim();
                        String phoneNum = jsonArray.getJSONObject(i).getString("PhoneNumber").trim();
                        String userType = jsonArray.getJSONObject(i).getString("UserType").trim();


                        if (Email.trim().equals(email) && Pwd.trim().equals(pwd) && userType.toLowerCase().equals("passenger")) {
                            Intent intent = new Intent(LoginActivity.this, PassengerMapsActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Passenger Successful login", Toast.LENGTH_LONG).show();
                            break;
                        } else if(Email.trim().equals(email) && Pwd.trim().equals(pwd) && userType.toLowerCase().equals("driver")) {
                            Intent intent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Driver Successful login", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
