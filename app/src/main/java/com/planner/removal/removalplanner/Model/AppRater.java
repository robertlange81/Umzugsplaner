package com.planner.removal.removalplanner.Model;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planner.removal.removalplanner.R;

public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext, String APP_PNAME, String APP_TITLE) {
        SharedPreferences prefs = mContext.getSharedPreferences("checklist", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        /*
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                if(Math.random() >= 0.8d)
                    showRateDialog(mContext, editor, APP_PNAME, APP_TITLE);
            }
        }
        */

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor, final String APP_PNAME, final String APP_TITLE) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(mContext.getResources().getString(R.string.Rate_App));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText(mContext.getResources().getString(R.string.textRate));
        tv.setWidth(1000);
        tv.setPadding(50, 50, 50, 50);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText(mContext.getResources().getString(R.string.Rate_App));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rateApp(mContext, APP_PNAME);
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText(mContext.getResources().getString(R.string.Remind_me_later));
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        dialog.setContentView(ll);
        dialog.show();
    }

    public static void rateApp(Context mContext, String APP_PNAME)
    {
        Uri uri = Uri.parse("market://details?id=" + APP_PNAME);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            mContext.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
    }
}