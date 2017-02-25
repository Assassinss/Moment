package me.zsj.moment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author zsj
 */
public class NetUtils {

    public static boolean checkNet(Context context) {
        boolean mWifiConnected = isWifiConnected(context);

        boolean mMobileConnected = isMobileConnected(context);

        if (!mWifiConnected && !mMobileConnected) {
            return false;
        }
        return true;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return info != null && info.isConnected();
    }
}