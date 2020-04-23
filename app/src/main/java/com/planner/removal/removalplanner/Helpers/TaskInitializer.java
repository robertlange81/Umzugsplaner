package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;

import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;
import com.planner.removal.removalplanner.Model.TaskTypeMain;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Date;

public class TaskInitializer {

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {
        if(!isInitialized) {

        }
    }

    public static void InitTasks(Date removalDate, Activity mainActivity) {
        // FIXME Notizen in Task
        // FIXME init new task serial ; Mietkautionskonto; Verträge; Bank etc. neue Adresse mitteilen

        Date today = Calendar.getInstance().getTime();
        today.setHours(0);
        today.setMinutes(0);
        Date tomorrow = addMonthDaysToJavaUtilDate(today, 0, 13);
        Task rentalContractOld = new Task(mainActivity.getString(R.string.taskRentalContractOld), (mainActivity.getString(R.string.taskRentalContractOldDesc) + ' ' + mainActivity.getString(R.string.contractCancel)), removalDate != null ? tomorrow : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()));
        rentalContractOld.addLink(mainActivity.getString(R.string.taskRentalContractOld3Months), mainActivity.getString(R.string.taskRentalContractOld3MonthsLINK));
        rentalContractOld.addLink(mainActivity.getString(R.string.rentalContractOldSpecialRightOfTermination), mainActivity.getString(R.string.rentalContractOldSpecialRightOfTerminationLINK));
        Task.addTask(rentalContractOld);

        Task rentalContractNew = new Task(mainActivity.getString(R.string.rentalContractNew), mainActivity.getString(R.string.rentalContractNewDesc), removalDate != null ? today : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()));
        rentalContractNew.addLink(mainActivity.getString(R.string.rentalContractNewTraps), mainActivity.getString(R.string.rentalContractNewTrapsLink));
        rentalContractNew.addLink(mainActivity.getString(R.string.rentalContractNewTips), mainActivity.getString(R.string.rentalContractNewTipsLINK));
        Task.addTask(rentalContractNew);

        Task informOldAndNewNeighbours = new Task(mainActivity.getString(R.string.informOldAndNewNeighbours), mainActivity.getString(R.string.informOldAndNewNeighboursDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -7) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        // TODO partner-link
        rentalContractNew.addLink(mainActivity.getString(R.string.informOldAndNewNeighboursDoorSigns), mainActivity.getString(R.string.informOldAndNewNeighboursDoorSignsLINK));
        Task.addTask(informOldAndNewNeighbours);

        Task informAuthorities = new Task(mainActivity.getString(R.string.informAuthorities), mainActivity.getString(R.string.informAuthoritiesDesc), addMonthDaysToJavaUtilDate(removalDate, 0, 13), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement.getValue()));
        informAuthorities.addLink(mainActivity.getString(R.string.informAuthoritiesResidence), mainActivity.getString(R.string.informAuthoritiesResidenceLINK));
        informAuthorities.addLink(mainActivity.getString(R.string.informAuthoritiesCar), mainActivity.getString(R.string.informAuthoritiesCarLINK));
        Task.addTask(informAuthorities);

        Task postalAftermath = new Task(mainActivity.getString(R.string.postalAftermath), mainActivity.getString(R.string.postalAftermathDesc), addMonthDaysToJavaUtilDate(removalDate, 0, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat.getValue()));
        postalAftermath.addLink(mainActivity.getString(R.string.postalAftermathSetup), mainActivity.getString(R.string.postalAftermathLINK));
        Task.addTask(postalAftermath);

        Task informContractors = new Task(mainActivity.getString(R.string.informContractors), mainActivity.getString(R.string.informContractorsDesc), addMonthDaysToJavaUtilDate(removalDate, 0, -14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts));
        Task.addTask(informContractors);

        Task energyContract = new Task(mainActivity.getString(R.string.energyContract), mainActivity.getString(R.string.energyContractDesc), addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        energyContract.addLink(mainActivity.getString(R.string.compareEnergyContractVerivox), mainActivity.getString(R.string.energyContractVerivoxLINK));
        energyContract.addLink(mainActivity.getString(R.string.compareEnergyContractCheck24), mainActivity.getString(R.string.energyContractCheck24LINK));
        energyContract.addLink(mainActivity.getString(R.string.compareGasContractVerivox), mainActivity.getString(R.string.gasContractVerivoxLINK));
        energyContract.addLink(mainActivity.getString(R.string.compareGasContractCheck24), mainActivity.getString(R.string.gasContractCheck24LINK));
        Task.addTask(energyContract);

        Task internetContract = new Task(mainActivity.getString(R.string.internetContract)  + ' ' + mainActivity.getString(R.string.contractCancel), mainActivity.getString(R.string.internetContractDesc), addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        internetContract.addLink(mainActivity.getString(R.string.internetContractAvailability), mainActivity.getString(R.string.internetContractAvailabilityLINK));
        internetContract.addLink(mainActivity.getString(R.string.compareContractCheck24), mainActivity.getString(R.string.internetContractCheck24LINK));
        internetContract.addLink(mainActivity.getString(R.string.compareContractVerivox), mainActivity.getString(R.string.internetContractVerivoxLINK));
        Task.addTask(internetContract);

        Task householdInsurance = new Task(mainActivity.getString(R.string.householdInsurance), mainActivity.getString(R.string.householdInsuranceDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -10) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts));
        householdInsurance.addLink(mainActivity.getString(R.string.compareContracts), mainActivity.getString(R.string.compareContractVerivoxLINK));
        Task.addTask(householdInsurance);

        Task fireAndBurglaryProtection = new Task(mainActivity.getString(R.string.fireAndBurglaryProtection), mainActivity.getString(R.string.fireAndBurglaryProtectionDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -6) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat));
        fireAndBurglaryProtection.addLink(mainActivity.getString(R.string.fireAndBurglaryProtectionSmokeDetector), mainActivity.getString(R.string.fireAndBurglaryProtectionSmokeDetectorLINK));
        fireAndBurglaryProtection.addLink(mainActivity.getString(R.string.fireAndBurglaryProtectionBurglary), mainActivity.getString(R.string.fireAndBurglaryProtectionBurglaryLINKI));
        Task.addTask(fireAndBurglaryProtection);

        Task requestSpecialLeave = new Task(mainActivity.getString(R.string.requestSpecialLeave), mainActivity.getString(R.string.requestSpecialLeaveDesc), tomorrow, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        requestSpecialLeave.addLink(mainActivity.getString(R.string.requestSpecialLeave), mainActivity.getString(R.string.requestSpecialLeaveLINK));
        Task.addTask(requestSpecialLeave);

        Task rentalDepositAccount = new Task(mainActivity.getString(R.string.rentalDepositAccount), mainActivity.getString(R.string.rentalDepositAccountDesc), today, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts));
        requestSpecialLeave.addLink(mainActivity.getString(R.string.rentalDepositAccountExplanation), mainActivity.getString(R.string.rentalDepositAccountExplanationLINK));
        Task.addTask(rentalDepositAccount);

        Task organizeHelper = new Task(mainActivity.getString(R.string.organizeHelper), mainActivity.getString(R.string.organizeHelperDesc), addMonthDaysToJavaUtilDate(removalDate, 0, -14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        Task.addTask(organizeHelper);

        Task movingVan = new Task(mainActivity.getString(R.string.movingVan), mainActivity.getString(R.string.movingVanDesc), tomorrow, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        movingVan.addLink(mainActivity.getString(R.string.movingVanCompare), mainActivity.getString(R.string.movingVanCheck24LINK));
        Task.addTask(movingVan);

        Task handoverOldFlat = new Task(mainActivity.getString(R.string.handoverOldFlat), mainActivity.getString(R.string.handoverOldFlatDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -7) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat));
        Task.addTask(handoverOldFlat);

        Task handoverNewFlat = new Task(mainActivity.getString(R.string.handoverNewFlat), mainActivity.getString(R.string.handoverNewFlatDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -14) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.NewFlat));
        Task.addTask(handoverNewFlat);

        Task renovateOldFlat = new Task(mainActivity.getString(R.string.renovateOldFlat), mainActivity.getString(R.string.renovateOldFlatDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -14) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat));
        renovateOldFlat.addLink(mainActivity.getString(R.string.renovationObligation), mainActivity.getString(R.string.renovationObligationLINK));
        Task.addTask(renovateOldFlat);

        Task preserveArtisanBills = new Task(mainActivity.getString(R.string.preserveArtisanBills), mainActivity.getString(R.string.preserveArtisanBillsDesc), getLastDayInYearOf(today), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        preserveArtisanBills.addLink(mainActivity.getString(R.string.preserveArtisanBillsTip), mainActivity.getString(R.string.preserveArtisanBillsLINK));
        Task.addTask(preserveArtisanBills);

        Task bulkyWaste = new Task(mainActivity.getString(R.string.bulkyWaste), mainActivity.getString(R.string.bulkyWasteDesc), removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -14) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat));
        Task.addTask(bulkyWaste);

        // alte Kaution / Nebenkostenabrechnung

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
