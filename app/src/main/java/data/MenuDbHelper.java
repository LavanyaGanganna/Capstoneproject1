package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lavanya on 11/8/16.
 */

public class MenuDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MenuDbHelper.class.getSimpleName();
    private static int DATABASE_VERSION = 1;
    private static String DB_PATH = "";
    private static String DB_NAME = "menu.db";


    public MenuDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MenuTableContract.MenuEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        DATABASE_VERSION = DATABASE_VERSION + 1;
    }
}
