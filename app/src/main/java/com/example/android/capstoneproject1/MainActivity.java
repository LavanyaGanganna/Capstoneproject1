package com.example.android.capstoneproject1;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavanya.myapplication.backend.myApi.MyApi;
import com.example.lavanya.myapplication.backend.usersApi.UsersApi;
import com.example.lavanya.myapplication.backend.usersApi.model.CollectionResponseUsers;
import com.example.lavanya.myapplication.backend.usersApi.model.Users;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;

import data.MenuDbHelper;
import data.MenuTableContract;
import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity {
    private static final String HOMEFRAG = "homefragment";
    private static final String TAG = MainActivity.class.getSimpleName();


    int[] mResources = {
            R.drawable.fishypage,
            R.drawable.keralafryrice,
            R.drawable.puttus,
            R.drawable.dilpasands

    };

    ViewPager mviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(MainActivity.this, null, null, 1);
        try {
            databaseOpenHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new EndpointsAsyncTask().execute(getString(R.string.admin), getString(R.string.passman), null);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(MainActivity.this);
                mviewpager = (ViewPager) findViewById(R.id.pager);
                mviewpager.setAdapter(customPagerAdapter);
                mviewpager.setOffscreenPageLimit(5);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
                tabLayout.setupWithViewPager(mviewpager, true);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.container, homeFragment, HOMEFRAG).commit();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(R.string.redc);
        //	getSupportActionBar().setLogo(R.drawable.homelogomin4);
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }

    }


    public class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview);
            imageView.setImageResource(mResources[position]);
            String description = getdescription(position);
            imageView.setContentDescription(description);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOMEFRAG);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    class EndpointsAsyncTask extends AsyncTask<String, Void, Void> {
        private UsersApi myApiService = null;
        Users retuser;

        @Override
        protected Void doInBackground(String... strings) {
            if (myApiService == null) {  // Only do this once
                UsersApi.Builder builder = new UsersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl(getString(R.string.appspots))
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            String email = strings[0];
            String pswrd = strings[1];
            String phno = strings[2];

            try {

                retuser = myApiService.checkUser(email).execute();

                if (retuser == null) {
                    Users users = new Users();
                    users.setEmail(email);
                    users.setPword(pswrd);
                    users.setPhno(phno);
                    Users returnuser = myApiService.insert(users).execute();
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    private String getdescription(int pos) {
        if (pos == 0) {
            return getString(R.string.keralfryfish);
        } else if (pos == 1) {
            return getString(R.string.fryrice);
        } else if (pos == 2) {
            return getString(R.string.steamputtu);
        } else if (pos == 3) {
            return getString(R.string.dilkush);
        } else
            return null;
    }

}
