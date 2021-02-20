package com.planner.generic.virus.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.github.paolorotolo.appintro.AppIntro;
import com.planner.generic.virus.Fragments.IntroFragmentHello;
import com.planner.generic.virus.Fragments.IntroFragmentIncidence;
import com.planner.generic.virus.Fragments.IntroFragmentInput;
import com.planner.generic.virus.Fragments.IntroFragmentLegend;
import com.planner.generic.virus.Fragments.IntroFragmentOverview;
import com.planner.generic.virus.Helpers.TaskFormater;
import com.planner.generic.virus.Helpers.TaskInitializer;
import com.planner.generic.virus.Model.Location;
import com.planner.generic.virus.Model.Task;
import com.planner.generic.virus.R;

import java.util.Date;

public class IntroActivity extends AppIntro {

    IntroFragmentHello fragmentHello;
    IntroFragmentInput fragmentInput;
    IntroFragmentLegend fragmentLegend;
    IntroFragmentIncidence fragmentIncidence;
    IntroFragmentOverview fragmentCountdown;
    private Date removalDate;
    private Location removalLocation;
    Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(TaskFormater.currentLocal == null) {
            TaskFormater.setCurrentLocale(this);
        }

        if (Task.getTaskList().size() == 0) {
            fragmentHello = new IntroFragmentHello();
            fragmentLegend = new IntroFragmentLegend();
            fragmentInput = new IntroFragmentInput();
            fragmentIncidence = new IntroFragmentIncidence();

            if (getBaseContext().getResources().getInteger(R.integer.tablet) == 1) {
                // xs/sm only
                addSlide(fragmentHello);
            }
            addSlide(fragmentLegend);
            addSlide(fragmentIncidence);
            addSlide(fragmentInput);
        } else {
            fragmentCountdown = new IntroFragmentOverview();
            addSlide(fragmentCountdown);
            fragmentIncidence = new IntroFragmentIncidence();
            addSlide(fragmentIncidence);

            SharedPreferences prefs = getSharedPreferences("checklist", 0);
            Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
            if (date_firstLaunch != null && date_firstLaunch + 2 * 60 * 60 * 48 < System.currentTimeMillis()) {
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

        if(currentFragment instanceof IntroFragmentInput) {

            this.removalDate = fragmentInput.getRemovalDate();
            this.removalLocation = fragmentInput.getRemovalLocation();

            if( this.removalDate == null && this.removalLocation == null && currentFragment.getView() != null) {
                Snackbar snack = Snackbar.make(
                        currentFragment.getView(),
                        getString(R.string.DateOrPlaceNeeded),
                        Snackbar.LENGTH_LONG);
                snack.show();
                return;
            }

            if(Task.getTaskList().size() == 0) {
                TaskInitializer.InitTasks(this.removalDate, this.removalLocation, this, null);
            }
        }

        // Do something when users tap on Done button.
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        currentFragment = newFragment;
    }

    @Override
    public void onBackPressed() {
        if(currentFragment != null
                && (currentFragment instanceof IntroFragmentHello || currentFragment instanceof IntroFragmentOverview)) {
            // moveTaskToBack(true);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }
}