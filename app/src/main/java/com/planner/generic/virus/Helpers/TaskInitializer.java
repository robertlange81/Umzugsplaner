package com.planner.generic.virus.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.planner.generic.virus.Model.Location;
import com.planner.generic.virus.Model.Priority;
import com.planner.generic.virus.Model.Task;
import com.planner.generic.virus.Model.TaskType;
import com.planner.generic.virus.Model.TaskTypeLockdown;
import com.planner.generic.virus.Model.TaskTypeRelocation;
import com.planner.generic.virus.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TaskInitializer {

    public static ListType CURRENT_LIST_TYPE = ListType.LOCKDOWN;

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

        // Desinfektionsmittel
        Task disinfectant = new Task(context.getString(R.string.disinfectant), context.getString(R.string.disinfectant_desc), addMonthDaysToJavaUtilDate(targetDate, 0, 1), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(disinfectant);

        // FFP3-Masken
        Task respiratoryProtection = new Task(context.getString(R.string.Respirators), context.getString(R.string.Respirators_desc), addMonthDaysToJavaUtilDate(targetDate, 0, 1), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(respiratoryProtection);

        // Verbandskasten
        Task medicalKit = new Task(context.getString(R.string.medicalkit), context.getString(R.string.firstAidKitDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(medicalKit);

        // Vom Arzt verschriebene Medikamente
        Task medication = new Task(context.getString(R.string.medication), context.getString(R.string.medicationDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(medication);

        // Schmerzmittel
        Task painReliever = new Task(context.getString(R.string.painReliever), context.getString(R.string.painRelieverDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(painReliever);

        // Mittel gegen Durchfall
        Task DiarrheaRemedy = new Task(context.getString(R.string.DiarrheaRemedy), context.getString(R.string.DiarrheaRemedy), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(DiarrheaRemedy);

        // Erkältungsmittel
        Task coldRemedies = new Task(context.getString(R.string.coldRemedies), context.getString(R.string.coldRemediesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 4), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(coldRemedies);

        // Fieberthermometer
        Task clinicalThermometer = new Task(context.getString(R.string.clinicalThermometer), context.getString(R.string.clinicalThermometerDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MEDICIN), targetLocation);
        Task.addTask(clinicalThermometer);

        // Wasser
        Task water = new Task(context.getString(R.string.water), context.getString(R.string.waterDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.HOUSEHOLD), targetLocation);
        Task.addTask(water);

        // Fertiggerichte
        Task readyMeals = new Task(context.getString(R.string.readyMeals), context.getString(R.string.readyMealsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(readyMeals);

        // Taschenlampe
        Task pocketLamp = new Task(context.getString(R.string.pocketLamp), context.getString(R.string.pocketLampDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.ELECTRONICS), targetLocation);
        Task.addTask(pocketLamp);

        // Batterien
        Task batteries = new Task(context.getString(R.string.batteries), context.getString(R.string.batteriesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.ELECTRONICS), targetLocation);
        Task.addTask(batteries);

        // Kartoffeln
        Task potatoes = new Task(context.getString(R.string.potatoes), context.getString(R.string.potatoesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(potatoes);

        // Treibstoff
        Task fuel = new Task(context.getString(R.string.fuel), context.getString(R.string.fuelDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.MISCELLANEOUS), targetLocation);
        Task.addTask(fuel);

        // Schlafsack
        Task sleepingBag = new Task(context.getString(R.string.sleepingBag), context.getString(R.string.sleepingBagDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.MISCELLANEOUS), targetLocation);
        Task.addTask(sleepingBag);

        // wasserfeste Kleidung
        Task waterproofClothing = new Task(context.getString(R.string.waterproofClothing), context.getString(R.string.waterproofClothingDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.MISCELLANEOUS), targetLocation);
        Task.addTask(waterproofClothing);

        // Gemüse
        Task vegetables = new Task(context.getString(R.string.vegetables), context.getString(R.string.vegetablesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(vegetables);

        // Obst
        Task fruit = new Task(context.getString(R.string.fruit), context.getString(R.string.fruitDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(fruit);

        // Milch
        Task milk = new Task(context.getString(R.string.milk), context.getString(R.string.milkDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(milk);

        // Fett und Öl
        Task fatOil = new Task(context.getString(R.string.fatOil), context.getString(R.string.fatOilDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 21), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(fatOil);

        // Gewürze
        Task spices = new Task(context.getString(R.string.spices), context.getString(R.string.spicesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 21), Priority.Normal, 0L,
                new TaskType(TaskTypeLockdown.FOOD), targetLocation);
        Task.addTask(spices);

        // Gaspistole
        Task pistol = new Task(context.getString(R.string.gasPisol), context.getString(R.string.gasPistolDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 10), Priority.High, 0L,
                new TaskType(TaskTypeLockdown.MISCELLANEOUS), targetLocation);
        Task.addTask(pistol);
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
