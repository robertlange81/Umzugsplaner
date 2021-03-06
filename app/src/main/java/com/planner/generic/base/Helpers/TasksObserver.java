package com.planner.generic.base.Helpers;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.planner.generic.base.Activities.Refreshable;

public class TasksObserver extends ContentObserver {

    private final Refreshable refreshable;

    // dont call Handler over UI!
    public TasksObserver(Handler handler, Refreshable refreshable) {
        super(handler);
        this.refreshable = refreshable;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d("DEBUG", "TasksObserver");
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        // do s.th.
        // depending on the handler you might be on the UI
        // thread, so be cautious!
        Log.d("DEBUG", "TasksObserver");
        this.refreshable.Refresh();
    }
}
