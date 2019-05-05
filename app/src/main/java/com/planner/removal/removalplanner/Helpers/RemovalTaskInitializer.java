package com.planner.removal.removalplanner.Helpers;

import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;

import java.util.Date;

public class RemovalTaskInitializer extends TaskInitializer{

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {

        if(!isInitialized) {
            isInitialized = true;
        }
    }
}
