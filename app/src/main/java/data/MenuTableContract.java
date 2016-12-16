package data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lavanya on 11/8/16.
 */

public class MenuTableContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.capstoneproject1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MENU = "menures";

    public static final class MenuEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;
        public static final String TABLE_NAME = "menutable";
        public static final String COLUMN_FIRST_VAL = "firstvalue";
        public static final String COLUMN_FOOD_TITLE = "titlename";
        public static final String COLUMN_FOOD_PRICE = "foodprice";

        public static Uri buildMenuUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMenuUri(String menuitem) {
            return CONTENT_URI.buildUpon().appendPath(menuitem).build();
            //.appendPath(itemprice).build();
        }

        public static String getMenutitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
      /*  public static String getPriceFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }*/

    }
}
