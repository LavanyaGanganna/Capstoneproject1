package com.example.android.capstoneproject1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavanya.myapplication.backend.ordersApi.OrdersApi;
import com.example.lavanya.myapplication.backend.ordersApi.model.Orders;
import com.example.lavanya.myapplication.backend.usersApi.UsersApi;
import com.example.lavanya.myapplication.backend.usersApi.model.Users;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/******
 * import com.paypal.android.sdk.payments.PayPalConfiguration;
 * import com.paypal.android.sdk.payments.PayPalPayment;
 * import com.paypal.android.sdk.payments.PayPalService;
 * import com.paypal.android.sdk.payments.PaymentActivity;
 * import com.paypal.android.sdk.payments.PaymentConfirmation;
 * <p>
 * import java.io.IOException;
 * import java.math.BigDecimal;
 * import java.util.ArrayList;
 * <p>
 * /**
 * Created by lavanya on 11/23/16.
 */

public class PaypalFragment extends Fragment {

    public static final String PAYPAL_CLIENT_ID = "Ab-SaIT8IXLvHG6NKVNXV00b_UCuWC0YzE01SZbKTM_4-iMNaJmGwXiaADDuUWtrm03fnfWHEy0tJJq1";

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String TAG = PaypalFragment.class.getSimpleName();
    private static PayPalConfiguration config;
    Button paynow;
    TextView textview;
    String emails;
    double price;
    double prics;
    int itemcnt;
    ProgressBar progressBar;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.paypalfragment, container, false);
        paynow = (Button) view.findViewById(R.id.paynow);
        enablebutton(false);
        progressBar = (ProgressBar) view.findViewById(R.id.pbar);
        // textView= (TextView) findViewById(R.id.paytext);
        price = getArguments().getDouble("price", 0);
        prics = price + 1.0;
        itemcnt = getArguments().getInt("values", 0);
        //emails=getIntent().getStringExtra("emailaddr");
        //  textView.setText(String.format("%s", vals));

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                boolean islogin = sharedPreferences.getBoolean("isLogin", false);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefs.edit().putBoolean("paypal", true).apply();
                if (!islogin) {
                    Intent intent = new Intent(getContext(), SignIn.class);
                    SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
                    sharedPreferences1.edit().putBoolean("paypal", true).apply();
                    intent.putExtra("price", prics);
                    startActivity(intent);
                } else {

                    config = new PayPalConfiguration()
                            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                            // or live (ENVIRONMENT_PRODUCTION)
                            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                            .clientId(PAYPAL_CLIENT_ID);
                    Intent intent = new Intent(getContext(), PayPalService.class);

                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    getActivity().startService(intent);
                    String paymentAmount = Double.toString(price);
                    //Creating a paypalpayment
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Simplified Coding Fee",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    //Creating Paypal Payment activity intent
                    Intent intents = new Intent(getContext(), PaymentActivity.class);

                    //putting the paypal configuration to the intent
                    intents.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    //Puting paypal payment to the intent
                    intents.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                    //Starting the intent activity for result
                    //the request code will be used on the method onActivityResult
                    startActivityForResult(intents, PAYPAL_REQUEST_CODE);
                }

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    String state = confirm.getProofOfPayment().getState();
                    if (state.equals("approved")) {
                        enablebutton(false);
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        emails = sharedPreferences.getString("emailaddr", null);

                        for (int i = 0; i < StartersListClass.starterclasses.size(); i++) {
                            titles.add(StartersListClass.starterclasses.get(i).getTitles());
                            prices.add(StartersListClass.starterclasses.get(i).getPrices());

                        }

                        new EndpointsAsyncTask().execute();

                    } else {
                        Toast.makeText(getContext(), "error in payment", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "confirmation is null", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    public void enablebutton(boolean value) {
        paynow.setEnabled(value);
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, Void> {
        private OrdersApi myApiService = null;
        Orders order;

        @Override
        protected Void doInBackground(Void... strings) {
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
                Orders orders = new Orders();
                orders.setEmailaddr(emails);
                orders.setTitle(titles);
                orders.setPrice(prices);
                orders.setTotalprice(Double.toString(price));
                order = myApiService.insert(orders).execute();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "payment approved", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), Thanks.class);
            startActivity(intent);
        }
    }
}
