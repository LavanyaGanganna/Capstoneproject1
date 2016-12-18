package com.example.android.capstoneproject1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Checkout extends AppCompatActivity implements CheckoutFragment.Callbacks {
    private static final String TAG = Checkout.class.getSimpleName();
    double price = 0;
    int itemcnt = 0;
    String recvdstring;
    TextView display;
    TextView ui_hot;
    FragmentTransaction fragmentTransaction;
    PaypalFragment paypalFragment;
    Intent shareintent;
    public static final String FOOD_SHARE_HASHTAG = "#RedChillies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        itemcnt = getIntent().getIntExtra(getString(R.string.values), 0);
        StartersListClass startersListClass = new StartersListClass();
        itemcnt = startersListClass.getsize();
        price = startersListClass.getprice();
        recvdstring = getIntent().getStringExtra(getString(R.string.total));
        display = (TextView) findViewById(R.id.displayid);
        display.setText(recvdstring);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean paylogin = prefs.getBoolean(getString(R.string.paylogin), false);
        if (paylogin) {
            StartersListClass startersListClasses = new StartersListClass();
            String displays = String.format(Locale.ENGLISH, "%d  %s %.2f", startersListClasses.getsize(), getString(R.string.item), startersListClasses.getprice());
            display.setText(displays);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().putBoolean(getString(R.string.paylogin), false).apply();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(getString(R.string.checkout));
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.price), price);
        bundle.putInt(getString(R.string.values), itemcnt);
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        checkoutFragment.setArguments(bundle);
        paypalFragment = new PaypalFragment();
        paypalFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.checkcontainer, checkoutFragment);

        fragmentTransaction.replace(R.id.paycontainer, paypalFragment);
//        paypalFragment.enablebutton(false);
        fragmentTransaction.commit();

    }

    @Override
    public void onItemSelected(String id) {
        if (id.equals("true")) {
            paypalFragment.enablebutton(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getString(R.string.price), price);
                returnIntent.putExtra(getString(R.string.values), itemcnt);
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
                Preferences.edit().putBoolean(getString(R.string.backpress), true).apply();
                setResult(RESULT_OK, returnIntent);
                finish();
                break;

            case R.id.Shareaction:
                String sharebody = "";
                shareintent = new Intent(android.content.Intent.ACTION_SEND);
                for (int i = 0; i < StartersListClass.starterclasses.size(); i++) {
                    sharebody = sharebody + StartersListClass.starterclasses.get(i).getTitles() + "--" + StartersListClass.starterclasses.get(i).getPrices() + "\n";
                }
                double prices = price + 1.0;
                shareintent.setType(getString(R.string.textplain));
                sharebody = sharebody + "\n" + getString(R.string.subtotal) + price + "\n" + getString(R.string.tax) + getString(R.string.onedollar) + "\n" +
                        getString(R.string.totalprice) + "$" + Double.toString(prices) + "\n";
                sharebody = sharebody + FOOD_SHARE_HASHTAG;
                shareintent.putExtra(Intent.EXTRA_TEXT, sharebody);
                startActivity(Intent.createChooser(shareintent, getString(R.string.sharevia)));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        RelativeLayout menulayout = (RelativeLayout) menu.findItem(R.id.shoppinglist).getActionView();
        ui_hot = (TextView) menulayout.findViewById(R.id.hotlist_hot);

        int sizes = StartersListClass.starterclasses.size();
        if (sizes != 0) {
            ui_hot.setVisibility(View.VISIBLE);
            // ui_hot.setText(Integer.toString(sizes));
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", sizes));
        }
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this, ViewSummary.class);
                intent.putExtra(getString(R.string.total), recvdstring);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.values), itemcnt);

                startActivity(intent);
            }
        });
        MenuItem menuItem = menu.findItem(R.id.Shareaction);
        ShareActionProvider mshareactionprovider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mshareactionprovider != null) {
            mshareactionprovider.setShareIntent(shareintent);
        } else {
            Log.d(TAG, getString(R.string.shareaction));
        }

        Drawable drawable = menu.getItem(0).getIcon();
        Drawable drawable1 = menu.getItem(1).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        return true;
    }


}
