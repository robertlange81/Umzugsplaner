package com.planner.removal.removalplanner.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.github.paolorotolo.appintro.AppIntro;
import com.planner.removal.removalplanner.Fragments.IntroFragmentHello;
import com.planner.removal.removalplanner.Fragments.IntroFragmentInput;
import com.planner.removal.removalplanner.Fragments.IntroFragmentLegend;
import com.planner.removal.removalplanner.Helpers.TaskInitializer;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.R;

public class IntroActivity extends AppIntro {

    IntroFragmentHello fragmentHello;
    IntroFragmentInput fragmentInput;
    IntroFragmentLegend fragmentLegend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentHello = new IntroFragmentHello();
        fragmentInput = new IntroFragmentInput();
        fragmentLegend = new IntroFragmentLegend();

        if (getBaseContext().getResources().getInteger(R.integer.orientation) == 1){
            // xs/sm only
            addSlide(fragmentHello);
        }
        addSlide(fragmentInput);
        addSlide(fragmentLegend);

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
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if(oldFragment == fragmentInput && newFragment == fragmentLegend && Task.TASK_LIST.size() == 0) {
            if(fragmentInput.getRemovalDate() != null || fragmentInput.getRemovalLocation() != null)
                TaskInitializer.InitTasks(fragmentInput.getRemovalDate(), fragmentInput.getRemovalLocation(), this);
        }
    }
}