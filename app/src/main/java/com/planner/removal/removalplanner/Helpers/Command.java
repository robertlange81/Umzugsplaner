package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Task;

import static com.planner.removal.removalplanner.Model.TaskContract.TaskData.item;
import static com.planner.removal.removalplanner.Model.TaskContract.TaskData.list;
import static com.planner.removal.removalplanner.Model.TaskContract.TaskData.list_self;

public class Command implements View.OnClickListener{

    private final CommandTyp _commandTyp;
    private final Task _task;
    private MainActivity.SimpleItemRecyclerViewAdapter _adapter;
    private Activity _activity;
    private boolean _hasbeenExecuted;

    public static boolean isOpenCommand = false;

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

    public Command(CommandTyp commandTyp, MainActivity.SimpleItemRecyclerViewAdapter adapter, Task task, Activity mParentActivity) {
        _commandTyp = commandTyp;
        _adapter = adapter;
        _task = task;
        _activity = mParentActivity;
    }

    public Command(CommandTyp commandTyp, Task task, Activity mParentActivity) {
        _commandTyp = commandTyp;
        _task = task;
        _activity = mParentActivity;
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
                MainActivity.NotifyTaskChanged(_task, _activity, new Long[] {list_self});
                break;
            case 1: //CommandTyp.Remove
                _adapter.remove(_task);
                MainActivity.NotifyTaskChanged(_task, _activity, new Long[] {list_self});
                break;
            case 2: //CommandTyp.Undo
                Log.d("command", "command");
                Task oldTask = Task.TASK_MAP.get(_task.id);

                if(oldTask != null)
                    oldTask.ImportTask(_task);

                MainActivity.NotifyTaskChanged(_task, _activity, new Long[] {list, item});
        }
    }
}
