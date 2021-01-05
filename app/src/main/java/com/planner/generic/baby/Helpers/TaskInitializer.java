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

        /* Vorlage
        https://www.missmum.at/10-dinge-die-dein-baby-am-anfang-wirklich-braucht/?cn-reloaded=1
        https://www.gofeminin.de/shopping/babyausstattung-s1850868.html
        Task xxx = new Task("", "", addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Literature), null);
        xxx.addLink("", "");
        Task.addTask(xxx);
        */

        // Service

        // Die Hebammensprechstunde
        Task midwife = new Task(context.getString(R.string.midwife), context.getString(R.string.midwifeDesc), today, Priority.High, 0L,
                new TaskType(TaskTypeBaby.Literature), null);
        midwife.addLink(context.getString(R.string.midwifeLinkName), context.getString(R.string.midwifeLinkHref));
        Task.addTask(midwife);

        // Ultraschall-Untersuchung
        Task ultrasoundExam = new Task(context.getString(R.string.ultrasoundExam), context.getString(R.string.ultrasoundExamDesc), addMonthDaysToJavaUtilDate(targetDate, -6, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        ultrasoundExam.addLink(context.getString(R.string.ultrasoundExamLinkName), context.getString(R.string.ultrasoundExamLinkHref));
        Task.addTask(ultrasoundExam);

        // Krippenplatz
        Task dayNursery = new Task(context.getString(R.string.dayNursery), context.getString(R.string.dayNurseryDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        dayNursery.addLink(context.getString(R.string.dayNurseryApplyLinkName), context.getString(R.string.dayNurseryApplyLinkHref));
        dayNursery.addLink(context.getString(R.string.dayNurseryOverviewLinkName), context.getString(R.string.dayNurseryOverviewLinkHref));
        Task.addTask(dayNursery);

        // Elternzeit
        Task parentalLeave = new Task(context.getString(R.string.parentalLeave), context.getString(R.string.parentalLeaveDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        parentalLeave.addLink(context.getString(R.string.parentalLeaveLinkName), context.getString(R.string.parentalLeaveLinkHref));
        Task.addTask(parentalLeave);

        // Wickeldecke und Schlafsack
        Task swaddle = new Task(context.getString(R.string.Bedding), context.getString(R.string.BeddingDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Clothing), null);
        swaddle.addLink(context.getString(R.string.SwaddleBlanketLinkName), context.getString(R.string.SwaddleBlanketLinkHref));
        swaddle.addLink(context.getString(R.string.SleepingBagLinkName), context.getString(R.string.SleepingBagLinkHref));
        Task.addTask(swaddle);

        // Kinderbett
        Task babyCrib = new Task(context.getString(R.string.babyCrib), context.getString(R.string.babyCribDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 15), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Furnishings), null);
        babyCrib.addLink(context.getString(R.string.babyCribLinkOverallName), context.getString(R.string.babyCribLinkOverallHref));
        babyCrib.addLink(context.getString(R.string.babyCribLinkTravelName), context.getString(R.string.babyCribLinkTravelHref));
        Task.addTask(babyCrib);

        // Kinderwagen
        Task stroller = new Task(context.getString(R.string.Stroller), context.getString(R.string.StrollerDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Transport), null);
        stroller.addLink(context.getString(R.string.StrollerLinkNewbornName), context.getString(R.string.StrollerLinkNewbornNameHref));
        stroller.addLink(context.getString(R.string.StrollerLinkTwinsName), context.getString(R.string.StrollerLinkTwinsHref));
        stroller.addLink(context.getString(R.string.StrollerLinkEbayName), context.getString(R.string.StrollerLinkEbayHref));
        Task.addTask(stroller);

        // Babyschale
        Task carSeat = new Task(context.getString(R.string.CarSeat), context.getString(R.string.CarSeatDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Transport), null);
        Task.addTask(carSeat);

        // Manduka / Baby Carrier
        Task babyCarrier = new Task(context.getString(R.string.babyCarrier), context.getString(R.string.babyCarrierDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Transport), null);
        babyCarrier.addLink(context.getString(R.string.babyCarrierLinkWrapName), context.getString(R.string.babyCarrierLinkWrapHref));
        babyCarrier.addLink(context.getString(R.string.babyCarrierLinkStructuredName), context.getString(R.string.babyCarrierLinkStructuredHref));
        babyCarrier.addLink(context.getString(R.string.babyCarrierLinkInsertName), context.getString(R.string.babyCarrierLinkInsertHref));
        Task.addTask(babyCarrier);

        // Badewanne
        Task bathtub = new Task(context.getString(R.string.bathtub), context.getString(R.string.bathtubDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Furnishings), null);
        Task.addTask(bathtub);

        // Pflegeprodukte
        Task careProducts = new Task(context.getString(R.string.careProducts), context.getString(R.string.careProductsDesc), addMonthDaysToJavaUtilDate(targetDate, -4, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Drugs), null);
        careProducts.addLink(context.getString(R.string.careProductsPowderLinkName), context.getString(R.string.careProductsPowderLinkHref));
        careProducts.addLink(context.getString(R.string.careProductsDiaperBalmLinkName), context.getString(R.string.careProductsDiaperBalmLinkHref));
        careProducts.addLink(context.getString(R.string.careProductsBodyCreamLinkName), context.getString(R.string.careProductsBodyCreamLinkHref));
        careProducts.addLink(context.getString(R.string.careProductsShampooLinkName), context.getString(R.string.careProductsShampooLinkHref));
        careProducts.addLink(context.getString(R.string.careProductsSuncreamLinkName), context.getString(R.string.careProductsSuncreamLinkHref));
        careProducts.addLink(context.getString(R.string.careProductsWipesLinkName), context.getString(R.string.careProductsWipesLinkHref));
        Task.addTask(careProducts);

        // Mutterschutz
        Task maternityLeave = new Task(context.getString(R.string.maternityLeave), context.getString(R.string.maternityLeaveDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -42), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        maternityLeave.addLink(context.getString(R.string.maternityLeaveLinkName), context.getString(R.string.maternityLeaveLinkHref));
        Task.addTask(maternityLeave);

        // Wärmelampe
        Task heatLamp = new Task(context.getString(R.string.heatLamp), context.getString(R.string.heatLampDesc), addMonthDaysToJavaUtilDate(targetDate, -4, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Furnishings), null);
        heatLamp.addLink(context.getString(R.string.heatLampLinkName), context.getString(R.string.heatLampLinkHref));
        Task.addTask(heatLamp);

        // Strampler
        Task onesie = new Task(context.getString(R.string.Onesies), context.getString(R.string.OnesiesDesc), addMonthDaysToJavaUtilDate(targetDate, -3, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Clothing), null);
        onesie.addLink(context.getString(R.string.OnesiesLinkNameLong), context.getString(R.string.OnesiesLinkNameLongHref));
        onesie.addLink(context.getString(R.string.OnesiesLinkNameShort), context.getString(R.string.OnesiesLinkNameShortHref));
        Task.addTask(onesie);

        // Nuckel
        Task pacifiers = new Task(context.getString(R.string.pacifiers), context.getString(R.string.pacifiersDesc), addMonthDaysToJavaUtilDate(targetDate, -3, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Toys), null);
        Task.addTask(pacifiers);

        // Thermometer
        Task thermometer = new Task(context.getString(R.string.thermometer), context.getString(R.string.thermometerDesc), addMonthDaysToJavaUtilDate(targetDate, -3, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Drugs), null);
        thermometer.addLink(context.getString(R.string.thermometerRectalLinkName), context.getString(R.string.thermometerRectalLinkHref));
        thermometer.addLink(context.getString(R.string.thermometerTouchlessLinkName), context.getString(R.string.thermometerTouchlessLinkHref));
        Task.addTask(thermometer);

        // Stillzubehör
        Task feedingSupplies = new Task(context.getString(R.string.feeding), context.getString(R.string.feedingDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Food), null);
        feedingSupplies.addLink(context.getString(R.string.feedingBibsLinkName), context.getString(R.string.feedingBibsLinkHref));
        feedingSupplies.addLink(context.getString(R.string.feedingBottlesLinkName), context.getString(R.string.feedingBottlesLinkHref));
        feedingSupplies.addLink(context.getString(R.string.feedingSterilizerLinkName), context.getString(R.string.feedingSterilizerLinkHref));
        feedingSupplies.addLink(context.getString(R.string.feedingBottleWarmerLinkName), context.getString(R.string.feedingBottleWarmerLinkHref));
        feedingSupplies.addLink(context.getString(R.string.feedingBreastPumpLinkName), context.getString(R.string.feedingBreastPumpLinkHref));
        Task.addTask(feedingSupplies);

        // Milchpulver
        Task formula = new Task(context.getString(R.string.formula), context.getString(R.string.formulaDesc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Food), null);
        formula.addLink(context.getString(R.string.formulaBestLinkName), context.getString(R.string.formulaBestLinkHref));
        formula.addLink(context.getString(R.string.formulaAllergicLinkName), context.getString(R.string.formulaAllergicLinkHref));
        Task.addTask(formula);

        // Spielzeug
        Task basicToys = new Task(context.getString(R.string.toys), context.getString(R.string.toysDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Toys), null);
        basicToys.addLink(context.getString(R.string.toysRattleLinkName), context.getString(R.string.toysRattleLinkHref));
        basicToys.addLink(context.getString(R.string.toysBookLinkName), context.getString(R.string.toysBookLinkHref));
        basicToys.addLink(context.getString(R.string.toysSootherLinkName), context.getString(R.string.toysSootherLinkHref));
        Task.addTask(basicToys);

        // Spielzeug+
        Task motorToys = new Task(context.getString(R.string.motoricToys), context.getString(R.string.motoricToysDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Toys), null);
        motorToys.addLink(context.getString(R.string.mororicToysObalLinkName), context.getString(R.string.mororicToysObalLinkHref));
        motorToys.addLink(context.getString(R.string.mororicToysSkwishLinkName), context.getString(R.string.mororicToysSkwishLinkHref));
        Task.addTask(motorToys);

        // Windeln, Feuchttücher, Wickelunterlagen
        Task diapers = new Task(context.getString(R.string.diapers), context.getString(R.string.diapersDesc), addMonthDaysToJavaUtilDate(targetDate, -1, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Drugs), null);
        diapers.addLink(context.getString(R.string.diapersLink1Name), context.getString(R.string.diapersLink1Href));
        diapers.addLink(context.getString(R.string.diapersLink2Name), context.getString(R.string.diapersLink2Href));
        diapers.addLink(context.getString(R.string.diapersLink3Name), context.getString(R.string.diapersLink3Href));
        Task.addTask(diapers);

        // Vitamin Öl
        Task d3 = new Task(context.getString(R.string.VitaminD3), context.getString(R.string.VitaminD3Desc), addMonthDaysToJavaUtilDate(targetDate, -2, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Drugs), null);
        Task.addTask(d3);

        // Namen finden
        Task naming = new Task(context.getString(R.string.findName), context.getString(R.string.findNameDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        naming.addLink(context.getString(R.string.findGirlsNameLinkName), context.getString(R.string.findGirlsNameLinkHref));
        naming.addLink(context.getString(R.string.findBoysNameLinkName), context.getString(R.string.findBoysNameLinkHref));
        naming.addLink(context.getString(R.string.NameGeneratorLinkName), context.getString(R.string.NameGeneratorLinkHref));
        Task.addTask(naming);

        // Geburtsurkunde
        Task birthCert = new Task(context.getString(R.string.BirthCertificate), context.getString(R.string.BirthCertificateDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.High, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        birthCert.addLink(context.getString(R.string.BirthCertificateLinkName), context.getString(R.string.BirthCertificateLinkHref));
        Task.addTask(birthCert);

        // Patenonkel / Patentante
        Task godparent = new Task(context.getString(R.string.godparent), context.getString(R.string.godparentDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeBaby.Bureaucracy), null);
        godparent.addLink(context.getString(R.string.godparentLinkName), context.getString(R.string.godparentLinkHref));
        Task.addTask(godparent);
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
