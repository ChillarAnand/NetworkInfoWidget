package com.chillarapps.networkinfowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by chillaranand on 8/10/17.
 */

public class NetworkInfoWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                NetworkInfoWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_network_info);

            remoteViews.setTextViewText(R.id.update, String.valueOf(number));

            Intent intent = new Intent(context, NetworkInfoWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
