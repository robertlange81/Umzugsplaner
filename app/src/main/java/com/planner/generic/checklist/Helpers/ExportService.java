package com.planner.generic.checklist.Helpers;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.planner.generic.checklist.Model.CheckListSerialized;
import com.planner.generic.checklist.Model.Location;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.R;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ExportService extends IntentService {

    protected static final int _NOTIFICATION_ID = 500;

    public static final int EXPORT_SUCCESS = 2;
    public static final int EXPORT_ERROR = -2;

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     */
    public ExportService() {
        super("Exporter");
        Log.d("DEBUG", "Exporter");
    }

    protected static String getNotificationChannel() {
        return "Save";
    }

    protected void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    getNotificationChannel(), // unique name
                    getName(), // name of the group
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getDescription());
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }
    }

    protected String getName() {
        return getString(R.string.SaveNotificationMessage);
    }

    protected String getDescription() {
        return getString(R.string.SaveNotificationChannelDescription);
    }

    protected int getIcon() {
        return R.drawable.ic_save_black_24dp;
    }

    protected NotificationCompat.Builder createNotification(Uri exportFile) {
        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getNotificationChannel())
                .setContentTitle(getName())
                .setContentText(getName())
                .setSmallIcon(getIcon())
                .setAutoCancel(true);

        Intent openFileIntent = new Intent();
        openFileIntent.setAction(Intent.ACTION_VIEW);
        openFileIntent.setDataAndType(exportFile, getContentResolver().getType(exportFile));
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, openFileIntent, 0);
        builder.setContentIntent(pIntent);
        return builder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null)
            return;

        Uri file = intent.getData();
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        if (file == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("URI", file.toString());
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        createChannel();
        NotificationCompat.Builder builder = createNotification(file);

        CheckListSerialized saving = new CheckListSerialized();
        saving.tasks = Task.getTaskListClone();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("checklist", 0);

        if(prefs != null) {
            long targetTimestamp = prefs.getLong("target_timestamp", 0L);
            if (targetTimestamp != 0) {
                saving.targetTimeStamp = targetTimestamp;
            }

            saving.location = new Location(
                prefs.getString(Location.PLACE, null),
                prefs.getString(Location.POSTAL, null),
                prefs.getString(Location.STREET, null),
                prefs.getString(Location.STREETNUMBER, null)
            );
        }

        boolean isError = false;

        try {
            if (saving.tasks.size() == 0) {
                return;
            }

            builder.setProgress(saving.tasks.size(), 0, false);
            notifyManager.notify(_NOTIFICATION_ID, builder.build());

            OutputStream os = null;
            try {
                os = getContentResolver().openOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(os);
                out.writeObject(saving);
                notifyManager.notify(_NOTIFICATION_ID, builder.build());
            } catch (IOException e) {
                isError = true;
                e.printStackTrace();
                receiver.send(EXPORT_ERROR, bundle);
            } finally {
                try {
                    receiver.send(EXPORT_SUCCESS, bundle);
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    isError = true;
                    e.printStackTrace();
                    receiver.send(EXPORT_ERROR, bundle);
                }
            }

        } finally {
            if (isError) {
                builder.setProgress(1, 1, false)
                  .setContentText(getString(R.string.errorSaving));
            } else {
                builder.setProgress(1, 1, false)
                  .setContentText(getString(R.string.SaveNotificationFinishedMessage));
                notifyManager.notify(_NOTIFICATION_ID, builder.build());
            }
        }
    }
}
