package com.chillarapps.networkinfowidget.util;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.chillarapps.networkinfowidget.NetworkInfoWidgetProvider;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by chillaranand on 8/13/17.
 */

public class NetworkInfo {

    public static String[] getData(NetworkInfoWidgetProvider niw) {

        String ipAddress = null;

        String network = "Offline";
        String ip = "0.0.0.0";
        String data[] = {network, ip};

        ConnectivityManager connManager = niw.connManager;
        WifiManager wifiManager = niw.wifiManager;

        if(wifiManager != null) {
            Log.d(TAG, "Getting Wifi info");
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID();
            ipAddress = castIP(info.getIpAddress());
            network = "WiFi : " + ssid;
            ip = "IP: " + ipAddress;
            Log.d(TAG, network + ip);
        }
        if (ipAddress == null) {
            Log.d(TAG, "Getting mobile data info");
            network = "Mobile Data";
            ip = getMobileIPAddress();
            Log.d(TAG, network + ip);
        }
        Log.d(TAG, network + ip);
        return data;
    }


    private static String castIP(int ipAddress) {
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e(TAG, "Unable to get host address.");
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
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

}
