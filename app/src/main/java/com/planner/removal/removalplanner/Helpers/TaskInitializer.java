package com.planner.removal.removalplanner.Helpers;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class TaskInitializer {

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {
        if(!isInitialized) {

        }
    }

    public static void InitTasks(Date removalDate) {
        // FIXME Notizen in Task
        // FIXME init new task serial ; Mietkautionskonto; Verträge; Bank etc. neue Adresse mitteilen
        Task rentalContractOld = new Task("Mietvertrag (alt) kündigen", "Kündigen Sie Ihr altes Mietverhältnis fristgerecht. Möglicherweise können Sie die Kündigungsfrist durch einen Nachmieter verkürzen.", removalDate != null ? Calendar.getInstance().getTime() : null, Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractOld.addLink("3-Monats-Frist?", "https://ratgeber.immowelt.de/a/wohnung-kuendigen-problemlos-raus-aus-dem-mietvertrag.html");
        rentalContractOld.addLink("Sonderkündigungsrecht", "https://www.hausgold.de/sonderkuendigungsrecht/");
        Task.addTask(rentalContractOld);

        Task rentalContractNew = new Task("Mietvertrag (neu) unterschreiben", "Prüfen Sie Ihren Mietvertrag auf Rechtskonformität und bindende Klauseln (Mindestmietdauer, angemessene Betriebskostenvorauszahlung, zu hohe Staffelmiete, sichtbare Mängel melden, Kleinreparaturklausel, WG-Miete, Maklerprovision, Vergleich Mietspiegel / Mietendeckel)", removalDate != null ? Calendar.getInstance().getTime() : null, Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractNew.addLink("Mietvertrag: Vorsicht Fallen", "https://ratgeber.immowelt.de/a/mietvertrag-vorsicht-fallen.html");
        rentalContractNew.addLink("Tipps zum Mietvertrag", "https://www.nach-dem-abitur.de/mietvertrag-tipps");
        Task.addTask(rentalContractNew);

        Task informOldAndNewNeighbours = new Task("Nachbarn (Alte und Neue) informieren", "Informieren Sie Ihre alten und neuen Nachbarn per Aushang über Ihren Umzug. Beachten Sie auch die Ruhezeiten. Üblicherweise von 13 Uhr bis 15 Uhr und 20 Uhr bis 7 Uhr. Nutzen Sie zudem die Gelegenheit, um Ihre Türschilder anzubringen, damit die Post und Speditionen Sie erreichen.", removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -7) : null, Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractNew.addLink("Türschilder aussuchen", "https://www.amazon.de/s?k=hausschilder+mit+namen&adgrpid=69976276766&gclid=Cj0KCQjwr-_tBRCMARIsAN413WTWhp80ZVdRs4LlF2dEewnOedLgufrRhydV9qZpyuDeoZXtbCbRPpYaAnsZEALw_wcB&hvadid=391551507346&hvdev=c&hvlocphy=9042976&hvnetw=g&hvpos=1t1&hvqmt=b&hvrand=1850260414947884646&hvtargid=kwd-299359190093&hydadcr=27958_1978105&tag=googhydr08-21&ref=pd_sl_35u1b84rff_b");
        Task.addTask(informOldAndNewNeighbours);

        Task informAuthorities = new Task("Ummeldung Behörden etc.", "Nach Ihrem Umzug (aber nicht davor!) müssen Sie sich in Ihrem Einwohnermeldeamt ummelden. Geben Sie auch Ihre neue Adresse an Ihre Vertragspartner weiter und verlassen Sie sich nicht auf den Nachsendeauftra.", addMonthDaysToJavaUtilDate(removalDate, 0, 13), Priority.High, 0L, TaskType.ORGANISATION);
        informAuthorities.addLink("Wohnsitz ummelden", "https://umziehen.de/an-ab-ummelden/wohnsitz-anmelden-192");
        informAuthorities.addLink("Auto ummelden / neue Plakette", "https://www.umzug.de/tipps/ummelden/auto-ummelden.html");
        Task.addTask(informAuthorities);

        Task informContractors = new Task("Vertragspartner informieren", "Informieren Sie Banken, Versicherungen, Mobilfunkanbieter, Arbeitgeber und andere Vertragspartner über Ihre neue Adresse. verlassen Sie sich nicht nur auf den Nachsendeauftrag", addMonthDaysToJavaUtilDate(removalDate, 0, -14), Priority.Normal, 0L, TaskType.ORGANISATION);
        Task.addTask(informContractors);

        Task currentContract = new Task("Energieversorger", "Prüfen Sie, ob Sie Ihren alten Gas - bzw. Stromvertrag behalten (aber über Adresswechsel informieren) oder wechseln müssen / dürfen.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L, TaskType.ORGANISATION);
        currentContract.addLink("Stromanbieter wechseln", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(currentContract);

        Task internetContract = new Task("Internetanschluss", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen. Andernfalls muss dieser zumindest informiert werden.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L, TaskType.ORGANISATION);
        internetContract.addLink("Internetanbieter wechseln", "https://www.verivox.de"); // FIXME: partner-id
        internetContract.addLink("alter Anbieter am neuen Wohnort?", "https://www.verivox.de"); // FIXME: link kann ich versorgt werden (Gebiet)
        Task.addTask(internetContract);

        Task phoneContract = new Task("Festnetztelefon", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen und ob eine Mitnahme der alten Nummer möglich ist.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L, TaskType.ORGANISATION);
        phoneContract.addLink("Telefonanbieter wechseln", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(phoneContract);

        Task tvContract = new Task("TV-Anschluss", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen. Andernfalls muss dieser zumindest informiert werden.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L, TaskType.ORGANISATION);
        tvContract.addLink("TV-Anbieter wechseln", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(tvContract);

        Task fireAndBurglaryProtection = new Task("Brandschutz und Einbruchsschutz", "Rauchmelder sind in einigen Bundesländern Pflicht und können Leben retten. Vergessen Sie zudem nicht den Einbruchsschutz.", removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -6) : null, Priority.Normal, 0L, TaskType.SECURITY);
        fireAndBurglaryProtection.addLink("Feuermelder", "https://www.amazon.de/Rauchmelder-Brandmelder/b?ie=UTF8&node=2077635031");
        fireAndBurglaryProtection.addLink("Einbruchsschutz", "https://www.amazon.de/Einbruchschutz-Sicherheitstechnik-Baumarkt/s?k=Einbruchschutz&rh=n%3A2077623031");
        Task.addTask(fireAndBurglaryProtection);

        MainActivity.NotifyTaskChanged(null, null);
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
}
