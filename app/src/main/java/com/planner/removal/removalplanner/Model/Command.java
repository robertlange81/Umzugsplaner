package com.planner.removal.removalplanner.Model;

import android.view.View;

import com.planner.removal.removalplanner.Activities.MainActivity;

public class Command implements View.OnClickListener{

    public enum CommandTyp {
        Add, Remove
    }

    CommandTyp _commandTyp;
    MainActivity.SimpleItemRecyclerViewAdapter _adapter;
    Task _task;

    public Command(CommandTyp commandTyp, MainActivity.SimpleItemRecyclerViewAdapter adapter, Task task) {
        _commandTyp = commandTyp;
        _adapter = adapter;
        _task = task;
    }

    @Override
    public void onClick(View v) {
        // TODO: check Map
        if(_commandTyp == CommandTyp.Add) {
            _adapter.add(_task);
        } else {
            _adapter.remove(_task);
        }
        _adapter.notifyDataSetChanged();
    }
}
