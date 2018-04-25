package com.example.melike.uberapp;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.melike.uberapp.BaseActivity.eco;


/**
 * Created by Melike on 19.04.2018.
 */

public class CarsPagerAdapter extends PagerAdapter {

    List<Integer> dataList;
    ImageView imgEco;
    public static TextView ecoPrice;


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
    public Object instantiateItem(ViewGroup container, int position) {

        View view;

        if (dataList.get(position) == 0) {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.uber_economy, container, false);
            imgEco = (ImageView) view.findViewById(R.id.id_uberGo);
            ecoPrice = (TextView) view.findViewById(R.id.txtEcoPrice);


        } else  {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.uber_premium, container, false);
        }

        container.addView(view);


        return view;
    }
}
