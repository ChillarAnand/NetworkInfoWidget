package com.chillarapps.networkinfowidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;

import com.chillarapps.networkinfowidget.util.NetworkInfo;

/**
 * Created by chillaranand on 8/10/17.
 */

public class NetworkInfoWidgetProvider extends AppWidgetProvider {

    public ConnectivityManager connManager;
    public WifiManager wifiManager;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.connManager = connManager;
        this.wifiManager = wifiManager;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,
                NetworkInfoWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_network_info);
            String[] data = NetworkInfo.getData(this);
            remoteViews.setTextViewText(R.id.network, data[0]);
            remoteViews.setTextViewText(R.id.ip, data[1]);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
