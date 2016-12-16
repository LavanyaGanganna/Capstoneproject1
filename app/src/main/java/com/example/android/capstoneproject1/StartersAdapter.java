package com.example.android.capstoneproject1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by lavanya on 11/14/16.
 */

public class StartersAdapter extends RecyclerView.Adapter<StartersAdapter.ViewHolder> {
    ArrayList<Starterclass> arrayList;
    public static final String FOOD_SHARE_HASHTAG = " #Redchillies";
    int total = 0;
    double price = 0;
    Context mcontext;
    String tosend;
    static Intent shareintent;

    public interface DataTransferInterface {
        public void setValues(int values, double price, String sending);

    }

    DataTransferInterface dataTransferInterface;

    public StartersAdapter(Context context, ArrayList<Starterclass> starterclasses, int count, double price) {
        arrayList = starterclasses;
        mcontext = context;
        total = count;
        this.price = price;
        dataTransferInterface = (DataTransferInterface) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public Button orderbut;

        public ViewHolder(View itemView, final ArrayList<Starterclass> arrayList, final Context mcontext) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.poster);
            textView = (TextView) itemView.findViewById(R.id.recipetext);
            textView1 = (TextView) itemView.findViewById(R.id.costext);
            orderbut = (Button) itemView.findViewById(R.id.order);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareintent = new Intent(android.content.Intent.ACTION_SEND);
                    String sharebody = arrayList.get(getAdapterPosition()).getTitles() + FOOD_SHARE_HASHTAG;
                    shareintent.setType("text/plain");
                    shareintent.putExtra(Intent.EXTRA_TEXT, sharebody);
                    mcontext.startActivity(Intent.createChooser(shareintent, "share via"));
                }
            });
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, arrayList, mcontext);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // holder.imageView.setImageResource(arrayList.get(position).getImageid());
        Picasso.with(mcontext).load(arrayList.get(position).getImageid()).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getTitles());
        holder.textView1.setText(arrayList.get(position).getPrices());

        holder.orderbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tosend = "";
                StartersListClass startersListClass = new StartersListClass();
                StartersListClass.starterclasses.add(arrayList.get(holder.getAdapterPosition()));
                Toast.makeText(mcontext, "item added to cart", Toast.LENGTH_SHORT).show();
                if (startersListClass.getsize() > 1) {
                    tosend = String.format(Locale.ENGLISH, "%d items | $ %.2f", startersListClass.getsize(), startersListClass.getprice());
                } else {
                    tosend = String.format(Locale.ENGLISH, "%d item | $ %.2f", startersListClass.getsize(), startersListClass.getprice());
                }
                dataTransferInterface.setValues(startersListClass.getsize(), startersListClass.getprice(), tosend);


            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
