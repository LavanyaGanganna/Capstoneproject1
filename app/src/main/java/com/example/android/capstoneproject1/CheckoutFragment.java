package com.example.android.capstoneproject1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lavanya on 11/23/16.
 */

public class CheckoutFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = CheckoutFragment.class.getSimpleName();
    List<String> payments = new ArrayList<String>();

    TextView pricetext, tax, totalcost, display;
    double price = 0;
    int itemcnt = 0;
    String emails;

    public interface Callbacks {
        /*Callback for when an item has been selected. */
        public void onItemSelected(String id);
    }

    Callbacks mcallbacks;
    String ids = "false";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.checkoutfragment, container, false);
        // payments.add("Add Gift Card");
        payments.add("Paypal");
        payments.add("Select Payment Method");
        Bundle bundle = getArguments();
        price = bundle.getDouble("price", 0);
        itemcnt = bundle.getInt("values", 0);
        // emails=getArguments().getStringExtra("emailaddr");
        pricetext = (TextView) view.findViewById(R.id.priceid);
        tax = (TextView) view.findViewById(R.id.taxid);
        totalcost = (TextView) view.findViewById(R.id.totalprice);
        // display= (TextView) getActivity().findViewById(R.id.displayid);
        StartersListClass startersListClass = new StartersListClass();
        pricetext.setText(String.format("$%s", startersListClass.getprice()));
        pricetext.setContentDescription(price + getString(R.string.dollar));
        tax.setText(String.format("$%s", 1.0));
        tax.setContentDescription(getString(R.string.dollarone));
        double money = price + 1.0;
        totalcost.setText(String.format("$%s", (startersListClass.getprice() + 1.0)));
        totalcost.setContentDescription(money + getString(R.string.dollar));
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        HintAdapter adapter = new HintAdapter(getContext(), R.layout.spinner_item, payments);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean paypals = sharedPref.getBoolean("paypal", false);
        if (paypals) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            spinner.setSelection(sharedPreferences.getInt("spinner", adapter.getCount()));

        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcallbacks = (Callbacks) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                ids = "true";
                mcallbacks.onItemSelected(ids);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                sharedPreferences.edit().putInt("spinner", i).apply();
                adapterView.getChildAt(0).setContentDescription("paypal");
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class HintAdapter extends ArrayAdapter<String> {

        public HintAdapter(Context context, int layoutres, List<String> payments) {
            super(context, layoutres, payments);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}
