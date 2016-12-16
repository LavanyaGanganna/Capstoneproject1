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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

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
        price = getIntent().getDoubleExtra("price", 0);
        itemcnt = getIntent().getIntExtra("values", 0);
        recvdstring = getIntent().getStringExtra("totalcost");
        display = (TextView) findViewById(R.id.displayid);
        // display.setText(String.format(Locale.ENGLISH,"%1$d items | $ %2$," +
        //                       ".2f",itemcnt,price));
        display.setText(recvdstring);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean paylogin = prefs.getBoolean("paylogin", false);
        if (paylogin) {
            StartersListClass startersListClass = new StartersListClass();
            String displays = String.format(Locale.ENGLISH, "%d items | $ %.2f", startersListClass.getsize(), startersListClass.getprice());
            display.setText(displays);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().putBoolean("paylogin", false).apply();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), "font/DarkLarch_PERSONAL_USE.ttf");
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle("Checkout");
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putDouble("price", price);
        bundle.putInt("values", itemcnt);
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
                returnIntent.putExtra("price", price);
                returnIntent.putExtra("values", itemcnt);
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
                double prices = price + 1.0;
                shareintent.setType("text/plain");
                sharebody = sharebody + "\n" + "Subtotal:" + price + "\n" + "Tax:" + "$1.00" + "\n" +
                        "Total price: " + "$" + Double.toString(prices) + "\n";
                sharebody = sharebody + FOOD_SHARE_HASHTAG;
                shareintent.putExtra(Intent.EXTRA_TEXT, sharebody);
                startActivity(Intent.createChooser(shareintent, "share via"));
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
            ui_hot.setText(Integer.toString(sizes));
        }
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this, ViewSummary.class);
                intent.putExtra("totalcost", recvdstring);
                intent.putExtra("price", price);
                intent.putExtra("values", itemcnt);

                startActivity(intent);
            }
        });
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


}
