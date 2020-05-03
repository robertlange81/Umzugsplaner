package com.planner.removal.removalplanner.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.github.paolorotolo.appintro.AppIntro;
import com.planner.removal.removalplanner.Fragments.IntroFragmentOverview;
import com.planner.removal.removalplanner.Fragments.IntroFragmentHello;
import com.planner.removal.removalplanner.Fragments.IntroFragmentInput;
import com.planner.removal.removalplanner.Fragments.IntroFragmentLegend;
import com.planner.removal.removalplanner.Helpers.Persistance;
import com.planner.removal.removalplanner.Helpers.TaskInitializer;
import com.planner.removal.removalplanner.Model.Location;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.R;

import java.util.Date;

public class IntroActivity extends AppIntro {

    IntroFragmentHello fragmentHello;
    IntroFragmentInput fragmentInput;
    IntroFragmentLegend fragmentLegend;
    IntroFragmentOverview fragmentCountdown;
    private Date removalDate;
    private Location removalLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Task.getTaskList().size() == 0) {
            fragmentHello = new IntroFragmentHello();
            fragmentInput = new IntroFragmentInput();
            fragmentLegend = new IntroFragmentLegend();

            if (getBaseContext().getResources().getInteger(R.integer.orientation) == 1) {
                // xs/sm only
                addSlide(fragmentHello);
            }
            addSlide(fragmentInput);
            addSlide(fragmentLegend);
        } else {
            fragmentCountdown = new IntroFragmentOverview();
            addSlide(fragmentCountdown);

            SharedPreferences prefs = getSharedPreferences("removal", 0);
            Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
            if (date_firstLaunch != null && date_firstLaunch * 60 * 60 * 48 < System.currentTimeMillis()) {
                fragmentLegend = new IntroFragmentLegend();
                addSlide(fragmentLegend);
            }
        }

        setBarColor(Color.parseColor("#770033"));
        setSeparatorColor(Color.parseColor("#770033"));

        this.setColorTransitionsEnabled(false);

        showSkipButton(false);
        ((Button) doneButton).setText(getResources().getString(R.string.go));
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Do something when users tap on Done button.
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if(oldFragment instanceof IntroFragmentInput && newFragment instanceof IntroFragmentLegend) {

            if(this.removalDate == null && fragmentInput.getRemovalDate() != null
                    || this.removalLocation == null && fragmentInput.getRemovalLocation() != null
                    || this.removalDate != null && !this.removalDate.equals(fragmentInput.getRemovalDate())
                    || (this.removalLocation != null && !this.removalLocation.equals(fragmentInput.getRemovalLocation()))) {
                // User comes back
                Persistance.PruneAllTasks(this, false,null, null);
            }

            this.removalDate = fragmentInput.getRemovalDate();
            this.removalLocation = fragmentInput.getRemovalLocation();

            // preload new task for performance reasons
            if((this.removalDate != null || this.removalLocation != null) && Task.getTaskList().size() == 0) {
                TaskInitializer.InitTasks(this.removalDate, this.removalLocation, this);
            }
        }
    }
}