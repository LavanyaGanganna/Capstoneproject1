package com.example.android.capstoneproject1;


import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lavanya.myapplication.backend.usersApi.model.Users;

import data.MenuTableContract;

/**
 * Created by lavanya on 12/4/16.
 */

public class GetMenuData {
    Context mcontext;
    int imageno;
    String titletext;
    String pricetext;
    int images;

    public GetMenuData(Context context) {
        mcontext = context;
    }

    public void getcursor(Cursor cursor) {

        cursorAsyncTask cursorTask = new cursorAsyncTask();
        cursorTask.execute(cursor);
    }


    public class cursorAsyncTask extends AsyncTask<Cursor, Void, Void> {

        // Cursor cursor = mcontext.getContentResolver().query(MenuTableContract.MenuEntry.CONTENT_URI, null, null, null, null);
        @Override
        protected Void doInBackground(Cursor... cursors) {
            Cursor cursor = cursors[0];
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    imageno = cursor.getInt(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FIRST_VAL));
                    titletext = cursor.getString(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FOOD_TITLE));
                    pricetext = cursor.getString(cursor.getColumnIndexOrThrow(MenuTableContract.MenuEntry.COLUMN_FOOD_PRICE));
                    images = getimage(imageno);
                    Starterclass starterclass = new Starterclass(images, titletext, pricetext);
                    DatabaseList.fullmenulist.add(starterclass);

                } while (cursor.moveToNext());
                cursor.close();
            }
            return null;
        }
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