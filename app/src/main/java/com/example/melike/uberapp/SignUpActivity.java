package com.example.melike.uberapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {

    EditText name,pwd,phoneNum,email;
    Button singUp;
    RadioGroup userType;
    String Name, Pwd,Phone,Email,UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=(EditText) findViewById(R.id.Name);
        pwd= findViewById(R.id.Password);
        phoneNum=findViewById(R.id.Phone);
        email=findViewById(R.id.Email);
        singUp=findViewById(R.id.btnRegister);
        userType =findViewById(R.id.radioUserType);


        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name=name.getText().toString();
                Pwd=pwd.getText().toString();
                Phone=phoneNum.getText().toString();
                Email=email.getText().toString();

                int checked = userType.getCheckedRadioButtonId();
                switch (checked) {
                    case R.id.radio_passenger: {
                        UserType = "Passenger";
                        break;
                    }
                    case R.id.radio_driver: {
                        UserType = "Driver";
                        break;
                    }
                }
                new UserRegister().execute("http://192.168.151.160/Service1.svc/UserRegister");

               // new UserRegister().execute("http://192.168.1.188/Service1.svc/UserRegister");

            }
        });

        }

    class UserRegister extends AsyncTask<String, Void, String> {
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
                j.put("FullName",Name);
                j.put("Password",Pwd);
                j.put("PhoneNumber",Phone);
                j.put("Email",Email);
                j.put("UserType",UserType);


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
                Toast.makeText(SignUpActivity.this, " save successfully ", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(SignUpActivity.this, " does not save successfully ", Toast.LENGTH_LONG).show();
            }
        }
    }
    }
