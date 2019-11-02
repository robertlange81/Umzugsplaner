package com.planner.removal.removalplanner.Helpers;

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
        Task rentalContractOld = new Task("Alten Mietvertrag kündigen", "Kündigen Sie Ihr altes Mietverhältnis fristgerecht. Möglicherweise können Sie die Kündigungsfrist durch einen Nachmieter verkürzen.", Calendar.getInstance().getTime(), Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractOld.addLink("3-Monats-Frist?", "https://ratgeber.immowelt.de/a/wohnung-kuendigen-problemlos-raus-aus-dem-mietvertrag.html");
        rentalContractOld.addLink("Sonderkündigungsrecht", "https://www.hausgold.de/sonderkuendigungsrecht/");
        Task.addTask(rentalContractOld.build());

        Task rentalContractNew = new Task("Neuen Mietvertrag unterschreiben", "Prüfen Sie Ihren Mietvertrag auf Rechtskonformität und bindende Klauseln (Mindestmietdauer, angemessene Betriebskostenvorauszahlung, zu hohe Staffelmiete, sichtbare Mängel melden, Kleinreparaturklausel, WG-Miete, Maklerprovision, Vergleich Mietspiegel / Mietendeckel)", Calendar.getInstance().getTime(), Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractNew.addLink("Mietvertrag: Vorsicht Fallen", "https://ratgeber.immowelt.de/a/mietvertrag-vorsicht-fallen.html");
        rentalContractNew.addLink("Tipps zum Mietvertrag", "https://www.nach-dem-abitur.de/mietvertrag-tipps");
        Task.addTask(rentalContractNew.build());

        Task informOldAndNewNeighbours = new Task("Alte und Neue Nachbarn informieren", "Informieren Sie Ihre alten und neuen Nachbarn per Aushang über Ihren Umzug. Beachten Sie auch die Ruhezeiten. Üblicherweise von 13 Uhr bis 15 Uhr und 20 Uhr bis 7 Uhr. Nutzen Sie zudem die Gelegenheit, um Ihre Türschilder anzubringen, damit die Post und Speditionen Sie erreichen.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L, TaskType.ORGANISATION);
        rentalContractNew.addLink("Türschilder aussuchen", "https://www.amazon.de/s?k=hausschilder+mit+namen&adgrpid=69976276766&gclid=Cj0KCQjwr-_tBRCMARIsAN413WTWhp80ZVdRs4LlF2dEewnOedLgufrRhydV9qZpyuDeoZXtbCbRPpYaAnsZEALw_wcB&hvadid=391551507346&hvdev=c&hvlocphy=9042976&hvnetw=g&hvpos=1t1&hvqmt=b&hvrand=1850260414947884646&hvtargid=kwd-299359190093&hydadcr=27958_1978105&tag=googhydr08-21&ref=pd_sl_35u1b84rff_b");
        Task.addTask(informOldAndNewNeighbours.build());

        Task informAuthoritiesAndContractors = new Task("Behörden etc. ummelden", "Nach Ihrem Umzug (aber nicht davor!) müssen Sie sich in Ihrem Einwohnermeldeamt ummelden. Geben Sie auch Ihre neue Adresse an Ihre Vertragspartner weiter und verlasen Sie sich nicht auf den Nachsendeauftrag:" +
                "\n* Einwohnermeldeamt" +
                "\n* KFZ, Parkausweis, Umwelt-Plakette" +
                "\n* Arbeitgeber" +
                "\n* Bank(en) und Versicherungen", addMonthDaysToJavaUtilDate(removalDate, 0, 13), Priority.High, 0L, TaskType.ORGANISATION);
        informAuthoritiesAndContractors.addLink("Wohnsitz ummelden", "https://umziehen.de/an-ab-ummelden/wohnsitz-anmelden-192");
        informAuthoritiesAndContractors.addLink("Auto ummelden", "https://www.umzug.de/tipps/ummelden/auto-ummelden.html");
        Task.addTask(informAuthoritiesAndContractors.build());

        Task fireAndBurglaryProtection = new Task("Brandschutz und Einbruchschutz", "Rauchmelder sind in einigen Bundesländern Pflicht und können Leben retten. Vergessen Sie zudem nicht den Einbruchsschutz.", addMonthDaysToJavaUtilDate(removalDate, 0, -6), Priority.Normal, 0L, TaskType.SECURITY);
        fireAndBurglaryProtection.addLink("Feuermelder", "https://www.amazon.de/Rauchmelder-Brandmelder/b?ie=UTF8&node=2077635031");
        fireAndBurglaryProtection.addLink("Einbruchsschutz", "https://www.amazon.de/Einbruchschutz-Sicherheitstechnik-Baumarkt/s?k=Einbruchschutz&rh=n%3A2077623031");
        Task.addTask(fireAndBurglaryProtection.build());
    }

    public static Date addMonthDaysToJavaUtilDate(Date date, int months, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
