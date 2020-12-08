package com.planner.generic.base.Helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.planner.generic.base.Activities.MainActivity;

public class OpenFileListener implements View.OnClickListener {
    Activity activity;
    String URI;
    public OpenFileListener(Activity a, String uri) {
        activity = a;
        URI = uri;
    }

    @Override
    public void onClick(View v) {
        Uri exportFile = Uri.parse(URI);
        Intent openFileIntent = new Intent();
        openFileIntent.setAction(Intent.ACTION_VIEW);
        openFileIntent.setDataAndType(exportFile, activity.getContentResolver().getType(exportFile));
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(openFileIntent);
    }
}
