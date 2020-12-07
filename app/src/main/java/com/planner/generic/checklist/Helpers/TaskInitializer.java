package com.planner.generic.checklist.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.planner.generic.checklist.Model.Location;
import com.planner.generic.checklist.Model.Priority;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.Model.TaskType;
import com.planner.generic.checklist.Model.TaskTypeLockdown;
import com.planner.generic.checklist.Model.TaskTypeRelocation;
import com.planner.generic.checklist.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TaskInitializer {

    public static ListType CURRENT_LIST_TYPE = ListType.RELOCATION;

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

    private static void AddLockdownTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {}

    private static void AddRemovalTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
        // Vertrag alt
        Task rentalContractOld = new Task(context.getString(R.string.taskRentalContractOld), (context.getString(R.string.taskRentalContractOldDesc) + ' ' + context.getString(R.string.contractCancel)), targetDate != null ? tomorrow : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts.getValue()), targetLocation);
        rentalContractOld.addLink(context.getString(R.string.taskRentalContractOld3Months), context.getString(R.string.taskRentalContractOld3MonthsLINK));
        rentalContractOld.addLink(context.getString(R.string.rentalContractOldSpecialRightOfTermination), context.getString(R.string.rentalContractOldSpecialRightOfTerminationLINK));
        Task.addTask(rentalContractOld);

        // Vertrag neu
        Task rentalContractNew = new Task(context.getString(R.string.rentalContractNew), context.getString(R.string.rentalContractNewDesc), targetDate != null ? today : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts.getValue()), targetLocation);
        rentalContractNew.addLink(context.getString(R.string.rentalContractNewTips), context.getString(R.string.rentalContractNewTipsLINK));
        rentalContractNew.addLink(context.getString(R.string.rentalContractNewTraps), context.getString(R.string.rentalContractNewTrapsLink));
        Task.addTask(rentalContractNew);

        // Vertragspartner
        Task informContractors = new Task(context.getString(R.string.informContractors), context.getString(R.string.informContractorsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        Task.addTask(informContractors);

        // Strom / Gas
        Task energyContract = new Task(context.getString(R.string.energyContract), context.getString(R.string.energyContractDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        energyContract.addLink(context.getString(R.string.compareEnergyContractVerivox), context.getString(R.string.energyContractVerivoxLINK));
        energyContract.addLink(context.getString(R.string.compareEnergyContractCheck24), context.getString(R.string.energyContractCheck24LINK));
        energyContract.addLink(context.getString(R.string.compareGasContractVerivox), context.getString(R.string.gasContractVerivoxLINK));
        energyContract.addLink(context.getString(R.string.compareGasContractCheck24), context.getString(R.string.gasContractCheck24LINK));
        Task.addTask(energyContract);

        // Internet / DSL
        Task internetContract = new Task(context.getString(R.string.internetContract), context.getString(R.string.internetContractDesc)  + ' ' + context.getString(R.string.contractCancel), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        internetContract.addLink(context.getString(R.string.internetContractAvailability), context.getString(R.string.internetContractAvailabilityLINK));
        internetContract.addLink(context.getString(R.string.compareContractCheck24), context.getString(R.string.internetContractCheck24LINK));
        internetContract.addLink(context.getString(R.string.compareContractVerivox), context.getString(R.string.internetContractVerivoxLINK));
        Task.addTask(internetContract);

        // Haftpflicht
        Task liabilityInsurance = new Task(context.getString(R.string.liabilityInsurance), context.getString(R.string.liabilityInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        liabilityInsurance.addLink(context.getString(R.string.compareLiabilityInsurance), context.getString(R.string.compareLiabilityInsuranceVerivoxLINK));
        Task.addTask(liabilityInsurance);

        // Hausrat
        Task householdInsurance = new Task(context.getString(R.string.householdInsurance), context.getString(R.string.householdInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -10) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        householdInsurance.addLink(context.getString(R.string.compareContracts), context.getString(R.string.compareContractVerivoxLINK));
        Task.addTask(householdInsurance);

        // Brandschutz
        Task fireAndBurglaryProtection = new Task(context.getString(R.string.fireAndBurglaryProtection), context.getString(R.string.fireAndBurglaryProtectionDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -6) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.NewFlat), targetLocation);
        fireAndBurglaryProtection.addLink(context.getString(R.string.fireAndBurglaryProtectionSmokeDetector), context.getString(R.string.fireAndBurglaryProtectionSmokeDetectorLINK));
        fireAndBurglaryProtection.addLink(context.getString(R.string.fireAndBurglaryProtectionBurglary), context.getString(R.string.fireAndBurglaryProtectionBurglaryLINKI));
        Task.addTask(fireAndBurglaryProtection);

        // Urlaub
        Task requestSpecialLeave = new Task(context.getString(R.string.requestSpecialLeave), context.getString(R.string.requestSpecialLeaveDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        requestSpecialLeave.addLink(context.getString(R.string.requestSpecialLeave), context.getString(R.string.requestSpecialLeaveLINK));
        Task.addTask(requestSpecialLeave);

        // Kautionskonto
        Task rentalDepositAccount = new Task(context.getString(R.string.rentalDepositAccount), context.getString(R.string.rentalDepositAccountDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -30) : null , Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.Contracts), targetLocation);
        rentalDepositAccount.addLink(context.getString(R.string.rentalDepositAccountExplanation), context.getString(R.string.rentalDepositAccountExplanationLINK));
        Task.addTask(rentalDepositAccount);

        // Umzugskartons / Müllsäcke
        Task packingMaterial = new Task(context.getString(R.string.packingMaterial), context.getString(R.string.packingMaterialDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        packingMaterial.addLink(context.getString(R.string.packingMaterialTrashBags), context.getString(R.string.packingMaterialTrashBagsAmazonLINK));
        packingMaterial.addLink(context.getString(R.string.packingMaterialAirCushionFoil), context.getString(R.string.packingMaterialAirCushionFoilAmazonLINK));
        packingMaterial.addLink(context.getString(R.string.packingMaterialMovingBoxes), context.getString(R.string.packingMaterialMovingBoxesEbayLINK));
        Task.addTask(packingMaterial);

        // Umzugshelfer
        Task organizeHelper = new Task(context.getString(R.string.organizeHelper), context.getString(R.string.organizeHelperDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        organizeHelper.addLink(context.getString(R.string.organizeHelperWorkGloves), context.getString(R.string.organizeHelperWorkGlovesAmazonLink));
        organizeHelper.addLink(context.getString(R.string.organizeHelperCarryingStraps), context.getString(R.string.organizeHelperCarryingStrapsEbayLINK));
        organizeHelper.addLink(context.getString(R.string.organizeHelperhandTruck), context.getString(R.string.organizeHelperHandTrucksEbayLINK));
        Task.addTask(organizeHelper);

        // Umzugswagen
        Task movingVan = new Task(context.getString(R.string.movingVan), context.getString(R.string.movingVanDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        movingVan.addLink(context.getString(R.string.movingVanCompare), context.getString(R.string.movingVanCheck24LINK));
        Task.addTask(movingVan);

        // Schlüsselübergabe
        Task handoverNewFlat = new Task(context.getString(R.string.handoverNewFlat), context.getString(R.string.handoverNewFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -3) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.NewFlat), targetLocation);
        if(targetLocation != null) {
            handoverNewFlat.addLink(
                    context.getString(R.string.routeMap),
                    "https://www.google.co.in/maps/place/"
                            + targetLocation.getStreet()
                            + "+" + targetLocation.getStreetNumber()
                            + ",+" + targetLocation.getPostal()
                            + "+" + targetLocation.getPlace()
            );
        }
        Task.addTask(handoverNewFlat);

        // Parkplatz absperren
        Task cordonOffParkingLot = new Task(context.getString(R.string.cordonOffParkingLot), context.getString(R.string.cordonOffParkingLotDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -3) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        cordonOffParkingLot.addLink(context.getString(R.string.cordonOffParkingLotHowTo), context.getString(R.string.cordonOffParkingLotHowToLINK));
        Task.addTask(cordonOffParkingLot);

        // Vortag
        Task prevDay = new Task(context.getString(R.string.previousDay), context.getString(R.string.previousDayDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        Task.addTask(prevDay);

        // Malern - Schönheitsreparaturen
        Task renovateOldFlat = new Task(context.getString(R.string.renovateOldFlat), context.getString(R.string.renovateOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 3) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.OldFlat), targetLocation);
        renovateOldFlat.addLink(context.getString(R.string.renovationObligation), context.getString(R.string.renovationObligationLINK));
        Task.addTask(renovateOldFlat);

        // Wohnungsabnahme
        Task handoverOldFlat = new Task(context.getString(R.string.handoverOldFlat), context.getString(R.string.handoverOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.High, 0L,
                new TaskType(TaskTypeRelocation.OldFlat), targetLocation);
        Task.addTask(handoverOldFlat);

        // Nachbarn
        Task informOldAndNewNeighbours = new Task(context.getString(R.string.informOldAndNewNeighbours), context.getString(R.string.informOldAndNewNeighboursDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        // TODO partner-link
        informOldAndNewNeighbours.addLink(context.getString(R.string.informOldAndNewNeighboursDoorSigns), context.getString(R.string.informOldAndNewNeighboursDoorSignsLINK));
        Task.addTask(informOldAndNewNeighbours);

        // Ummeldung
        Task informAuthorities = new Task(context.getString(R.string.informAuthorities), context.getString(R.string.informAuthoritiesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 13), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.Movement.getValue()), targetLocation);
        informAuthorities.addLink(context.getString(R.string.informAuthoritiesResidence), context.getString(R.string.informAuthoritiesResidenceLINK));
        informAuthorities.addLink(context.getString(R.string.informAuthoritiesCar), context.getString(R.string.informAuthoritiesCarLINK));
        Task.addTask(informAuthorities);

        // Nachsendeauftrag
        Task postalAftermath = new Task(context.getString(R.string.postalAftermath), context.getString(R.string.postalAftermathDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.High, 0L,
                new TaskType(TaskTypeRelocation.OldFlat.getValue()), targetLocation);
        postalAftermath.addLink(context.getString(R.string.postalAftermathSetup), context.getString(R.string.postalAftermathLINK));
        Task.addTask(postalAftermath);

        // Steuer
        Task preserveArtisanBills = new Task(context.getString(R.string.preserveArtisanBills), context.getString(R.string.preserveArtisanBillsDesc), getLastDayInYearOf(today), Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.Movement), targetLocation);
        preserveArtisanBills.addLink(context.getString(R.string.preserveArtisanBillsTip), context.getString(R.string.preserveArtisanBillsLINK));
        Task.addTask(preserveArtisanBills);

        // Sperrmüll
        Task bulkyWaste = new Task(context.getString(R.string.bulkyWaste), context.getString(R.string.bulkyWasteDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.OldFlat), targetLocation);
        Task.addTask(bulkyWaste);

        // alte Kaution / Nebenkostenabrechnung
        Task oldRentDeposit = new Task(context.getString(R.string.oldRentDeposit), context.getString(R.string.oldRentDepositDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 60) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.OldFlat), targetLocation);
        oldRentDeposit.addLink(context.getString(R.string.oldRentDepositHowItWorks), context.getString(R.string.oldRentDepositHowItWorksLINK));
        Task.addTask(oldRentDeposit);

        // Einweihungsfeier
        Task party = new Task(context.getString(R.string.party), context.getString(R.string.partyDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 30) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeRelocation.NewFlat), targetLocation);
        party.addLink(context.getString(R.string.partyEbay), context.getString(R.string.partyEbayLINK));
        Task.addTask(party);
    }

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
