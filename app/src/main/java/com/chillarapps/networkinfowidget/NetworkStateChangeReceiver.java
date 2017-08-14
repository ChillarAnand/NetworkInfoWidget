package com.chillarapps.networkinfowidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

    String TAG = "==================";
    String networkOffline = "Offline";
    String ipOffline = "0.0.0.0";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received Network State Change");
        String wifiIP = null;
        String mobileIP = null;
        String ip = null;
        String network = null;

        try {
            Log.d(TAG, "Sleep");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (cm.getActiveNetworkInfo() == null) {
            Log.d(TAG, "Empty ni");
            network = networkOffline;
            ip = ipOffline;
        } else {
            Log.d(TAG, "Getting Wifi info");
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID();
            String ipAddress = castIP(info.getIpAddress());
            if (ipAddress != null) {
                network = "WiFi : " + ssid;
                ip = "IP: " + ipAddress;
                Log.d(TAG, network + ip);
            } else {
                String ipAddr = getMobileIPAddress();
                if (ipAddr != null) {
                    network = "Mobile Data";
                    ip = ipAddr;
                }
            }
        }
        Log.d(TAG, "STATE: " + network + ip);
        updateWidget(context, network, ip);
    }



    private void updateWidget(Context context, String network, String ip) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());

        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NetworkInfoWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        if(appWidgetIds != null && appWidgetIds.length > 0) {
            for(int widgetId : appWidgetIds) {
                RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.widget_network_info);
                rViews.setTextViewText(R.id.network, network);
                rViews.setTextViewText(R.id.ip, ip);
                appWidgetManager.updateAppWidget(widgetId, rViews);
            }
        }

    }

    private String castIP(int ipAddress) {
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        return ipAddressString;
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

}
