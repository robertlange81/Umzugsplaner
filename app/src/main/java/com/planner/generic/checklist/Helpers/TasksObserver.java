package com.planner.generic.checklist.Helpers;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.planner.generic.checklist.Activities.MainActivity;

public class TasksObserver extends ContentObserver {

    // dont call Handler over UI!
    public TasksObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        int x = 1;
        Log.d("DEBUG", "TasksObserver");
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        // do s.th.
        // depending on the handler you might be on the UI
        // thread, so be cautious!
        Log.d("DEBUG", "TasksObserver");
        // MainActivity.SimpleItemRecyclerViewAdapter.notifyDataSetChanged();
        int x = 1;
    }
}
