package com.example.android.capstoneproject1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import data.MenuDbHelper;

/**
 * Created by lavanya on 12/4/16.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "menu.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = DatabaseOpenHelper.class.getSimpleName();
    private static String DB_PATH = "";
    private Context mcontext;

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        mcontext = context;
    }

    public class copydata extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InputStream mInput = mcontext.getAssets().open(DATABASE_NAME);
                MenuDbHelper menudbhelper = new MenuDbHelper(mcontext);
                menudbhelper.getWritableDatabase();
                String outFileName = DB_PATH + DATABASE_NAME;
                OutputStream mOutput = new FileOutputStream(outFileName);
                byte[] mBuffer = new byte[1024];
                int mLength;
                while ((mLength = mInput.read(mBuffer)) > 0) {
                    // Log.d(TAG, "copying");
                    mOutput.write(mBuffer, 0, mLength);
                }
                mOutput.flush();
                mOutput.close();
                mInput.close();

                menudbhelper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }

    public void copyDataBase() throws IOException {
        copydata copydatas = new copydata();
        copydatas.execute();
    }
}
