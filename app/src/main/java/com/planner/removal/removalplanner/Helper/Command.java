package com.planner.removal.removalplanner.Helper;

import android.util.Log;
import android.view.View;
import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.Model.Task;

public class Command implements View.OnClickListener{

    private final CommandTyp _commandTyp;
    private final Task _task;
    private MainActivity.SimpleItemRecyclerViewAdapter _adapter;
    private boolean _hasbeenExecuted;

    public enum CommandTyp {
        Add(0), Remove(1), Undo(2);

        private int value;

        CommandTyp(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Command(CommandTyp commandTyp, MainActivity.SimpleItemRecyclerViewAdapter adapter, Task task) {
        _commandTyp = commandTyp;
        _adapter = adapter;
        _task = task;
    }

    public Command(CommandTyp commandTyp, Task task) {
        _commandTyp = commandTyp;
        _task = task;
    }

    @Override
    public void onClick(View v) {
        // TODO: check Map
        if(_hasbeenExecuted)
            return;

        _hasbeenExecuted = true;
        switch (_commandTyp.getValue()) {
            case 0: //CommandTyp.Add
                _adapter.add(_task);
                break;
            case 1: //CommandTyp.Remove
                _adapter.remove(_task);
                break;
            case 2: //CommandTyp.Undo
                Log.e("command", "command");
                Task.TASK_MAP.put(_task.id, _task);
        }

        MainActivity.notifyTaskChanged();
        TaskDetailFragment.notifyTaskChanged();
    }
}