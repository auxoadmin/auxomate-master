package com.auxomate.mynewself.mynewself.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.auxomate.mynewself.mynewself.activities.HomeActivity;

public class ClearPrefReceiver extends BroadcastReceiver {
    PrefManager prefManager;
    public void onReceive (Context context, Intent intent) {

    PrefManager.resetPref(HomeActivity.getContextOfApplication());


    }
}
