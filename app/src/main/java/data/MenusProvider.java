package data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by lavanya on 11/8/16.
 */

public class MenusProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = MenusProvider.class.getSimpleName();
    private MenuDbHelper mOpenHelper;
    static final int MENUS = 100;
    static final int MENUS_DATA = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MenuTableContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MenuTableContract.PATH_MENU, MENUS);
        matcher.addURI(authority, MenuTableContract.PATH_MENU + "/*", MENUS_DATA);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MenuDbHelper(getContext());
        return true;

    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case MENUS_DATA: {
                String menutitle = MenuTableContract.MenuEntry.getMenutitleFromUri(uri);
                selection = MenuTableContract.MenuEntry.TABLE_NAME + "." + MenuTableContract.MenuEntry.COLUMN_FOOD_TITLE + " = ? ";
                //+ MenuTableContract.MenuEntry.COLUMN_PASSWORD + " = ?";
                selectionArgs = new String[]{menutitle};
                retCursor = mOpenHelper.getReadableDatabase().query(MenuTableContract.MenuEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            }

            case MENUS: {
                retCursor = mOpenHelper.getReadableDatabase().query(MenuTableContract.MenuEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MENUS:
                return MenuTableContract.MenuEntry.CONTENT_TYPE;
            case MENUS_DATA:
                return MenuTableContract.MenuEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {
            case MENUS_DATA: {
                long _id = db.insert(MenuTableContract.MenuEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = MenuTableContract.MenuEntry.buildMenuUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MENUS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MenuTableContract.MenuEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
