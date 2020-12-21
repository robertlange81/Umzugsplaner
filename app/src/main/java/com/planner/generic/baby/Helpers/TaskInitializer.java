package com.planner.generic.baby.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.planner.generic.baby.Model.Location;
import com.planner.generic.baby.Model.Priority;
import com.planner.generic.baby.Model.Task;
import com.planner.generic.baby.Model.TaskType;
import com.planner.generic.baby.Model.TaskTypeBaby;
import com.planner.generic.baby.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TaskInitializer {

    public static ListType CURRENT_LIST_TYPE = ListType.BABY;

    public enum ListType {

        BASE(0),
        RELOCATION(1),
        LOCKDOWN(2),
        BABY(3),
        BIRTHDAY(4),
        CHRISTMAS(5),
        WEDDING(6);

        private int value;

        ListType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static void InitTasks(Date targetDate, Location targetLocation, Activity context, List<Task> newTasks) {

        if (targetDate == null) {
            targetDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
            targetDate.setHours(0);
            targetDate.setMinutes(0);
            targetDate.setSeconds(0);
        }

        if (newTasks !=  null) {
            // just import tasks
            for (Task t : newTasks) {
                Task.addTask(t);
            }
        } else {

            Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            Date tomorrow = addMonthDaysToJavaUtilDate(today, 0, 1);

            switch (CURRENT_LIST_TYPE.toString().toUpperCase()) {
                case "RELOCATION":
                    AddRemovalTasks(targetDate, targetLocation, context, today, tomorrow);
                    break;
                case "LOCKDOWN":
                    AddLockdownTasks(
                      addMonthDaysToJavaUtilDate(targetDate, 0, -22),
                      targetLocation,
                      context,
                      today,
                      tomorrow
                    );
                    break;
                case "BABY":
                    AddBabyTasks(targetDate, targetLocation, context, today, tomorrow);
                    break;
                case "BIRTHDAY":
                    AddBirthdayTasks(targetDate, targetLocation, context, today, tomorrow);
                    break;
                case "CHRISTMAS":
                    AddChristmasTasks(targetDate, targetLocation, context, today, tomorrow);
                    break;
                case "WEDDING":
                    AddWeddingTasks(targetDate, targetLocation, context, today, tomorrow);
                    break;
                case "BASE":
                default:
                    // Do Nothing
            }
        }

        setTargetDateAndLocationToShardPrefs(targetDate, targetLocation, context);

        Persistance.SaveTasks(context);
    }

    private static void AddBabyTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {

        // Vorlage
        Task xxx = new Task("", "", addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Literature), null);
        xxx.addLink("", "");
        Task.addTask(xxx);


        // Wärmelampe
        Task heatLamp = new Task(context.getString(R.string.heatLamp), context.getString(R.string.heatLampDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Furnishings), null);
        heatLamp.addLink(context.getString(R.string.heatLampLinkName), context.getString(R.string.heatLampLinkHref));
        Task.addTask(heatLamp);
        
        // Geburtsurkunde


        // Krippenplatz
        Task dayNursery = new Task(context.getString(R.string.dayNursery), context.getString(R.string.dayNurseryDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        dayNursery.addLink(context.getString(R.string.dayNurseryApplyLinkName), context.getString(R.string.dayNurseryApplyLinkHref));
        dayNursery.addLink(context.getString(R.string.dayNurseryOverviewLinkName), context.getString(R.string.dayNurseryOverviewLinkHref));
        Task.addTask(dayNursery);

        // Strampler
        // dont cry over spoiled milk, but change the strampler

        // Die Hebammensprechstunde
        Task midwife = new Task(context.getString(R.string.midwife), context.getString(R.string.midwifeDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeBaby.Literature), null);
        midwife.addLink(context.getString(R.string.midwifeLinkName), context.getString(R.string.midwifeLinkHref));
        Task.addTask(midwife);


        // Namen finden

        // Elternzeit
    }

    private static void AddBirthdayTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
    }

    private static void AddChristmasTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
    }

    private static void AddWeddingTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
    }

    public static void setTargetDateAndLocationToShardPrefs(Date targetDate, Location targetLocation, Activity context) {
        SharedPreferences prefs = context.getSharedPreferences("checklist", 0);

        if(prefs != null) {
            SharedPreferences.Editor editor = prefs.edit();

            if(targetDate != null) {
                editor.putLong("target_timestamp", targetDate.getTime());
            } else {
                editor.remove("target_timestamp");
            }

            if(targetLocation != null) {
                editor.putString(Location.PLACE, targetLocation.getPlace());
                editor.putString(Location.POSTAL, targetLocation.getPostal());
                editor.putString(Location.STREET, targetLocation.getStreet());
                editor.putString(Location.STREETNUMBER, targetLocation.getStreetNumber());
            } else {
                editor.remove(Location.PLACE);
                editor.remove(Location.POSTAL);
                editor.remove(Location.STREET);
                editor.remove(Location.STREETNUMBER);
            }
            editor.apply();
        }
    }

    private static void AddLockdownTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
    }

    private static void AddRemovalTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {}

    public static Date addMonthDaysToJavaUtilDate(Date date, int months, int days) {

        if(date == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date getLastDayInYearOf(Date date) {

        if(date == null)
            return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
        return cal.getTime();
    }
}
