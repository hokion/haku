package com.hokion.haku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by NGo on 2016/03/28.
 */
public class autostart extends BroadcastReceiver {
    public void onReceive(Context arg0, Intent arg1) {
        arg0.startService(new Intent(arg0,DrawerService.class));
    }
}
