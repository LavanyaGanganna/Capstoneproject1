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

import static android.content.ContentValues.TAG;

public class WrapDishes extends AppCompatActivity implements StartersAdapter.DataTransferInterface {
    RecyclerView recyclerView;
    int itemcnt = 0;
    double price = 0;
    TextView textView;
    TextView ui_hot;
    Button cartbutton;
    String recvdstring = null;
    ArrayList<Starterclass> wrapslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap_dishes);
        itemcnt = getIntent().getIntExtra("values", 0);
        price = getIntent().getDoubleExtra("price", 0);
        recvdstring = getIntent().getStringExtra("totalcost");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for (int j = 55; j < 59; j++) {
            wrapslist.add(DatabaseList.fullmenulist.get(j));
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclewrap);
        cartbutton = (Button) findViewById(R.id.viewcart);
        textView = (TextView) findViewById(R.id.ordertext);
        StartersListClass startersListClass = new StartersListClass();
        textView.setText(String.format(Locale.ENGLISH, "%d item | $ %.2f", startersListClass.getsize(), startersListClass.getprice()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StartersAdapter startersAdapter = new StartersAdapter(WrapDishes.this, wrapslist, itemcnt, price);
        recyclerView.setAdapter(startersAdapter);
        startersAdapter.notifyDataSetChanged();
        cartbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WrapDishes.this, ViewSummary.class);
                intent.putExtra("values", itemcnt);
                intent.putExtra("price", price);
                intent.putExtra("totalcost", recvdstring);
                startActivityForResult(intent, 5);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), "font/DarkLarch_PERSONAL_USE.ttf");
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle("Wraps");
    }

    @Override
    public void setValues(int values, double price, String sending) {
        itemcnt = values;
        this.price = price;
        recvdstring = sending;
        if (itemcnt != 0) {
            ui_hot.setVisibility(View.VISIBLE);
            ui_hot.setText(Integer.toString(itemcnt));
            ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
        } else {
            ui_hot.setVisibility(View.GONE);
        }
        textView.setText(sending);
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
                Intent intent = new Intent(WrapDishes.this, ViewSummary.class);
                intent.putExtra("totalcost", recvdstring);
                intent.putExtra("price", price);
                intent.putExtra("values", itemcnt);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "inside onActivityresult");
                price = data.getDoubleExtra("price", 0);
                itemcnt = data.getIntExtra("values", 0);
                recvdstring = data.getStringExtra("totalcost");
                if (itemcnt != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(itemcnt));
                    ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
                } else {
                    ui_hot.setVisibility(View.GONE);
                }
                textView.setText(recvdstring);

            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("values", itemcnt);
        returnIntent.putExtra("price", price);
        returnIntent.putExtra("totalcost", recvdstring);
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferences.edit().putBoolean("backpress", true).apply();
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("values", itemcnt);
            returnIntent.putExtra("price", price);
            returnIntent.putExtra("totalcost", recvdstring);
            SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Preferences.edit().putBoolean("backpress", true).apply();
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
