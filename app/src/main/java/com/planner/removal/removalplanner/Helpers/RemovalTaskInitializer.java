package com.planner.removal.removalplanner.Helpers;

import java.util.Date;

public class RemovalTaskInitializer extends TaskInitializer{

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {

        if(!isInitialized) {
            isInitialized = true;
        }
    }
}
