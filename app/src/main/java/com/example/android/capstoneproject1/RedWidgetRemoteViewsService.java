package com.example.android.capstoneproject1;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by lavanya on 12/16/16.
 */

public class RedWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return StartersListClass.starterclasses.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                views.setImageViewResource(R.id.widget_posterid, StartersListClass.starterclasses.get(position).getImageid());
                views.setTextViewText(R.id.widget_recipetextid, StartersListClass.starterclasses.get(position).getTitles());
                views.setTextViewText(R.id.widget_costextid, StartersListClass.starterclasses.get(position).getPrices());
                final Intent fillInIntent = new Intent(RedWidgetRemoteViewsService.this, ViewSummary.class);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_icon, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {

                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
