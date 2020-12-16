package com.planner.generic.christmas.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.planner.generic.christmas.Model.Location;
import com.planner.generic.christmas.Model.Priority;
import com.planner.generic.christmas.Model.Task;
import com.planner.generic.christmas.Model.TaskType;
import com.planner.generic.christmas.Model.TaskTypeChristmas;
import com.planner.generic.christmas.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TaskInitializer {

    public static ListType CURRENT_LIST_TYPE = ListType.CHRISTMAS;

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
    }

    private static void AddBirthdayTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
    }

    private static void AddChristmasTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
        // https://www.realsimple.com/holidays-entertaining/holidays/christmas/ultimate-christmas-countdown-checklist
        // https://www.elizabethdhokia.com/100-things-to-do-this-christmas/#:~:text=%20100%20Things%20To%20Do%20This%20Christmas%20,Log%2010%20Wear%20a%20Santa%20hat%20More%20

        // Urlaub beantragen
        Task leave = new Task(context.getString(R.string.leave), context.getString(R.string.leaveDesc), addMonthDaysToJavaUtilDate(targetDate, 2, 0), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Bureaucracy), targetLocation);
        Task.addTask(leave);

        // Adventskalender
        Task adventCalendar = new Task(context.getString(R.string.adventCalendar), context.getString(R.string.adventCalendarDesc), addMonthDaysToJavaUtilDate(targetDate, -1, -7), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Bureaucracy), targetLocation);
        Task.addTask(adventCalendar);

        // Weihnachtskarten
        Task cards = new Task(context.getString(R.string.cards), context.getString(R.string.cardsDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Bureaucracy), targetLocation);
        Task.addTask(cards);

        // Geschenke
        Task giftList = new Task(context.getString(R.string.giftList), context.getString(R.string.giftListDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Presents), targetLocation);
        Task.addTask(giftList);

        // Geschenkpapier
        Task wrappingPaper = new Task(context.getString(R.string.wrappingPaper), context.getString(R.string.wrappingPaperDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Presents), targetLocation);
        Task.addTask(wrappingPaper);

        // Adventskranz
        Task adventWreath = new Task(context.getString(R.string.adventWreath), context.getString(R.string.adventWreathDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(adventWreath);

        // Deko
        Task decoration = new Task(context.getString(R.string.Decorate), context.getString(R.string.decorationDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(decoration);

        // Lichterketten
        Task fairyLights = new Task(context.getString(R.string.fairyLights), context.getString(R.string.fairyLightsDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(fairyLights);

        // Playlist
        Task playlist = new Task(context.getString(R.string.christmasPlaylist), context.getString(R.string.christmasPlaylistDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -25), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(playlist);

        // Kerzen
        Task candles = new Task(context.getString(R.string.candles), context.getString(R.string.candlesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -21), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(candles);

        // Batterien
        Task batteries = new Task(context.getString(R.string.batteries), context.getString(R.string.batteriesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -21), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Miscellaneous), targetLocation);
        Task.addTask(batteries);

        // Weihnachtsbaum
        Task tree = new Task(context.getString(R.string.tree), context.getString(R.string.treeDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(tree);

        // Weihnachtsbaumständer
        Task christmasTreeStand = new Task(context.getString(R.string.treeStand), context.getString(R.string.treeStandDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Decoration), targetLocation);
        Task.addTask(christmasTreeStand);

        // Weihnachtsessen
        Task roast = new Task(context.getString(R.string.roast), context.getString(R.string.roastDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -10), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Miscellaneous), targetLocation);
        Task.addTask(roast);

        // Getränke
        Task beverage = new Task(context.getString(R.string.beverage), context.getString(R.string.beverageDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -10), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Food), targetLocation);
        Task.addTask(beverage);

        // Last Minute
        Task lastMinute = new Task(context.getString(R.string.lastMinute), context.getString(R.string.lastMinuteDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Miscellaneous), targetLocation);
        Task.addTask(lastMinute);

        // Saubermachen
        Task clean = new Task(context.getString(R.string.houseCleaning), context.getString(R.string.cleanDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Miscellaneous), targetLocation);
        Task.addTask(clean);

        // Frische Lebensmittel
        Task freshIngredients = new Task(context.getString(R.string.freshIngredients), context.getString(R.string.freshIngredientsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -3), Priority.High, 0L,
                new TaskType(TaskTypeChristmas.Food), targetLocation);
        Task.addTask(freshIngredients);

        // Vorkochen
        Task precook = new Task(context.getString(R.string.precook), context.getString(R.string.precookDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -1), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Food), targetLocation);
        Task.addTask(precook);

        // Grinch
        Task grinch = new Task(context.getString(R.string.grinch), context.getString(R.string.grinchDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Entertainment), targetLocation);
        Task.addTask(grinch);

        // Spenden
        Task donate = new Task(context.getString(R.string.donate), context.getString(R.string.donateDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Entertainment), targetLocation);
        Task.addTask(donate);

        // Nussknacker
        Task nutcracker = new Task(context.getString(R.string.nutcracker), context.getString(R.string.nutcrackerDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeChristmas.Entertainment), targetLocation);
        Task.addTask(nutcracker);
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
