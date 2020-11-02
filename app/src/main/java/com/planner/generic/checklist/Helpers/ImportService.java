package com.planner.generic.checklist.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.planner.generic.checklist.Activities.MainActivity;
import com.planner.generic.checklist.Helpers.DBConverter.TimestampConverter;
import com.planner.generic.checklist.Model.CheckListSerialized;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportService extends ExportService {

    public static final int IMPORT_SUCCESS = 3;
    public static final int IMPORT_ERROR = -3;

    protected String getName() {
        return getString(R.string.ImportNotificationMessage);
    }

    protected String getDescription() {
        return getString(R.string.ImportNotificationMessage);
    }

    protected int getIcon() {
        return R.drawable.ic_restore_page_black_24dp;
    }

    protected static String getNotificationChannel() {
        return "Import";
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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null)
            return;

        Uri fileUri = intent.getData();
        InputStream selectedFileInputStream = null;

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        if (fileUri == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("URI", fileUri.toString());
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        createChannel();
        NotificationCompat.Builder builder = createNotification(fileUri);

        try {
            builder.setProgress(10, 0, false);
            notifyManager.notify(_NOTIFICATION_ID, builder.build());

            selectedFileInputStream = getContentResolver().openInputStream(fileUri);
            if (selectedFileInputStream != null) {
                ObjectInputStream ois = new ObjectInputStream(selectedFileInputStream);
                CheckListSerialized saving = (CheckListSerialized) ois.readObject();
                Persistance.PruneAllTasks(
                  MainActivity.instance,
                  true,
                  saving.targetTimeStamp == null ? null : new Date(saving.targetTimeStamp),
                  saving.location,
                  saving.tasks);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            receiver.send(IMPORT_ERROR, bundle);
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            receiver.send(IMPORT_ERROR, bundle);
        } finally {
            try {
                if(selectedFileInputStream != null) {
                    selectedFileInputStream.close();
                }

                builder.setProgress(10, 10, false)
                  .setContentText(getString(R.string.ImportNotificationFinishedMessage));
                notifyManager.notify(_NOTIFICATION_ID, builder.build());
                receiver.send(IMPORT_SUCCESS, bundle);
            } catch (Exception e) {
                e.printStackTrace();
                receiver.send(IMPORT_ERROR, bundle);
            }
        }
    }
}
