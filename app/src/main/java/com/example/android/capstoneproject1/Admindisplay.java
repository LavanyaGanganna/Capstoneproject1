package com.example.android.capstoneproject1;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavanya.myapplication.backend.ordersApi.OrdersApi;
import com.example.lavanya.myapplication.backend.ordersApi.model.CollectionResponseOrders;
import com.example.lavanya.myapplication.backend.ordersApi.model.Orders;
import com.example.lavanya.myapplication.backend.usersApi.UsersApi;
import com.example.lavanya.myapplication.backend.usersApi.model.Users;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Admindisplay extends AppCompatActivity implements OrdersAdapter.Recyclerviewcallback {
    private static final String TAG = Admindisplay.class.getSimpleName();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Ordersclass> displaylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindisplay);
        progressBar = (ProgressBar) findViewById(R.id.probar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "inside on create");
        new EndpointsAsyncTask().execute();
        recyclerView = (RecyclerView) findViewById(R.id.orderslist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), "font/DarkLarch_PERSONAL_USE.ttf");
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle("Admin Display");
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }

    }

    @Override
    public void itemclicked(List<Ordersclass> ordersclassList) {
        OrdersAdapter ordersAdapter = new OrdersAdapter(Admindisplay.this, ordersclassList);
        recyclerView.setAdapter(ordersAdapter);
        ordersAdapter.notifyDataSetChanged();
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Ordersclass>> {
        private OrdersApi myApiService = null;
        List<Orders> ordersArrayList = new ArrayList<Orders>();
        CollectionResponseOrders collectionResponseOrders;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Ordersclass> doInBackground(Void... voids) {
            if (myApiService == null) {  // Only do this once
                OrdersApi.Builder builder = new OrdersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl("https://capstoneproject1-150817.appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                Log.d(TAG, "inside the async task");

                collectionResponseOrders = myApiService.list().execute();
                ordersArrayList = collectionResponseOrders.getItems();
                if (ordersArrayList != null) {
                    for (int i = 0; i < ordersArrayList.size(); i++) {
                        List<String> titlesend = new ArrayList<>();
                        String email = ordersArrayList.get(i).getEmailaddr();
                        Log.d(TAG, "the backend order list" + email);
                        List<String> titleprice = ordersArrayList.get(i).getTitle();
                        List<String> pricetitle = ordersArrayList.get(i).getPrice();
                        for (int j = 0; j < titleprice.size(); j++) {
                            String titles = titleprice.get(j) + "      " + pricetitle.get(j);
                            Log.d(TAG, "the backend " + titles);
                            titlesend.add(titles);
                        }
                        String totalcost = ordersArrayList.get(i).getTotalprice();
                        totalcost = String.format("%1s  %2s", "TotalCost:", totalcost);
                        Log.d(TAG, "the backend cost" + totalcost);
                        Ordersclass ordersclass = new Ordersclass(email, titlesend, totalcost);
                        displaylist.add(ordersclass);
                    }

                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return displaylist;

        }

        @Override
        protected void onPostExecute(List<Ordersclass> ordersclassList) {
            super.onPostExecute(ordersclassList);
            progressBar.setVisibility(View.GONE);
            OrdersAdapter ordersAdapter = new OrdersAdapter(Admindisplay.this, ordersclassList);
            ordersAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(ordersAdapter);

        }
    }
}
