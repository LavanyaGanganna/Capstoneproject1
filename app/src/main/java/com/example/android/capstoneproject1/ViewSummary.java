package com.example.android.capstoneproject1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;

public class ViewSummary extends AppCompatActivity implements SummaryAdapter.SummaryInterface {
    private static final String TAG = ViewSummary.class.getSimpleName();
    RecyclerView recyclerView;
    TextView textView;
    Button button;
    Button removeall, addmore;
    Intent shareintent;
    SummaryAdapter summaryAdapter = null;
    int itemcnt = 0;
    double price = 0;
    String recvdstring = null;
    public static final String FOOD_SHARE_HASHTAG = "#RedChillies";
    TextView ui_hot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);
        itemcnt = getIntent().getIntExtra("values", 0);
        price = getIntent().getDoubleExtra("price", 0);
        recvdstring = getIntent().getStringExtra("totalcost");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.totaltext);
        button = (Button) findViewById(R.id.checkout);
        removeall = (Button) findViewById(R.id.removeall);
        addmore = (Button) findViewById(R.id.addmore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), "font/DarkLarch_PERSONAL_USE.ttf");
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle("Summary");
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StartersListClass startersListClass = new StartersListClass();
        summaryAdapter = new SummaryAdapter(ViewSummary.this, StartersListClass.starterclasses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(summaryAdapter);
        itemcnt = startersListClass.getsize();
        price = startersListClass.getprice();
        if (startersListClass.getsize() > 1) {
            textView.setText(String.format(Locale.ENGLISH, "%d items | $ %.2f", startersListClass.getsize(), startersListClass.getprice()));
            recvdstring = String.format(Locale.ENGLISH, "%d items | $ %.2f", startersListClass.getsize(), startersListClass.getprice());
        } else {
            textView.setText(String.format(Locale.ENGLISH, "%d item | $ %.2f", startersListClass.getsize(), startersListClass.getprice()));
            recvdstring = String.format(Locale.ENGLISH, "%d item | $ %.2f", startersListClass.getsize(), startersListClass.getprice());
        }
        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSummary.this, OurMenu.class);
                startActivity(intent);
            }
        });
        removeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartersListClass.starterclasses.clear();
                summaryAdapter.notifyDataSetChanged();
                Intent intent = new Intent(ViewSummary.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSummary.this, Checkout.class);
                intent.putExtra("price", price);
                intent.putExtra("values", itemcnt);
                intent.putExtra("totalcost", recvdstring);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("price", price);
        returnIntent.putExtra("values", itemcnt);
        returnIntent.putExtra("totalcost", recvdstring);
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferences.edit().putBoolean("backpress", true).apply();
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void showValues(int values, double price, String sending) {
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
        MenuItem menuItem = menu.findItem(R.id.Shareaction);
        ShareActionProvider mshareactionprovider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mshareactionprovider != null) {
            mshareactionprovider.setShareIntent(shareintent);
        } else {
            Log.d(TAG, "share action provider is null");
        }
        Drawable drawable = menu.getItem(0).getIcon();
        Drawable drawable1 = menu.getItem(1).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("price", price);
                returnIntent.putExtra("values", itemcnt);
                returnIntent.putExtra("totalcost", recvdstring);
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
                Preferences.edit().putBoolean("backpress", true).apply();
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.Shareaction:
                String sharebody = "";
                shareintent = new Intent(android.content.Intent.ACTION_SEND);
                for (int i = 0; i < StartersListClass.starterclasses.size(); i++) {
                    sharebody = sharebody + StartersListClass.starterclasses.get(i).getTitles() + "--" + StartersListClass.starterclasses.get(i).getPrices() + "\n";
                }
                shareintent.setType("text/plain");
                sharebody = sharebody + FOOD_SHARE_HASHTAG;
                shareintent.putExtra(Intent.EXTRA_TEXT, sharebody);
                startActivity(Intent.createChooser(shareintent, "share via"));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}



