package com.example.android.capstoneproject1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavanya on 11/25/16.
 */

public class StartersListClass {
    private static final String TAG = StartersListClass.class.getSimpleName();
    public static List<Starterclass> starterclasses = new ArrayList<Starterclass>();

    public int getsize() {
        return starterclasses.size();
    }

    public double getprice() {
        double price = 0;
        for (Starterclass starterclass : starterclasses) {
            price = price + Double.parseDouble(starterclass.getPrices().substring(1));
            Log.d(TAG, "the price is" + price);
        }
        price = Math.floor(price * 100) / 100;
        return price;
    }


}
