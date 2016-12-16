package com.example.android.capstoneproject1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class OurMenu extends AppCompatActivity {
    private static final String TAG = OurMenu.class.getSimpleName();
    ListView listView;
    ArrayList<MenuList> Menus = new ArrayList<MenuList>();
    TextView ui_hot;
    int itemcnt = 0;
    String rcvdstring;
    double price;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_menu);
        setProgressBarIndeterminateVisibility(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdView = (AdView) findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("4867513FE210DD1D74E0FF3EA301E4C0")
                        .build();
                mAdView.loadAd(adRequest);
            }
        });

        itemcnt = getIntent().getIntExtra("values", 0);
        price = getIntent().getDoubleExtra("price", 0);
        rcvdstring = getIntent().getStringExtra("totalcost");
        listView = (ListView) findViewById(R.id.menulist);
        Menus.add(new MenuList(R.drawable.starter, "Starters"));
        Menus.add(new MenuList(R.drawable.soupsalad, "Soups And Salads"));
        Menus.add(new MenuList(R.drawable.veggi, "Vegetarian Specialties"));
        Menus.add(new MenuList(R.drawable.fishes, "Fish Dishes"));
        Menus.add(new MenuList(R.drawable.chickenleg, "Birds Nest"));
        Menus.add(new MenuList(R.drawable.meatloaf, "Meat Specialties"));
        Menus.add(new MenuList(R.drawable.biryanis, "Biryani Corner"));
        Menus.add(new MenuList(R.drawable.wrapss, "Wrap House"));
        Menus.add(new MenuList(R.drawable.wheatss, "Breads & Sides"));
        Menus.add(new MenuList(R.drawable.icecream, "Desserts"));
        Menus.add(new MenuList(R.drawable.drin, "Beverages"));
        MenuArray menuArray = new MenuArray(getApplicationContext(), Menus);
        listView.setAdapter(menuArray);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(OurMenu.this, StartersActivity.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 1);
                }
                if (i == 1) {
                    Intent intent = new Intent(OurMenu.this, SoupsActivity.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 2);
                }
                if (i == 2) {
                    Intent intent = new Intent(OurMenu.this, Vegetarian.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 3);
                }
                if (i == 3) {
                    Intent intent = new Intent(OurMenu.this, FishDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 4);
                }
                if (i == 4) {
                    Intent intent = new Intent(OurMenu.this, BirdsNest.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 5);
                }

                if (i == 5) {
                    Intent intent = new Intent(OurMenu.this, MeatDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 6);
                }
                if (i == 6) {
                    Intent intent = new Intent(OurMenu.this, Biryani.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 7);
                }

                if (i == 7) {
                    Intent intent = new Intent(OurMenu.this, WrapDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 8);
                }

                if (i == 8) {
                    Intent intent = new Intent(OurMenu.this, BreadsDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 9);
                }
                if (i == 9) {
                    Intent intent = new Intent(OurMenu.this, DessertsDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 10);
                }

                if (i == 10) {
                    Intent intent = new Intent(OurMenu.this, BeveragesDishes.class);
                    intent.putExtra("values", itemcnt);
                    intent.putExtra("price", price);
                    Log.d(TAG, "going price" + price);
                    intent.putExtra("totalcost", rcvdstring);
                    startActivityForResult(intent, 11);
                }

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), "font/DarkLarch_PERSONAL_USE.ttf");
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle("Menu");
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2 || requestCode == 3 || requestCode == 4 || requestCode == 5
                || requestCode == 6 || requestCode == 7 || requestCode == 8 || requestCode == 9 ||
                requestCode == 10 || requestCode == 11) {
            if (resultCode == RESULT_OK) {
                int value = data.getIntExtra("values", 0);
                rcvdstring = data.getStringExtra("totalcost");
                price = data.getDoubleExtra("price", 0);
                Log.d(TAG, "the price is" + price);
                itemcnt = value;
                if (value != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(value));
                    ui_hot.setContentDescription(Integer.toString(value) + getString(R.string.itemselected));
                }
            }
        }

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
            ui_hot.setText(Integer.toString(sizes));
            ui_hot.setContentDescription(Integer.toString(sizes) + getString(R.string.itemselected));
        }
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OurMenu.this, ViewSummary.class);
                intent.putExtra("price", price);
                intent.putExtra("totalcost", rcvdstring);
                startActivity(intent);
            }
        });
        Drawable drawable = menu.getItem(0).getIcon();
        Drawable drawable1 = menu.getItem(0).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("values", itemcnt);
        returnIntent.putExtra("price", price);
        returnIntent.putExtra("totalcost", rcvdstring);
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferences.edit().putBoolean("backpress", true).apply();
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("values", itemcnt);
                returnIntent.putExtra("price", price);
                returnIntent.putExtra("totalcost", rcvdstring);
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
                Preferences.edit().putBoolean("backpress", true).apply();
                setResult(RESULT_OK, returnIntent);
                finish();
                break;


        }
        return true;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menushare = menu.findItem(R.id.Shareaction);
        menushare.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
