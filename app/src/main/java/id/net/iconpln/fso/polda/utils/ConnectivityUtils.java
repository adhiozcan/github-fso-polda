package id.net.iconpln.fso.polda.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import id.net.iconpln.fso.polda.FsoPoldaApp;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.listener.SignalListener;

/**
 * Created by Ozcan on 18/01/2017.
 */

public class ConnectivityUtils {
    private static final int DISCONNECT    = -1;
    private static final int SEARCHING     = 0;
    private static final int SIGNAL_WEAK   = 1;
    private static final int SIGNAL_STRONG = 2;

    private static int            signalStrength;
    private static SignalListener signalListener;

    public static boolean isHaveInternetConnection() {
        Context context = FsoPoldaApp.getContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void determineSignalStrength(SignalListener signalListener) {
        Context context = FsoPoldaApp.getContext();
        ConnectivityUtils.signalListener = signalListener;
        PhoneStateListener phoneStateListener = new InnerPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public static void stopDetermineSignalStrength() {
        Context context = FsoPoldaApp.getContext();

        PhoneStateListener phoneStateListener = new InnerPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private static class InnerPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (signalStrength.isGsm()) {
                int asu = signalStrength.getGsmSignalStrength();
                if (asu == 0 || asu == 99) {
                    ConnectivityUtils.signalStrength = 0;
                    signalListener.onReceivedSignalStrength(DISCONNECT, R.color.signalDisconnect);
                } else if (asu < 5) {
                    ConnectivityUtils.signalStrength = 2;
                    signalListener.onReceivedSignalStrength(SEARCHING, R.color.signalSearching);
                } else if (asu >= 5 && asu < 8) {
                    ConnectivityUtils.signalStrength = 3;
                    signalListener.onReceivedSignalStrength(SIGNAL_WEAK, R.color.signalWeak);
                } else if (asu >= 8 && asu < 12) {
                    ConnectivityUtils.signalStrength = 4;
                    signalListener.onReceivedSignalStrength(SIGNAL_WEAK, R.color.signalWeak);
                } else if (asu >= 12 && asu < 14) {
                    ConnectivityUtils.signalStrength = 5;
                    signalListener.onReceivedSignalStrength(SIGNAL_STRONG, R.color.signalStrong);
                } else if (asu >= 14) {
                    ConnectivityUtils.signalStrength = 6;
                    signalListener.onReceivedSignalStrength(SIGNAL_STRONG, R.color.signalStrong);
                }
            } else {
                int cdmaDbm = signalStrength.getCdmaDbm();
                if (cdmaDbm >= -89) {
                    ConnectivityUtils.signalStrength = 6;
                    signalListener.onReceivedSignalStrength(SIGNAL_STRONG, R.color.signalStrong);
                } else if (cdmaDbm >= -97) {
                    ConnectivityUtils.signalStrength = 5;
                    signalListener.onReceivedSignalStrength(SIGNAL_STRONG, R.color.signalStrong);
                } else if (cdmaDbm >= -103) {
                    ConnectivityUtils.signalStrength = 4;
                    signalListener.onReceivedSignalStrength(SIGNAL_WEAK, R.color.signalWeak);
                } else if (cdmaDbm >= -107) {
                    ConnectivityUtils.signalStrength = 3;
                    signalListener.onReceivedSignalStrength(SIGNAL_WEAK, R.color.signalWeak);
                } else if (cdmaDbm >= 109) {
                    ConnectivityUtils.signalStrength = 2;
                    signalListener.onReceivedSignalStrength(DISCONNECT, R.color.signalDisconnect);
                } else {
                    ConnectivityUtils.signalStrength = 0;
                    signalListener.onReceivedSignalStrength(DISCONNECT, R.color.signalDisconnect);
                }
            }

            L.d("[Info Signal Listen] -------------------------------------------------");
            L.d("Signal Strength [Cat-" + "]" + signalStrength + " --> " + ConnectivityUtils.signalStrength);
        }
    }
}