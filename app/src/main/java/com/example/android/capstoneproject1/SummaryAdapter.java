package com.example.android.capstoneproject1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lavanya on 11/18/16.
 */

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {
    private static final String TAG = SummaryAdapter.class.getSimpleName();
    Context mcontext;
    List<Starterclass> selectlist = new ArrayList<Starterclass>();

    public interface SummaryInterface {
        void showValues(int values, double price, String sending);

    }

    SummaryInterface summaryInterface;

    public SummaryAdapter(Context context, List<Starterclass> selected) {
        mcontext = context;
        selectlist = selected;
        summaryInterface = (SummaryInterface) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mcontext, selectlist);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // holder.imageView.setImageResource(selectlist.get(position).getImageid());
        Picasso.with(mcontext).load(selectlist.get(position).getImageid()).into(holder.imageView);
        holder.textView.setText(selectlist.get(position).getTitles());
        holder.textView1.setText(selectlist.get(position).getPrices());
        holder.rembutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setMessage(R.string.removeitem);
                alertDialogBuilder.setPositiveButton(R.string.remv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String tosend = "";
                        selectlist.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), selectlist.size());
                        StartersListClass startersListClass = new StartersListClass();
                        if (startersListClass.getsize() > 1) {
                            tosend = String.format(Locale.ENGLISH, "%d " + mcontext.getString(R.string.itemss) + "%.2f", startersListClass.getsize(), startersListClass.getprice());
                        } else {
                            tosend = String.format(Locale.ENGLISH, "%d " + mcontext.getString(R.string.item) + "%.2f", startersListClass.getsize(), startersListClass.getprice());
                        }
                        summaryInterface.showValues(startersListClass.getsize(), startersListClass.getprice(), tosend);
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.cancl, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return selectlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{
        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public Button rembutton;
        public Context mcontext;
        public List<Starterclass> starterclasses;

        public ViewHolder(View itemView, Context context, List<Starterclass> starterclasses) {
            super(itemView);
            this.mcontext = context;
            this.starterclasses = starterclasses;
            imageView = (ImageView) itemView.findViewById(R.id.posterid);
            textView = (TextView) itemView.findViewById(R.id.recipetextid);
            textView1 = (TextView) itemView.findViewById(R.id.costextid);
            rembutton = (Button) itemView.findViewById(R.id.remove);

        }

    }
}
