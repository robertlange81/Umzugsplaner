package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Location;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;
import com.planner.removal.removalplanner.Model.TaskTypeMain;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TaskInitializer {

    public static void InitTasks(Date targetDate, Location targetLocation, Activity mainActivity) {

        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
        today.setHours(0);
        today.setMinutes(0);
        Date tomorrow = addMonthDaysToJavaUtilDate(today, 0, 1);

        SharedPreferences prefs = MainActivity.instance.getSharedPreferences("removal", 0);

        if(prefs != null) {
            SharedPreferences.Editor editor = prefs.edit();

            if(targetDate != null) {
                editor.putLong("target_timestamp", targetDate.getTime());
                editor.apply();
            }

            if(targetLocation != null) {
                editor.putString(Location.PLACE, targetLocation.getPlace());
                editor.putString(Location.POSTAL, targetLocation.getPostal());
                editor.putString(Location.STREET, targetLocation.getStreet());
                editor.putString(Location.STREETNUMBER, targetLocation.getStreetNumber());
                editor.apply();
            }
        }

        // Umzugstermin selbst
        if(targetDate != null) {
            Task removal = new Task(mainActivity.getString(R.string.removalDate), mainActivity.getString(R.string.removalDateDesc), new Date(targetDate.getTime()), Priority.High, 0L,
                    new TaskType(TaskTypeMain.Movement), targetLocation);
            if(targetLocation != null) {
                removal.addLink(
                        mainActivity.getString(R.string.routeMap),
                        "https://www.google.co.in/maps/place/"
                                + targetLocation.getStreet()
                                + "+" + targetLocation.getStreetNumber()
                                + ",+" + targetLocation.getPostal()
                                + "+" + targetLocation.getPlace()
                );
            }
            Task.addTask(removal);

            targetDate.setHours(0);
            targetDate.setMinutes(0);
        }

        // Vertrag alt
        Task rentalContractOld = new Task(mainActivity.getString(R.string.taskRentalContractOld), (mainActivity.getString(R.string.taskRentalContractOldDesc) + ' ' + mainActivity.getString(R.string.contractCancel)), targetDate != null ? tomorrow : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()), targetLocation);
        rentalContractOld.addLink(mainActivity.getString(R.string.taskRentalContractOld3Months), mainActivity.getString(R.string.taskRentalContractOld3MonthsLINK));
        rentalContractOld.addLink(mainActivity.getString(R.string.rentalContractOldSpecialRightOfTermination), mainActivity.getString(R.string.rentalContractOldSpecialRightOfTerminationLINK));
        Task.addTask(rentalContractOld);

        // Vertrag neu
        Task rentalContractNew = new Task(mainActivity.getString(R.string.rentalContractNew), mainActivity.getString(R.string.rentalContractNewDesc), targetDate != null ? today : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()), targetLocation);
        rentalContractNew.addLink(mainActivity.getString(R.string.rentalContractNewTraps), mainActivity.getString(R.string.rentalContractNewTrapsLink));
        rentalContractNew.addLink(mainActivity.getString(R.string.rentalContractNewTips), mainActivity.getString(R.string.rentalContractNewTipsLINK));
        Task.addTask(rentalContractNew);

        // Vertragspartner
        Task informContractors = new Task(mainActivity.getString(R.string.informContractors), mainActivity.getString(R.string.informContractorsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        Task.addTask(informContractors);

        // Strom / Gas
        Task energyContract = new Task(mainActivity.getString(R.string.energyContract), mainActivity.getString(R.string.energyContractDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        energyContract.addLink(mainActivity.getString(R.string.compareEnergyContractVerivox), mainActivity.getString(R.string.energyContractVerivoxLINK));
        energyContract.addLink(mainActivity.getString(R.string.compareEnergyContractCheck24), mainActivity.getString(R.string.energyContractCheck24LINK));
        energyContract.addLink(mainActivity.getString(R.string.compareGasContractVerivox), mainActivity.getString(R.string.gasContractVerivoxLINK));
        energyContract.addLink(mainActivity.getString(R.string.compareGasContractCheck24), mainActivity.getString(R.string.gasContractCheck24LINK));
        Task.addTask(energyContract);

        // Internet / DSL
        Task internetContract = new Task(mainActivity.getString(R.string.internetContract), mainActivity.getString(R.string.internetContractDesc)  + ' ' + mainActivity.getString(R.string.contractCancel), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        internetContract.addLink(mainActivity.getString(R.string.internetContractAvailability), mainActivity.getString(R.string.internetContractAvailabilityLINK));
        internetContract.addLink(mainActivity.getString(R.string.compareContractCheck24), mainActivity.getString(R.string.internetContractCheck24LINK));
        internetContract.addLink(mainActivity.getString(R.string.compareContractVerivox), mainActivity.getString(R.string.internetContractVerivoxLINK));
        Task.addTask(internetContract);

        // Haftplficht
        Task liabilityInsurance = new Task(mainActivity.getString(R.string.liabilityInsurance), mainActivity.getString(R.string.liabilityInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        liabilityInsurance.addLink(mainActivity.getString(R.string.compareLiabilityInsurance), mainActivity.getString(R.string.compareLiabilityInsuranceVerivoxLINK));
        Task.addTask(liabilityInsurance);

        // Hausrat
        Task householdInsurance = new Task(mainActivity.getString(R.string.householdInsurance), mainActivity.getString(R.string.householdInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -10) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        householdInsurance.addLink(mainActivity.getString(R.string.compareContracts), mainActivity.getString(R.string.compareContractVerivoxLINK));
        Task.addTask(householdInsurance);

        // Brandschutz
        Task fireAndBurglaryProtection = new Task(mainActivity.getString(R.string.fireAndBurglaryProtection), mainActivity.getString(R.string.fireAndBurglaryProtectionDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -6) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
        fireAndBurglaryProtection.addLink(mainActivity.getString(R.string.fireAndBurglaryProtectionSmokeDetector), mainActivity.getString(R.string.fireAndBurglaryProtectionSmokeDetectorLINK));
        fireAndBurglaryProtection.addLink(mainActivity.getString(R.string.fireAndBurglaryProtectionBurglary), mainActivity.getString(R.string.fireAndBurglaryProtectionBurglaryLINKI));
        Task.addTask(fireAndBurglaryProtection);

        // Urlaub
        Task requestSpecialLeave = new Task(mainActivity.getString(R.string.requestSpecialLeave), mainActivity.getString(R.string.requestSpecialLeaveDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        requestSpecialLeave.addLink(mainActivity.getString(R.string.requestSpecialLeave), mainActivity.getString(R.string.requestSpecialLeaveLINK));
        Task.addTask(requestSpecialLeave);

        // Kautionskonto
        Task rentalDepositAccount = new Task(mainActivity.getString(R.string.rentalDepositAccount), mainActivity.getString(R.string.rentalDepositAccountDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -30) : null , Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        rentalDepositAccount.addLink(mainActivity.getString(R.string.rentalDepositAccountExplanation), mainActivity.getString(R.string.rentalDepositAccountExplanationLINK));
        Task.addTask(rentalDepositAccount);

        // Umzugskartons / Müllsäcke
        Task packingMaterial = new Task(mainActivity.getString(R.string.packingMaterial), mainActivity.getString(R.string.packingMaterialDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        packingMaterial.addLink(mainActivity.getString(R.string.packingMaterialMovingBoxes), mainActivity.getString(R.string.packingMaterialMovingBoxesEbayLINK));
        packingMaterial.addLink(mainActivity.getString(R.string.packingMaterialTrashBags), mainActivity.getString(R.string.packingMaterialTrashBagsAmazonLINK));
        packingMaterial.addLink(mainActivity.getString(R.string.packingMaterialAirCushionFoil), mainActivity.getString(R.string.packingMaterialAirCushionFoilAmazonLINK));
        Task.addTask(packingMaterial);

        // Umzugshelfer
        Task organizeHelper = new Task(mainActivity.getString(R.string.organizeHelper), mainActivity.getString(R.string.organizeHelperDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        organizeHelper.addLink(mainActivity.getString(R.string.organizeHelperCarryingStraps), mainActivity.getString(R.string.organizeHelperCarryingStrapsEbayLINK));
        organizeHelper.addLink(mainActivity.getString(R.string.organizeHelperhandTruck), mainActivity.getString(R.string.organizeHelperHandTrucksEbayLINK));
        organizeHelper.addLink(mainActivity.getString(R.string.organizeHelperWorkGloves), mainActivity.getString(R.string.organizeHelperWorkGlovesAmazonLink));
        Task.addTask(organizeHelper);

        // Umzugswagen
        Task movingVan = new Task(mainActivity.getString(R.string.movingVan), mainActivity.getString(R.string.movingVanDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        movingVan.addLink(mainActivity.getString(R.string.movingVanCompare), mainActivity.getString(R.string.movingVanCheck24LINK));
        Task.addTask(movingVan);

        // Schlüsselübergabe
        Task handoverNewFlat = new Task(mainActivity.getString(R.string.handoverNewFlat), mainActivity.getString(R.string.handoverNewFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -3) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
        if(targetLocation != null) {
            handoverNewFlat.addLink(
                    mainActivity.getString(R.string.routeMap),
                    "https://www.google.co.in/maps/place/"
                            + targetLocation.getStreet()
                            + "+" + targetLocation.getStreetNumber()
                            + ",+" + targetLocation.getPostal()
                            + "+" + targetLocation.getPlace()
            );
        }
        Task.addTask(handoverNewFlat);

        // Parkplatz absperren
        Task cordonOffParkingLot = new Task(mainActivity.getString(R.string.cordonOffParkingLot), mainActivity.getString(R.string.cordonOffParkingLotDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -3) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        cordonOffParkingLot.addLink(mainActivity.getString(R.string.cordonOffParkingLotHowTo), mainActivity.getString(R.string.cordonOffParkingLotHowToLINK));
        Task.addTask(cordonOffParkingLot);

        // Vortag
        Task prevDay = new Task(mainActivity.getString(R.string.previousDay), mainActivity.getString(R.string.previousDayDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        Task.addTask(prevDay);

        // Malern - Schönheitsreparaturen
        Task renovateOldFlat = new Task(mainActivity.getString(R.string.renovateOldFlat), mainActivity.getString(R.string.renovateOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 3) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        renovateOldFlat.addLink(mainActivity.getString(R.string.renovationObligation), mainActivity.getString(R.string.renovationObligationLINK));
        Task.addTask(renovateOldFlat);

        // Wohnungsabnahme
        Task handoverOldFlat = new Task(mainActivity.getString(R.string.handoverOldFlat), mainActivity.getString(R.string.handoverOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        Task.addTask(handoverOldFlat);

        // Nachbarn
        Task informOldAndNewNeighbours = new Task(mainActivity.getString(R.string.informOldAndNewNeighbours), mainActivity.getString(R.string.informOldAndNewNeighboursDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        // TODO partner-link
        informOldAndNewNeighbours.addLink(mainActivity.getString(R.string.informOldAndNewNeighboursDoorSigns), mainActivity.getString(R.string.informOldAndNewNeighboursDoorSignsLINK));
        Task.addTask(informOldAndNewNeighbours);

        // Ummeldung
        Task informAuthorities = new Task(mainActivity.getString(R.string.informAuthorities), mainActivity.getString(R.string.informAuthoritiesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 13), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement.getValue()), targetLocation);
        informAuthorities.addLink(mainActivity.getString(R.string.informAuthoritiesResidence), mainActivity.getString(R.string.informAuthoritiesResidenceLINK));
        informAuthorities.addLink(mainActivity.getString(R.string.informAuthoritiesCar), mainActivity.getString(R.string.informAuthoritiesCarLINK));
        Task.addTask(informAuthorities);

        // Nachsendeauftrag
        Task postalAftermath = new Task(mainActivity.getString(R.string.postalAftermath), mainActivity.getString(R.string.postalAftermathDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat.getValue()), targetLocation);
        postalAftermath.addLink(mainActivity.getString(R.string.postalAftermathSetup), mainActivity.getString(R.string.postalAftermathLINK));
        Task.addTask(postalAftermath);

        // Steuer
        Task preserveArtisanBills = new Task(mainActivity.getString(R.string.preserveArtisanBills), mainActivity.getString(R.string.preserveArtisanBillsDesc), getLastDayInYearOf(today), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        preserveArtisanBills.addLink(mainActivity.getString(R.string.preserveArtisanBillsTip), mainActivity.getString(R.string.preserveArtisanBillsLINK));
        Task.addTask(preserveArtisanBills);

        // Sperrmüll
        Task bulkyWaste = new Task(mainActivity.getString(R.string.bulkyWaste), mainActivity.getString(R.string.bulkyWasteDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        Task.addTask(bulkyWaste);

        // alte Kaution / Nebenkostenabrechnung
        Task oldRentDeposit = new Task(mainActivity.getString(R.string.oldRentDeposit), mainActivity.getString(R.string.oldRentDepositDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 60) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        oldRentDeposit.addLink(mainActivity.getString(R.string.oldRentDepositHowItWorks), mainActivity.getString(R.string.oldRentDepositHowItWorksLINK));
        Task.addTask(oldRentDeposit);

        // Einweihungsfeier
        Task party = new Task(mainActivity.getString(R.string.party), mainActivity.getString(R.string.partyDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 30) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
        party.addLink(mainActivity.getString(R.string.partyEbay), mainActivity.getString(R.string.partyEbayLINK));
        Task.addTask(party);

        Persistance.SaveTasks(mainActivity);
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
