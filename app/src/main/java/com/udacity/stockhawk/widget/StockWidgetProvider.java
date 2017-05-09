package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailActivity;

/**
 * Created by duopegla on 8.5.2017..
 */

public class StockWidgetProvider extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds){
            Intent intent = new Intent(context, MainActivity.class);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_initial);

            Intent widgetIntent = new Intent(context, StockWidgetService.class);
            context.startService(widgetIntent);
            remoteViews.setRemoteAdapter(R.id.widget_list, widgetIntent);

            // Intent to launch Main Activity
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Intent to launch Detail Stock Activity
            Intent detailIntent = new Intent(context, StockDetailActivity.class);
            PendingIntent pendingIntentDetail = PendingIntent.getActivity(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntentDetail);

            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);


        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        if(intent.getAction().equals(QuoteSyncJob.ACTION_DATA_UPDATED)) {
            AppWidgetManager am = AppWidgetManager.getInstance(context);
            int [] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, StockWidgetProvider.class));
            am.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }
}
