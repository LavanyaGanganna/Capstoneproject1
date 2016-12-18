package com.example.android.capstoneproject1;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager;

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
import java.util.Locale;

import data.MenuTableContract;

import static android.content.ContentValues.TAG;

public class OurMenu extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = OurMenu.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    ListView listView;
    ArrayList<MenuList> Menus = new ArrayList<MenuList>();
    TextView ui_hot;
    int itemcnt = 0;
    String rcvdstring;
    double price;
    AdView mAdView;
    private static final int STARTERS_REQUEST_CODE = 1;
    private static final int SOUPS_REQUEST_CODE = 2;
    private static final int VEGGIES_REQUEST_CODE = 3;
    private static final int FISH_REQUEST_CODE = 4;
    private static final int BIRDS_REQUEST_CODE = 5;
    private static final int MEAT_REQUEST_CODE = 6;
    private static final int BIRYANI_REQUEST_CODE = 7;
    private static final int WRAP_REQUEST_CODE = 8;
    private static final int BREAD_REQUEST_CODE = 9;
    private static final int DESSERT_REQUEST_CODE = 10;
    private static final int BEVERAGE_REQUEST_CODE = 11;
    private static final int MENU_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_menu);
        itemcnt = getIntent().getIntExtra(getString(R.string.values), 0);
        price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        rcvdstring = getIntent().getStringExtra(getString(R.string.total));
        StartersListClass startersListClass = new StartersListClass();
        itemcnt = startersListClass.getsize();
        price = startersListClass.getprice();
        setProgressBarIndeterminateVisibility(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdView = (AdView) findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(getString(R.string.testdeviceno))
                        .build();
                mAdView.loadAd(adRequest);
            }
        });


        listView = (ListView) findViewById(R.id.menulist);
        Menus.add(new MenuList(R.drawable.starter, getString(R.string.starterss)));
        Menus.add(new MenuList(R.drawable.soupsalad, getString(R.string.soupss)));
        Menus.add(new MenuList(R.drawable.veggi, getString(R.string.vegies)));
        Menus.add(new MenuList(R.drawable.fishes, getString(R.string.fishdish)));
        Menus.add(new MenuList(R.drawable.chickenleg, getString(R.string.birdsnest)));
        Menus.add(new MenuList(R.drawable.meatloaf, getString(R.string.meatspecial)));
        Menus.add(new MenuList(R.drawable.biryanis, getString(R.string.biryanicor)));
        Menus.add(new MenuList(R.drawable.wrapss, getString(R.string.wraphous)));
        Menus.add(new MenuList(R.drawable.wheatss, getString(R.string.breadssides)));
        Menus.add(new MenuList(R.drawable.icecream, getString(R.string.desert)));
        Menus.add(new MenuList(R.drawable.drin, getString(R.string.beveragess)));
        MenuArray menuArray = new MenuArray(getApplicationContext(), Menus);
        listView.setAdapter(menuArray);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    startSubmenuActivityForResult(StartersActivity.class, STARTERS_REQUEST_CODE);
                }
                if (i == 1) {
                    startSubmenuActivityForResult(SoupsActivity.class, SOUPS_REQUEST_CODE);
                }
                if (i == 2) {
                    startSubmenuActivityForResult(Vegetarian.class, VEGGIES_REQUEST_CODE);
                }
                if (i == 3) {
                    startSubmenuActivityForResult(FishDishes.class, FISH_REQUEST_CODE);
                }
                if (i == 4) {
                    startSubmenuActivityForResult(BirdsNest.class, BIRDS_REQUEST_CODE);
                }

                if (i == 5) {
                    startSubmenuActivityForResult(MeatDishes.class, MEAT_REQUEST_CODE);
                }
                if (i == 6) {
                    startSubmenuActivityForResult(Biryani.class, BIRYANI_REQUEST_CODE);
                }

                if (i == 7) {
                    startSubmenuActivityForResult(WrapDishes.class, WRAP_REQUEST_CODE);
                }

                if (i == 8) {
                    startSubmenuActivityForResult(BreadsDishes.class, BREAD_REQUEST_CODE);

                }
                if (i == 9) {
                    startSubmenuActivityForResult(DessertsDishes.class, DESSERT_REQUEST_CODE);
                }

                if (i == 10) {
                    startSubmenuActivityForResult(BeveragesDishes.class, BEVERAGE_REQUEST_CODE);
                }

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(R.string.menuss);
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
    }

    private void startSubmenuActivityForResult(Class type, int requestCode) {
        Intent intent = new Intent(this, type);
        intent.putExtra(getString(R.string.values), itemcnt);
        intent.putExtra(getString(R.string.price), price);
        intent.putExtra(getString(R.string.total), rcvdstring);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STARTERS_REQUEST_CODE || requestCode == SOUPS_REQUEST_CODE || requestCode == VEGGIES_REQUEST_CODE || requestCode == FISH_REQUEST_CODE || requestCode == BIRDS_REQUEST_CODE
                || requestCode == MEAT_REQUEST_CODE || requestCode == BIRYANI_REQUEST_CODE || requestCode == WRAP_REQUEST_CODE || requestCode == BREAD_REQUEST_CODE ||
                requestCode == DESSERT_REQUEST_CODE || requestCode == BEVERAGE_REQUEST_CODE || requestCode == MENU_CODE) {
            if (resultCode == RESULT_OK) {
                int value = data.getIntExtra(getString(R.string.values), 0);
                rcvdstring = data.getStringExtra(getString(R.string.total));
                price = data.getDoubleExtra(getString(R.string.price), 0);
                //   Log.d(TAG, "the price is" + price);
                itemcnt = value;
                StartersListClass startersListClass = new StartersListClass();
                itemcnt = startersListClass.getsize();
                if (itemcnt != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    // ui_hot.setText(Integer.toString(value));
                    ui_hot.setText(String.format(Locale.ENGLISH, "%d", itemcnt));
                    ui_hot.setContentDescription(Integer.toString(itemcnt) + getString(R.string.itemselected));
                }
                else{
                    ui_hot.setVisibility(View.GONE);
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
            // ui_hot.setText(Integer.toString(sizes));
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", sizes));
            ui_hot.setContentDescription(Integer.toString(sizes) + getString(R.string.itemselected));
        }
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OurMenu.this, ViewSummary.class);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.total), rcvdstring);
                startActivityForResult(intent, MENU_CODE);
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
        StartersListClass startersListClass = new StartersListClass();
        itemcnt = startersListClass.getsize();
        price = startersListClass.getprice();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.values), itemcnt);
        returnIntent.putExtra(getString(R.string.price), price);
        returnIntent.putExtra(getString(R.string.total), rcvdstring);
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Preferences.edit().putBoolean(getString(R.string.backpress), true).apply();
        setResult(RESULT_OK, returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                StartersListClass startersListClass = new StartersListClass();
                itemcnt = startersListClass.getsize();
                price = startersListClass.getprice();
                returnIntent.putExtra(getString(R.string.values), itemcnt);
                returnIntent.putExtra(getString(R.string.price), price);
                returnIntent.putExtra(getString(R.string.total), rcvdstring);
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
                Preferences.edit().putBoolean(getString(R.string.backpress), true).apply();
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


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getApplicationContext(), MenuTableContract.MenuEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int imageno;
        String titletext;
        String pricetext;
        int images;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                imageno = cursor.getInt(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FIRST_VAL));
                titletext = cursor.getString(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FOOD_TITLE));
                pricetext = cursor.getString(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FOOD_PRICE));
                images = getimage(imageno);
                Starterclass starterclass = new Starterclass(images, titletext, pricetext);
                DatabaseList.fullmenulist.add(starterclass);

            } while (cursor.moveToNext());

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private int getimage(int imageno) {
        int images = 0;
        switch (imageno) {
            case 300:
                images = R.drawable.vsoup;
                break;
            case 301:
                images = R.drawable.corchicksoup;
                break;
            case 302:
                images = R.drawable.greensald;
                break;
            case 200:
                images = R.drawable.vcutlets;
                break;
            case 201:
                images = R.drawable.mushroom;
                break;
            case 202:
                images = R.drawable.gobis;
                break;
            case 203:
                images = R.drawable.gobichill;
                break;
            case 204:
                images = R.drawable.vegpakora;
                break;
            case 205:
                images = R.drawable.corn;
                break;
            case 206:
                images = R.drawable.chillp;
                break;
            case 207:
                images = R.drawable.fcutlet;
                break;
            case 208:
                images = R.drawable.natholi;
                break;
            case 209:
                images = R.drawable.fpakora;
                break;
            case 210:
                images = R.drawable.gshrimp;
                break;
            case 211:
                images = R.drawable.chemeen;
                break;
            case 212:
                images = R.drawable.travancore;
                break;
            case 213:
                images = R.drawable.chillchick;
                break;
            case 214:
                images = R.drawable.chick65;
                break;
            case 215:
                images = R.drawable.chicktick;
                break;
            case 216:
                images = R.drawable.beefcut;
                break;
            case 303:
                images = R.drawable.greenpeass;
                break;
            case 304:
                images = R.drawable.vegstews;
                break;
            case 305:
                images = R.drawable.kadalas;
                break;
            case 306:
                images = R.drawable.vegtheeyals;
                break;
            case 307:
                images = R.drawable.avials;
                break;
            case 308:
                images = R.drawable.vegmappass;
                break;
            case 309:
                images = R.drawable.morcurries;
                break;
            case 400:
                images = R.drawable.fmolees;
                break;
            case 401:
                images = R.drawable.fishporicha;
                break;
            case 402:
                images = R.drawable.grillfishfr;
                break;
            case 403:
                images = R.drawable.fishporicha;
                break;
            case 404:
                images = R.drawable.fishcurry;
                break;
            case 405:
                images = R.drawable.chemmeenfry;
                break;
            case 406:
                images = R.drawable.crabfry;
                break;
            case 500:
                images = R.drawable.porichas;
                break;
            case 501:
                images = R.drawable.chickmappas;
                break;
            case 502:
                images = R.drawable.chickulli;
                break;
            case 503:
                images = R.drawable.chickrost;
                break;
            case 504:
                images = R.drawable.chickpepr;
                break;
            case 505:
                images = R.drawable.chicktik;
                break;
            case 506:
                images = R.drawable.eggmasal;
                break;
            case 507:
                images = R.drawable.omlette;
                break;
            case 600:
                images = R.drawable.goatcurr;
                break;
            case 601:
                images = R.drawable.goatrogans;
                break;
            case 602:
                images = R.drawable.goatpeprs;
                break;
            case 603:
                images = R.drawable.beeffri;
                break;
            case 604:
                images = R.drawable.beefullis;
                break;
            case 605:
                images = R.drawable.beefcurs;
                break;
            case 606:
                images = R.drawable.beefchil;
                break;
            case 700:
                images = R.drawable.malbrchickbr;
                break;
            case 701:
                images = R.drawable.chickdumsbir;
                break;
            case 702:
                images = R.drawable.muttonbir;
                break;
            case 703:
                images = R.drawable.eggbir;
                break;
            case 704:
                images = R.drawable.vegbiry;
                break;
            case 705:
                images = R.drawable.gheerce;
                break;
            case 706:
                images = R.drawable.vegfryrice;
                break;
            case 707:
                images = R.drawable.chickfryrce;
                break;
            case 708:
                images = R.drawable.vegwrap;
                break;
            case 709:
                images = R.drawable.gobiwraps;
                break;
            case 710:
                images = R.drawable.chicktickwrap;
                break;
            case 711:
                images = R.drawable.beefwraps;
                break;
            case 800:
                images = R.drawable.rotis;
                break;
            case 801:
                images = R.drawable.garlicsnaan;
                break;
            case 802:
                images = R.drawable.chapatis;
                break;
            case 803:
                images = R.drawable.eggchillparatha;
                break;
            case 804:
                images = R.drawable.appams;
                break;
            case 805:
                images = R.drawable.puttuus;
                break;
            case 806:
                images = R.drawable.iddipaam;
                break;
            case 807:
                images = R.drawable.kappaa;
                break;
            case 808:
                images = R.drawable.fruitbowls;
                break;
            case 809:
                images = R.drawable.icecreams;
                break;
            case 810:
                images = R.drawable.faludas;
                break;
            case 811:
                images = R.drawable.gulbjamun;
                break;
            case 812:
                images = R.drawable.paysampradhan;
                break;
            case 900:
                images = R.drawable.softdrinksres;
                break;
            case 901:
                images = R.drawable.buttermlk;
                break;
            case 902:
                images = R.drawable.plinlassi;
                break;
            case 903:
                images = R.drawable.mangolassis;
                break;
            case 904:
                images = R.drawable.limesoda;
                break;
            case 905:
                images = R.drawable.orangejuce;
                break;
            case 906:
                images = R.drawable.teas;
                break;
            case 907:
                images = R.drawable.coffeess;
                break;
        }
        return images;
    }
}
