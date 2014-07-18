package com.huewu.pla.lib;

import android.util.Log;

public class DebugUtil {
    
    private static final boolean DEBUG = false;
    private static final String TAG = "PLA";

    public static void LogError(String log) {
        if (DEBUG)
            Log.e(TAG, "" + log);
    }

    public static void LogDebug(String log) {
        if (DEBUG)
            Log.d(TAG, "" + log);
    }

    public static void i(String log) {
        if (DEBUG)
            Log.i(TAG, "" + log);
    }
}//end of class
