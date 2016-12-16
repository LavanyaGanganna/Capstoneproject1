package com.example.android.capstoneproject1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lavanya on 11/14/16.
 */

public class Starterclass implements Parcelable {
    int imageid;
    String titles;
    String prices;

    public Starterclass(int imageid, String titles, String prices) {
        this.imageid = imageid;
        this.titles = titles;
        this.prices = prices;
    }

    protected Starterclass(Parcel in) {
        imageid = in.readInt();
        titles = in.readString();
        prices = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageid);
        dest.writeString(titles);
        dest.writeString(prices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Starterclass> CREATOR = new Creator<Starterclass>() {
        @Override
        public Starterclass createFromParcel(Parcel in) {
            return new Starterclass(in);
        }

        @Override
        public Starterclass[] newArray(int size) {
            return new Starterclass[size];
        }
    };

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }
}
