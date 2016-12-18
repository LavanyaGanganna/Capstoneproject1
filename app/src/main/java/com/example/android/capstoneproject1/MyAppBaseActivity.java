package com.example.android.capstoneproject1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by lavanya on 12/17/16.
 */

public abstract class MyAppBaseActivity extends AppCompatActivity implements StartersAdapter.DataTransferInterface {
    private static final String TAG = MyAppBaseActivity.class.getSimpleName();
    int itemcnt = 0;
    RecyclerView recyclerView;
    double price = 0;
    String recvdstring = null;
    TextView textView;
    Button cartbutton;
    TextView ui_hot;
    ArrayList<Starterclass> startarraylist = new ArrayList<>();
    private static final int CART_CODE = 5;
    private static final int MENU_BASE_CODE = 9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemcnt = getIntent().getIntExtra(getString(R.string.values), 0);
        price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        StartersListClass startersListClass = new StartersListClass();
        itemcnt = startersListClass.getsize();
        price = startersListClass.getprice();
        recvdstring = getIntent().getStringExtra(getString(R.string.total));


    }

    @Override
    public void onBackPressed() {
        StartersListClass startersListClass = new StartersListClass();
        itemcnt = startersListClass.getsize();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.values), itemcnt);
        returnIntent.putExtra(getString(R.string.price), price);
        returnIntent.putExtra(getString(R.string.total), recvdstring);
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public void setcommonproperties() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        cartbutton = (Button) findViewById(R.id.viewcart);
        textView = (TextView) findViewById(R.id.ordertext);
        StartersListClass startersListClass = new StartersListClass();
        textView.setText(String.format(Locale.ENGLISH, "%d  %s  %.2f", startersListClass.getsize(), getString(R.string.item), startersListClass.getprice()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StartersAdapter startersAdapter = new StartersAdapter(MyAppBaseActivity.this, startarraylist, itemcnt, price);
        recyclerView.setAdapter(startersAdapter);
        startersAdapter.notifyDataSetChanged();
        cartbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAppBaseActivity.this, ViewSummary.class);
                intent.putExtra(getString(R.string.values), itemcnt);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.total), recvdstring);
                startActivityForResult(intent, CART_CODE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);

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
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", sizes));
            ui_hot.setContentDescription(Integer.toString(sizes) + getString(R.string.itemselected));
        }
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAppBaseActivity.this, ViewSummary.class);
                intent.putExtra(getString(R.string.total), recvdstring);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.values), itemcnt);

                startActivityForResult(intent, MENU_BASE_CODE);
            }
        });
        Drawable drawable = menu.getItem(0).getIcon();
        Drawable drawable1 = menu.getItem(0).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CART_CODE || requestCode == MENU_BASE_CODE) {
            if (resultCode == RESULT_OK) {
                price = data.getDoubleExtra(getString(R.string.price), 0);
                itemcnt = data.getIntExtra(getString(R.string.values), 0);
                recvdstring = data.getStringExtra(getString(R.string.total));
                StartersListClass startersListClass = new StartersListClass();
                itemcnt = startersListClass.getsize();
                if (itemcnt != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
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
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", itemcnt));
            ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
        } else {
            ui_hot.setVisibility(View.GONE);
        }
        textView.setText(sending);


    }


}
