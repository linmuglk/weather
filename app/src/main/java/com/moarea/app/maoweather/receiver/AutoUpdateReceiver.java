package com.moarea.app.maoweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moarea.app.maoweather.service.AutoUpdateService;

/**
 * Created by Johnny on 2016/6/29.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent_for_service = new Intent(context, AutoUpdateService.class);
        context.startService(intent_for_service);

    }
}
