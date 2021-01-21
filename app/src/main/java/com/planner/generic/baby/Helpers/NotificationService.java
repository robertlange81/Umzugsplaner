package com.planner.generic.baby.Helpers;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.planner.generic.baby.Activities.SplashActivity;
import com.planner.generic.baby.Helpers.DBConverter.UuidConverter;
import com.planner.generic.baby.Model.Task;
import com.planner.generic.baby.R;
import java.util.Timer;
import java.util.TimerTask;

import static com.planner.generic.baby.Activities.MainActivity.maxreminders;

public class NotificationService extends Service {
    public int counter=0;

    public NotificationService(Context applicationContext) {
        super();
        Log.i("NotificationService", "Constructor");
    }

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "onStartCommand!");
        startTimer();
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
        //return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NotificationService", "ondestroy!");
        Intent broadcastIntent = new Intent(this, NotificationRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
        stopSelf();
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        Log.d ("NotificationRestarter", "startTimer");
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every second
        long minitCheck = 1000;
        timer.schedule(timerTask, 1000, minitCheck);
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("NotificationService", "in timer ++++  "+ (counter++));
                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isActive", true)) {
                    Log.i("NotificationService", "don't remind when app is active");
                    return;
                }

                Task next = getNextTask();
                if(next == null)
                    return;

                createChannel(next);
                NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
                notifyManager.notify(next.id.toString().hashCode(), createNotification(next).build());
            }
        };
    }

    private Task getNextTask() {

        long current = System.currentTimeMillis();
        long oneHourInMillis    = 1000 * 60 * 60;
        long threeHourInMillis  = 3 * oneHourInMillis;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("checklist", Context.MODE_MULTI_PROCESS);

        if(prefs != null) {
            for(int i = 0; i < maxreminders; i++) {
                long next = prefs.getLong("nextTaskTime" + i, 0);
                Log.i("getNextTask", (next - current) / (60 * 1000) + "");
                if (next != 0 && (next - current) > oneHourInMillis && (next - current) < threeHourInMillis) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("nextTaskTime" + i, 0).apply(); // delete task to be not notified twice
                    return new Task(UuidConverter.fromString(prefs.getString("nextTaskId" + i, "")), prefs.getString("nextTaskName" + i, ""), prefs.getString("nextTaskDesc" + i, ""));
                }
            }
        }

        return null;
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        Log.i("NotificationServce", "stoptimertask");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    protected void createChannel(Task next) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    getApplicationContext().getString(R.string.app_name), // unique name
                    getApplicationContext().getString(R.string.app_name), // name of the group
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(next.name);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
    }

    protected NotificationCompat.Builder createNotification(Task task) {
        // Create notification
        Context cx = getApplicationContext();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(cx, getApplicationContext().getString(R.string.app_name))
                .setContentTitle(task.name)
                .setContentText(task.description)
                .setSmallIcon(R.drawable.ic_baseline_child_care_24)
                .setLargeIcon(BitmapFactory.decodeResource(cx.getResources(), R.drawable.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "REMINDERS";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) cx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        Intent intentForActivity = new Intent(getApplicationContext(), SplashActivity.class);
        //intentForActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intentForActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intentForActivity.setAction(Intent.ACTION_MAIN);
        intentForActivity.addCategory(Intent.CATEGORY_LAUNCHER);
        intentForActivity.setAction("reminderAction");
        intentForActivity.addCategory("babyList");
        //intentForActivity.putExtra(DetailActivity.ARG_TASK_ID, task.id.toString());
        //Log.i("set task id to intent", task.id.toString());
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentForActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);
        return builder;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
