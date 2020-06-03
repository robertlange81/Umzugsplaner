package com.planner.generic.checklist.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import com.planner.generic.checklist.Model.Location;
import com.planner.generic.checklist.Model.Priority;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.Model.TaskType;
import com.planner.generic.checklist.Model.TaskTypeMain;
import com.planner.generic.checklist.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TaskInitializer {

    public static ListType CURRENT_LIST_TYPE = ListType.LOCKDOWN;

    public enum ListType {

        NONE(0),
        REMOVAL(1),
        LOCKDOWN(2);

        private int value;

        ListType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static void InitTasks(Date targetDate, Location targetLocation, Activity context) {

        if(CURRENT_LIST_TYPE.toString() == "LOCKDOWN") {
            targetDate = addMonthDaysToJavaUtilDate(targetDate, 0, -22);
        }

        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
        today.setHours(0);
        today.setMinutes(0);
        Date tomorrow = addMonthDaysToJavaUtilDate(today, 0, 1);

        SharedPreferences prefs = context.getSharedPreferences("checklist", 0);

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

        switch (CURRENT_LIST_TYPE.toString()) {
            case "REMOVAL":
                //AddRemovalTasks(targetDate, targetLocation, context, today, tomorrow);
                break;
            case "LOCKDOWN":
                AddLockdownTasks(targetDate, targetLocation, context, today, tomorrow);
                break;
        }

        Persistance.SaveTasks(context);
    }

    private static void AddLockdownTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {

        // Desinfektionsmittel
        Task disinfectant = new Task(context.getString(R.string.disinfectant), context.getString(R.string.disinfectant_desc), addMonthDaysToJavaUtilDate(targetDate, 0, 1), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(disinfectant);

        // FFP3-Masken
        Task respiratoryProtection = new Task(context.getString(R.string.Respirators), context.getString(R.string.Respirators_desc), addMonthDaysToJavaUtilDate(targetDate, 0, 1), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(respiratoryProtection);

        // Verbandskasten
        Task medicalKit = new Task(context.getString(R.string.medicalkit), context.getString(R.string.firstAidKitDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(medicalKit);

        // Vom Arzt verschriebene Medikamente
        Task medication = new Task(context.getString(R.string.medication), context.getString(R.string.medicationDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(medication);

        // Schmerzmittel
        Task painReliever = new Task(context.getString(R.string.painReliever), context.getString(R.string.painRelieverDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(painReliever);

        // Mittel gegen Durchfall
        Task DiarrheaRemedy = new Task(context.getString(R.string.DiarrheaRemedy), context.getString(R.string.DiarrheaRemedy), addMonthDaysToJavaUtilDate(targetDate, 0, 3), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(DiarrheaRemedy);

        // Erkältungsmittel
        Task coldRemedies = new Task(context.getString(R.string.coldRemedies), context.getString(R.string.coldRemediesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 4), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(coldRemedies);

        // Fieberthermometer
        Task clinicalThermometer = new Task(context.getString(R.string.clinicalThermometer), context.getString(R.string.clinicalThermometerDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeMain.MEDICIN), targetLocation);
        Task.addTask(clinicalThermometer);

        // Wasser
        Task water = new Task(context.getString(R.string.water), context.getString(R.string.waterDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeMain.HOUSEHOLD), targetLocation);
        Task.addTask(water);

        // Fertiggerichte
        Task readyMeals = new Task(context.getString(R.string.readyMeals), context.getString(R.string.readyMealsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(readyMeals);

        // Taschenlampe
        Task pocketLamp = new Task(context.getString(R.string.pocketLamp), context.getString(R.string.pocketLampDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeMain.ELECTRONICS), targetLocation);
        Task.addTask(pocketLamp);

        // Batterien
        Task batteries = new Task(context.getString(R.string.batteries), context.getString(R.string.batteriesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 5), Priority.High, 0L,
                new TaskType(TaskTypeMain.ELECTRONICS), targetLocation);
        Task.addTask(batteries);

        // Kartoffeln
        Task potatoes = new Task(context.getString(R.string.potatoes), context.getString(R.string.potatoesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(potatoes);

        // Treibstoff
        Task fuel = new Task(context.getString(R.string.fuel), context.getString(R.string.fuelDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.MISCELLANEOUS), targetLocation);
        Task.addTask(fuel);

        // Schlafsack
        Task sleepingBag = new Task(context.getString(R.string.sleepingBag), context.getString(R.string.sleepingBagDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 7), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.MISCELLANEOUS), targetLocation);
        Task.addTask(sleepingBag);

        // wasserfeste Kleidung
        Task waterproofClothing = new Task(context.getString(R.string.waterproofClothing), context.getString(R.string.waterproofClothingDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.MISCELLANEOUS), targetLocation);
        Task.addTask(waterproofClothing);

        // Gemüse
        Task vegetables = new Task(context.getString(R.string.vegetables), context.getString(R.string.vegetablesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(vegetables);

        // Obst
        Task fruit = new Task(context.getString(R.string.fruit), context.getString(R.string.fruitDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(fruit);

        // Milch
        Task milk = new Task(context.getString(R.string.milk), context.getString(R.string.milkDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(milk);

        // Fett und Öl
        Task fatOil = new Task(context.getString(R.string.fatOil), context.getString(R.string.fatOilDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 21), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(fatOil);

        // Gewürze
        Task spices = new Task(context.getString(R.string.spices), context.getString(R.string.spicesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 21), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.FOOD), targetLocation);
        Task.addTask(spices);
    }

    /*
    private static void AddRemovalTasks(Date targetDate, Location targetLocation, Activity context, Date today, Date tomorrow) {
        // Umzugstermin selbst
        if(targetDate != null) {
            Task removal = new Task(context.getString(R.string.removalDate), context.getString(R.string.removalDateDesc), new Date(targetDate.getTime()), Priority.High, 0L,
                    new TaskType(TaskTypeMain.Movement), targetLocation);
            if(targetLocation != null) {
                removal.addLink(
                        context.getString(R.string.routeMap),
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
        Task rentalContractOld = new Task(context.getString(R.string.taskRentalContractOld), (context.getString(R.string.taskRentalContractOldDesc) + ' ' + context.getString(R.string.contractCancel)), targetDate != null ? tomorrow : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()), targetLocation);
        rentalContractOld.addLink(context.getString(R.string.taskRentalContractOld3Months), context.getString(R.string.taskRentalContractOld3MonthsLINK));
        rentalContractOld.addLink(context.getString(R.string.rentalContractOldSpecialRightOfTermination), context.getString(R.string.rentalContractOldSpecialRightOfTerminationLINK));
        Task.addTask(rentalContractOld);

        // Vertrag neu
        Task rentalContractNew = new Task(context.getString(R.string.rentalContractNew), context.getString(R.string.rentalContractNewDesc), targetDate != null ? today : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()), targetLocation);
        rentalContractNew.addLink(context.getString(R.string.rentalContractNewTips), context.getString(R.string.rentalContractNewTipsLINK));
        rentalContractNew.addLink(context.getString(R.string.rentalContractNewTraps), context.getString(R.string.rentalContractNewTrapsLink));
        Task.addTask(rentalContractNew);

        // Vertragspartner
        Task informContractors = new Task(context.getString(R.string.informContractors), context.getString(R.string.informContractorsDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        Task.addTask(informContractors);

        // Strom / Gas
        Task energyContract = new Task(context.getString(R.string.energyContract), context.getString(R.string.energyContractDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        energyContract.addLink(context.getString(R.string.compareEnergyContractVerivox), context.getString(R.string.energyContractVerivoxLINK));
        energyContract.addLink(context.getString(R.string.compareEnergyContractCheck24), context.getString(R.string.energyContractCheck24LINK));
        energyContract.addLink(context.getString(R.string.compareGasContractVerivox), context.getString(R.string.gasContractVerivoxLINK));
        energyContract.addLink(context.getString(R.string.compareGasContractCheck24), context.getString(R.string.gasContractCheck24LINK));
        Task.addTask(energyContract);

        // Internet / DSL
        Task internetContract = new Task(context.getString(R.string.internetContract), context.getString(R.string.internetContractDesc)  + ' ' + context.getString(R.string.contractCancel), addMonthDaysToJavaUtilDate(targetDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        internetContract.addLink(context.getString(R.string.internetContractAvailability), context.getString(R.string.internetContractAvailabilityLINK));
        internetContract.addLink(context.getString(R.string.compareContractCheck24), context.getString(R.string.internetContractCheck24LINK));
        internetContract.addLink(context.getString(R.string.compareContractVerivox), context.getString(R.string.internetContractVerivoxLINK));
        Task.addTask(internetContract);

        // Haftpflicht
        Task liabilityInsurance = new Task(context.getString(R.string.liabilityInsurance), context.getString(R.string.liabilityInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        liabilityInsurance.addLink(context.getString(R.string.compareLiabilityInsurance), context.getString(R.string.compareLiabilityInsuranceVerivoxLINK));
        Task.addTask(liabilityInsurance);

        // Hausrat
        Task householdInsurance = new Task(context.getString(R.string.householdInsurance), context.getString(R.string.householdInsuranceDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -10) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        householdInsurance.addLink(context.getString(R.string.compareContracts), context.getString(R.string.compareContractVerivoxLINK));
        Task.addTask(householdInsurance);

        // Brandschutz
        Task fireAndBurglaryProtection = new Task(context.getString(R.string.fireAndBurglaryProtection), context.getString(R.string.fireAndBurglaryProtectionDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -6) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
        fireAndBurglaryProtection.addLink(context.getString(R.string.fireAndBurglaryProtectionSmokeDetector), context.getString(R.string.fireAndBurglaryProtectionSmokeDetectorLINK));
        fireAndBurglaryProtection.addLink(context.getString(R.string.fireAndBurglaryProtectionBurglary), context.getString(R.string.fireAndBurglaryProtectionBurglaryLINKI));
        Task.addTask(fireAndBurglaryProtection);

        // Urlaub
        Task requestSpecialLeave = new Task(context.getString(R.string.requestSpecialLeave), context.getString(R.string.requestSpecialLeaveDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        requestSpecialLeave.addLink(context.getString(R.string.requestSpecialLeave), context.getString(R.string.requestSpecialLeaveLINK));
        Task.addTask(requestSpecialLeave);

        // Kautionskonto
        Task rentalDepositAccount = new Task(context.getString(R.string.rentalDepositAccount), context.getString(R.string.rentalDepositAccountDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -30) : null , Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts), targetLocation);
        rentalDepositAccount.addLink(context.getString(R.string.rentalDepositAccountExplanation), context.getString(R.string.rentalDepositAccountExplanationLINK));
        Task.addTask(rentalDepositAccount);

        // Umzugskartons / Müllsäcke
        Task packingMaterial = new Task(context.getString(R.string.packingMaterial), context.getString(R.string.packingMaterialDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -20) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        packingMaterial.addLink(context.getString(R.string.packingMaterialTrashBags), context.getString(R.string.packingMaterialTrashBagsAmazonLINK));
        packingMaterial.addLink(context.getString(R.string.packingMaterialAirCushionFoil), context.getString(R.string.packingMaterialAirCushionFoilAmazonLINK));
        packingMaterial.addLink(context.getString(R.string.packingMaterialMovingBoxes), context.getString(R.string.packingMaterialMovingBoxesEbayLINK));
        Task.addTask(packingMaterial);

        // Umzugshelfer
        Task organizeHelper = new Task(context.getString(R.string.organizeHelper), context.getString(R.string.organizeHelperDesc), addMonthDaysToJavaUtilDate(targetDate, 0, -14), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        organizeHelper.addLink(context.getString(R.string.organizeHelperWorkGloves), context.getString(R.string.organizeHelperWorkGlovesAmazonLink));
        organizeHelper.addLink(context.getString(R.string.organizeHelperCarryingStraps), context.getString(R.string.organizeHelperCarryingStrapsEbayLINK));
        organizeHelper.addLink(context.getString(R.string.organizeHelperhandTruck), context.getString(R.string.organizeHelperHandTrucksEbayLINK));
        Task.addTask(organizeHelper);

        // Umzugswagen
        Task movingVan = new Task(context.getString(R.string.movingVan), context.getString(R.string.movingVanDesc), tomorrow, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        movingVan.addLink(context.getString(R.string.movingVanCompare), context.getString(R.string.movingVanCheck24LINK));
        Task.addTask(movingVan);

        // Schlüsselübergabe
        Task handoverNewFlat = new Task(context.getString(R.string.handoverNewFlat), context.getString(R.string.handoverNewFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -3) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
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
                new TaskType(TaskTypeMain.Movement), targetLocation);
        cordonOffParkingLot.addLink(context.getString(R.string.cordonOffParkingLotHowTo), context.getString(R.string.cordonOffParkingLotHowToLINK));
        Task.addTask(cordonOffParkingLot);

        // Vortag
        Task prevDay = new Task(context.getString(R.string.previousDay), context.getString(R.string.previousDayDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        Task.addTask(prevDay);

        // Malern - Schönheitsreparaturen
        Task renovateOldFlat = new Task(context.getString(R.string.renovateOldFlat), context.getString(R.string.renovateOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 3) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        renovateOldFlat.addLink(context.getString(R.string.renovationObligation), context.getString(R.string.renovationObligationLINK));
        Task.addTask(renovateOldFlat);

        // Wohnungsabnahme
        Task handoverOldFlat = new Task(context.getString(R.string.handoverOldFlat), context.getString(R.string.handoverOldFlatDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        Task.addTask(handoverOldFlat);

        // Nachbarn
        Task informOldAndNewNeighbours = new Task(context.getString(R.string.informOldAndNewNeighbours), context.getString(R.string.informOldAndNewNeighboursDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, -7) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        // TODO partner-link
        informOldAndNewNeighbours.addLink(context.getString(R.string.informOldAndNewNeighboursDoorSigns), context.getString(R.string.informOldAndNewNeighboursDoorSignsLINK));
        Task.addTask(informOldAndNewNeighbours);

        // Ummeldung
        Task informAuthorities = new Task(context.getString(R.string.informAuthorities), context.getString(R.string.informAuthoritiesDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 13), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement.getValue()), targetLocation);
        informAuthorities.addLink(context.getString(R.string.informAuthoritiesResidence), context.getString(R.string.informAuthoritiesResidenceLINK));
        informAuthorities.addLink(context.getString(R.string.informAuthoritiesCar), context.getString(R.string.informAuthoritiesCarLINK));
        Task.addTask(informAuthorities);

        // Nachsendeauftrag
        Task postalAftermath = new Task(context.getString(R.string.postalAftermath), context.getString(R.string.postalAftermathDesc), addMonthDaysToJavaUtilDate(targetDate, 0, 0), Priority.High, 0L,
                new TaskType(TaskTypeMain.OldFlat.getValue()), targetLocation);
        postalAftermath.addLink(context.getString(R.string.postalAftermathSetup), context.getString(R.string.postalAftermathLINK));
        Task.addTask(postalAftermath);

        // Steuer
        Task preserveArtisanBills = new Task(context.getString(R.string.preserveArtisanBills), context.getString(R.string.preserveArtisanBillsDesc), getLastDayInYearOf(today), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement), targetLocation);
        preserveArtisanBills.addLink(context.getString(R.string.preserveArtisanBillsTip), context.getString(R.string.preserveArtisanBillsLINK));
        Task.addTask(preserveArtisanBills);

        // Sperrmüll
        Task bulkyWaste = new Task(context.getString(R.string.bulkyWaste), context.getString(R.string.bulkyWasteDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 1) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        Task.addTask(bulkyWaste);

        // alte Kaution / Nebenkostenabrechnung
        Task oldRentDeposit = new Task(context.getString(R.string.oldRentDeposit), context.getString(R.string.oldRentDepositDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 60) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat), targetLocation);
        oldRentDeposit.addLink(context.getString(R.string.oldRentDepositHowItWorks), context.getString(R.string.oldRentDepositHowItWorksLINK));
        Task.addTask(oldRentDeposit);

        // Einweihungsfeier
        Task party = new Task(context.getString(R.string.party), context.getString(R.string.partyDesc), targetDate != null ? addMonthDaysToJavaUtilDate(targetDate, 0, 30) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat), targetLocation);
        party.addLink(context.getString(R.string.partyEbay), context.getString(R.string.partyEbayLINK));
        Task.addTask(party);
    }
*/
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
