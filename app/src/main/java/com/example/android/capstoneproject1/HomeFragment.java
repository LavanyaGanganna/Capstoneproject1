package com.example.android.capstoneproject1;

import android.app.Activity;
import android.support.v4.app.LoaderManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import data.MenuTableContract;

import static android.content.ContentValues.TAG;

/**
 * Created by lavanya on 11/7/16.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ArrayList<NavItems> navItems = new ArrayList<>();

    ListView lists;
    Button signbutton;
    Button signupbutton;
    TextView ui_hot;
    int itemcnt = 0;
    double price = 0;
    String rcvdstring = null;
    ArrayList<Starterclass> selectedlist = null;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lists = (ListView) view.findViewById(R.id.lists);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle(getString(R.string.bakingappam));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean bigtabletsize = getResources().getBoolean(R.bool.isbigTablet);
                if (bigtabletsize) {
                    navItems.add(new NavItems(getString(R.string.orders), R.drawable.waiterbig));
                    navItems.add(new NavItems(getString(R.string.locations), R.drawable.markerbig));
                    navItems.add(new NavItems(getString(R.string.ourmenus), R.drawable.restaurantbig));
                    navItems.add(new NavItems(getString(R.string.contacts), R.drawable.phonebig));
                } else {
                    navItems.add(new NavItems(getString(R.string.orders), R.drawable.waiter));
                    navItems.add(new NavItems(getString(R.string.locations), R.drawable.marker));
                    navItems.add(new NavItems(getString(R.string.ourmenus), R.drawable.rstaurant));
                    navItems.add(new NavItems(getString(R.string.contacts), R.drawable.phone));
                }
                DrawerAdapter drawerAdapter = new DrawerAdapter(getContext(), navItems);
                lists.setAdapter(drawerAdapter);
                lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                        mProgressDialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (i == 0) {
                                    Intent intent = new Intent(getContext(), OurMenu.class);
                                    intent.putExtra(getString(R.string.values), itemcnt);
                                    intent.putExtra(getString(R.string.price), price);
                                    intent.putExtra(getString(R.string.total), rcvdstring);
                                    startActivityForResult(intent, 2);
                                    mProgressDialog.dismiss();
                                } else if (i == 1) {
                                    Intent intent = new Intent(getContext(), LocateActivity.class);
                                    intent.putExtra(getString(R.string.values), itemcnt);
                                    intent.putExtra(getString(R.string.price), price);
                                    intent.putExtra(getString(R.string.total), rcvdstring);
                                    startActivityForResult(intent, 6);
                                    mProgressDialog.dismiss();
                                } else if (i == 2) {
                                    Intent intent = new Intent(getContext(), OurMenu.class);
                                    intent.putExtra(getString(R.string.values), itemcnt);
                                    intent.putExtra(getString(R.string.price), price);
                                    intent.putExtra(getString(R.string.total), rcvdstring);
                                    startActivityForResult(intent, 3);
                                    mProgressDialog.dismiss();
                                } else if (i == 3) {
                                    Intent intent = new Intent(getContext(), Contact.class);
                                    startActivity(intent);
                                    mProgressDialog.dismiss();
                                }
                            }
                        }, 5000);

                    }

                });
            }
        });

        signbutton = (Button) view.findViewById(R.id.signin);
        signupbutton = (Button) view.findViewById(R.id.signup);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignUp.class);
                startActivity(intent);
            }
        });
        signbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignIn.class);
                startActivity(intent);
            }
        });
        return view;
    }


    class NavItems {
        String mTitle;
        int mIcon;

        public NavItems(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    class DrawerAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<NavItems> navItemArrayList;

        public DrawerAdapter(Context context, ArrayList<NavItems> navItems) {
            mContext = context;
            navItemArrayList = navItems;
        }

        @Override
        public int getCount() {
            return navItemArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return navItemArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1 = inflater.inflate(R.layout.home_drawer_item, viewGroup, false);
            } else {
                view1 = view;
            }
            ImageView imageView = (ImageView) view1.findViewById(R.id.itemimage);
            TextView textView = (TextView) view1.findViewById(R.id.itemtext);
            boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
            if (tabletsize) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                textView.setTextColor(getResources().getColor(R.color.colorblack));
            }
            boolean bigtabletsize = getResources().getBoolean(R.bool.isbigTablet);
            if (bigtabletsize) {
                ViewGroup.LayoutParams params = view1.getLayoutParams();
                params.height = 175;
                view1.setLayoutParams(params);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            textView.setText(navItemArrayList.get(i).mTitle);
            imageView.setImageResource(navItemArrayList.get(i).mIcon);
            return view1;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        RelativeLayout menulayout = (RelativeLayout) menu.findItem(R.id.shoppinglist).getActionView();
        ui_hot = (TextView) menulayout.findViewById(R.id.hotlist_hot);
        int sizes = StartersListClass.starterclasses.size();
        if (sizes != 0) {
            ui_hot.setVisibility(View.VISIBLE);
            // ui_hot.setText(Integer.toString(sizes));
            ui_hot.setText(String.format(Locale.ENGLISH, "%d", sizes));
            ui_hot.setContentDescription(Integer.toString(sizes) + getString(R.string.itemselected));
        }
        Drawable drawable = menu.getItem(0).getIcon();
        Drawable drawable1 = menu.getItem(0).getIcon();
        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewSummary.class);
                intent.putExtra(getString(R.string.summary), selectedlist);
                intent.putExtra(getString(R.string.price), price);
                intent.putExtra(getString(R.string.total), rcvdstring);
                startActivity(intent);
            }
        });

        drawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);
        drawable1.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                int value = data.getIntExtra(getString(R.string.values), 0);
                rcvdstring = data.getStringExtra(getString(R.string.total));
                price = data.getDoubleExtra(getString(R.string.price), 0);
                itemcnt = value;
                if (value != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    //    ui_hot.setText(Integer.toString(value));
                    ui_hot.setText(String.format(Locale.ENGLISH, "%d", value));
                    ui_hot.setContentDescription(Integer.toString(value) + getString(R.string.itemselected));
                }
            }
        }

        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                int value = data.getIntExtra(getString(R.string.values), 0);
                rcvdstring = data.getStringExtra(getString(R.string.total));
                price = data.getDoubleExtra(getString(R.string.price), 0);
                itemcnt = value;
                if (value != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    //   ui_hot.setText(Integer.toString(value));
                    ui_hot.setText(String.format(Locale.ENGLISH, "%d", value));
                    ui_hot.setContentDescription(Integer.toString(value) + getString(R.string.itemselected));
                }
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                int value = data.getIntExtra(getString(R.string.values), 0);
                rcvdstring = data.getStringExtra(getString(R.string.total));
                price = data.getDoubleExtra(getString(R.string.price), 0);
                //  Log.d(TAG, "the price is" + price);
                itemcnt = value;
                if (value != 0) {
                    ui_hot.setVisibility(View.VISIBLE);
                    // ui_hot.setText(Integer.toString(value));
                    ui_hot.setText(String.format(Locale.ENGLISH, "%d", value));
                    ui_hot.setContentDescription(Integer.toString(value) + getString(R.string.itemselected));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menushare = menu.findItem(R.id.Shareaction);
        menushare.setVisible(false);
    }
}

