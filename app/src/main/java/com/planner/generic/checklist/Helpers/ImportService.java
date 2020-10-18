package com.planner.generic.checklist.Helpers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.planner.generic.checklist.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImportService extends ExportService {

    public static final int IMPORT_SUCCESS = 1;
    public static final int IMPORT_ERROR = -1;

    protected String getName() {
        return getString(R.string.ExportNotificationMessage);
    }

    protected String getDescription() {
        return getString(R.string.ExportNotificationChannelDescription);
    }

    protected int getIcon() {
        return R.drawable.ic_restore_page_black_24dp;
    }

    protected static String getNotificationChannel() {
        return "Import";
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
                DataInputStream dis = new DataInputStream(selectedFileInputStream);
                long fileSize = new File(fileUri.getPath()).length();
                byte[] allBytes = new byte[(int) fileSize];
                dis.readFully(allBytes);
                dis.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
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
