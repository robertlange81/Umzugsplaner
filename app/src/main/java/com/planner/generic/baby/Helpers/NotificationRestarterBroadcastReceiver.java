package com.planner.generic.baby.Helpers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class NotificationRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(NotificationRestarterBroadcastReceiver.class.getSimpleName(), "Generic Planner Service Stops! Oooooooooooooppppssssss!!!!");

        if (context == null) {
            Log.i(NotificationRestarterBroadcastReceiver.class.getSimpleName(), "GContext is NULL !!!!!!");
            return;
        }
        NotificationService reminderService = new NotificationService(context);
        if (!isReminderRunning(reminderService.getClass(), context)) {
            try {
                context.startService(new Intent(context, NotificationService.class));
            } catch (IllegalStateException ex) {
                // The app is in background, starting service is disallow
                Log.e ("NotificationRestarter", ex.getMessage() == null ? "cant startService" : ex.getMessage());
            } catch (Exception e) {
                Log.e ("NotificationRestarter", e.getMessage() == null ? "cant startService" : e.getMessage());
            }
        }
    }

    public boolean isReminderRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isReminderRunning?", true+"");
                return true;
            }
        }
        Log.i ("isReminderRunning?", false+"");
        return false;
    }
}
