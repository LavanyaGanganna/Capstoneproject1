package com.example.android.capstoneproject1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lavanya.myapplication.backend.ordersApi.OrdersApi;
import com.example.lavanya.myapplication.backend.ordersApi.model.CollectionResponseOrders;
import com.example.lavanya.myapplication.backend.ordersApi.model.Orders;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavanya on 11/30/16.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private static final String TAG = OrdersAdapter.class.getSimpleName();
    List<Ordersclass> mordersclassList;
    Context mcontext;

    public interface Recyclerviewcallback {
        void itemclicked(List<Ordersclass> ordersclassList);
    }

    Recyclerviewcallback recyclerviewcallback;

    public OrdersAdapter(Context context, List<Ordersclass> ordersclassList) {
        mordersclassList = ordersclassList;
        mcontext = context;
        recyclerviewcallback = (Recyclerviewcallback) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_table_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.emailtext.setText(mordersclassList.get(position).getEmailaddr());
        List<String> titlelist = mordersclassList.get(holder.getAdapterPosition()).getTitles();
        //Log.d(TAG, "the email is" + mordersclassList.get(position).getEmailaddr());
        //Log.d(TAG, "the position is" + position);
        for (int j = 0; j < titlelist.size(); j++)
            Log.d(TAG, titlelist.get(j));
        holder.linearlayout.setOrientation(LinearLayout.VERTICAL);
        TextView[] mytextviews = new TextView[titlelist.size()];
        for (int i = 0; i < titlelist.size(); i++) {
            TextView rowtextview = new TextView(mcontext);
            rowtextview.setText(titlelist.get(i));
            holder.linearlayout.addView(rowtextview);
            mytextviews[i] = rowtextview;
        }
        holder.totaltext.setText(mordersclassList.get(position).getTotal());
        holder.delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emails = mordersclassList.get(holder.getAdapterPosition()).getEmailaddr();
                mordersclassList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mordersclassList.size());
                recyclerviewcallback.itemclicked(mordersclassList);
                new EndpointsAsyncTask().execute(emails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mordersclassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView emailtext;
        public Button delbutton;
        public LinearLayout linearlayout;
        public TextView totaltext;

        public ViewHolder(View itemView) {
            super(itemView);
            emailtext = (TextView) itemView.findViewById(R.id.emailtable);
            delbutton = (Button) itemView.findViewById(R.id.delbutton);
            //listView = (ListView) itemView.findViewById(R.id.titleprice);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.titleprice);
            totaltext = (TextView) itemView.findViewById(R.id.totaltext);

        }
    }

    class EndpointsAsyncTask extends AsyncTask<String, Void, Void> {
        private OrdersApi myApiService = null;


        @Override
        protected Void doInBackground(String... strings) {
            if (myApiService == null) {  // Only do this once
                OrdersApi.Builder builder = new OrdersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl(mcontext.getString(R.string.orderappspot))
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }
            String emailaddr = strings[0];

            try {

                Orders orders = myApiService.checkorder(emailaddr).execute();
                Long id = orders.getId();
                myApiService.remove(id).execute();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;

        }


    }
}
