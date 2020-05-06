package com.planner.removal.removalplanner.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planner.removal.removalplanner.Helpers.Comparators.DateComparator;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class IntroFragmentOverview extends Fragment {

    TextView days, hours, minits;
    TextView taskTextView;

    static String bullet = "• ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView   = inflater.inflate(R.layout.fragment_intro_overview, container, false);

        SharedPreferences prefs = this.getContext().getSharedPreferences("removal", 0);
        long removalTimestamp = prefs.getLong("removal_timestamp", 0L);

        long now = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
        if(removalTimestamp != 0) {

            // print days
            long diff = removalTimestamp - now;
            days   = rootView.findViewById(R.id.intro_overview_counter_days);
            long daysNumber = diff / (86400000); // 1000 * 60 * 60 * 24 = 86400000 = 1 day in ms
            days.setText(String.valueOf(daysNumber));
            long rest = diff % (86400000);

            // print hours
            hours  = rootView.findViewById(R.id.intro_overview_counter_hours);
            long hoursNumber = rest / (3600000); // 1000 * 60 * 60 = 3600000 = 1 hour in ms
            rest = diff % (3600000);
            hours.setText(String.valueOf(hoursNumber));


            minits = rootView.findViewById(R.id.intro_overview_counter_minutes);
            long minitsNumber = rest / (60000); // 1000 * 60 = 60000 = 1 minute in ms
            minits.setText(String.valueOf(minitsNumber));
        }

        Task.lock.lock();
        List<Task> t = Task.getTaskListClone();
        Task.lock.unlock();
        Collections.sort(t, new DateComparator(true));
        int showNextTasksNumber = 0;
        String[] nextTaskName = new String[3];
        for(Task task: t) {
            if(!task.is_Done && task.date != null) {
                nextTaskName[showNextTasksNumber] = task.name;

                if(showNextTasksNumber == 0) {
                    taskTextView = rootView.findViewById(R.id.intro_overview_next_0);
                }

                if(showNextTasksNumber == 1) {
                    taskTextView = rootView.findViewById(R.id.intro_overview_next_1);
                }

                if(showNextTasksNumber == 2) {
                    taskTextView = rootView.findViewById(R.id.intro_overview_next_2);
                }

                taskTextView.setText(bullet + nextTaskName[showNextTasksNumber]);
                if(task.date.getTime() < now) {
                    taskTextView.setTextColor(Color.RED);
                }

                if(++showNextTasksNumber >= 3)
                    break;
            }
        }

        if(taskTextView == null) {
            taskTextView = rootView.findViewById(R.id.intro_overview_next_0);
            taskTextView.setText(bullet + getString(R.string.introDateNecessary));
        }

        return rootView;
    }
}