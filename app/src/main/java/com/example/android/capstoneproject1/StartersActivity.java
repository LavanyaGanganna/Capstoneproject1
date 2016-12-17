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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class StartersActivity extends AppCompatActivity implements StartersAdapter.DataTransferInterface {
    RecyclerView recyclerView;
    String recvdstring = null;
    TextView ui_hot;
    int itemcnt = 0;
    double price = 0;
    TextView textView;
    Button cartbutton;
    ArrayList<Starterclass> starterclassArrayList = new ArrayList<Starterclass>();
    StartersAdapter startersAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starters);
        textView = (TextView) findViewById(R.id.ordertext);
        recyclerView = (RecyclerView) findViewById(R.id.starterslist);
        itemcnt = getIntent().getIntExtra(getString(R.string.values), 0);
        price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        recvdstring = getIntent().getStringExtra(getString(R.string.total));
        StartersListClass startersListClass = new StartersListClass();
        if (startersListClass.getsize() > 1) {
            textView.setText(String.format(Locale.ENGLISH, "%d " + getString(R.string.itemss) + "%.2f", startersListClass.getsize(), startersListClass.getprice()));
        } else {
            textView.setText(String.format(Locale.ENGLISH, "%d " + getString(R.string.item) + "%.2f", startersListClass.getsize(), startersListClass.getprice()));
        }
        cartbutton = (Button) findViewById(R.id.viewcart);
        // Log.d(TAG, "oncreate");
        for (int i = 0; i < 17; i++) {
            starterclassArrayList.add(DatabaseList.fullmenulist.get(i));
        }

        startersAdapter = new StartersAdapter(StartersActivity.this, starterclassArrayList, itemcnt, price);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(startersAdapter);
        startersAdapter.notifyDataSetChanged();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(R.string.start);
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        cartbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartersActivity.this, ViewSummary.class);
                intent.putExtra(getString(R.string.values), itemcnt);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.total), recvdstring);
                startActivityForResult(intent, 5);
            }
        });
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
            //ui_hot.setText(Integer.toString(sizes));
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", sizes));
            ui_hot.setContentDescription(Integer.toString(sizes) + getString(R.string.itemselected));
        }

        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartersActivity.this, ViewSummary.class);
                intent.putExtra(getString(R.string.total), recvdstring);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.values), itemcnt);

                startActivity(intent);
            }
        });
        Drawable drawable = menu.getItem(0).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                // Log.d(TAG, "inside onActivityresult");
                price = data.getDoubleExtra(getString(R.string.price), 0);
                itemcnt = data.getIntExtra(getString(R.string.values), 0);
                recvdstring = data.getStringExtra(getString(R.string.total));
                if (itemcnt != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    //   ui_hot.setText(Integer.toString(itemcnt));
                    ui_hot.setText(String.format(Locale.ENGLISH, "%d", itemcnt));
                    ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
                } else {
                    ui_hot.setVisibility(View.GONE);
                }
                textView.setText(recvdstring);

            }
        }
    }

    @Override
    public void setValues(int values, double price, String sending) {
        itemcnt = values;
        this.price = price;
        recvdstring = sending;
        if (itemcnt != 0) {
            ui_hot.setVisibility(View.VISIBLE);
            //  ui_hot.setText(Integer.toString(itemcnt));
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", itemcnt));
            ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
        } else {
            ui_hot.setVisibility(View.GONE);
        }
        textView.setText(sending);

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.values), itemcnt);
        returnIntent.putExtra(getString(R.string.price), price);
        returnIntent.putExtra(getString(R.string.total), recvdstring);
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferences.edit().putBoolean(getString(R.string.backpress), true).apply();
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(getString(R.string.values), itemcnt);
            returnIntent.putExtra(getString(R.string.price), price);
            returnIntent.putExtra(getString(R.string.total), recvdstring);
            SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Preferences.edit().putBoolean(getString(R.string.backpress), true).apply();
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menushare = menu.findItem(R.id.Shareaction);
        menushare.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

}






