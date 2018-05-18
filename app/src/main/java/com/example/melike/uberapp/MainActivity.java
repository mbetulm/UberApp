package com.example.melike.uberapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button passenger, driver;
    public static boolean isPassenger=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passenger =(Button) findViewById(R.id.btnPassenger);
        driver=(Button) findViewById(R.id.btnDriver);

        passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPassenger=true;
                Intent i= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPassenger=false;
                Intent i= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });
    }
}
