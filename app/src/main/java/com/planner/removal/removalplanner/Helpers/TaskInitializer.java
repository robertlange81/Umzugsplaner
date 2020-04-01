package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;
import com.planner.removal.removalplanner.Model.TaskTypeMain;

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
        Task rentalContractOld = new Task("Alten Mietvertrag kündigen", "Kündigen Sie Ihr altes Mietverhältnis fristgerecht. Möglicherweise können Sie die Kündigungsfrist durch einen Nachmieter verkürzen.", removalDate != null ? Calendar.getInstance().getTime() : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()));
        rentalContractOld.addLink("3-Monats-Frist?", "https://ratgeber.immowelt.de/a/wohnung-kuendigen-problemlos-raus-aus-dem-mietvertrag.html");
        rentalContractOld.addLink("Sonderkündigungsrecht", "https://www.hausgold.de/sonderkuendigungsrecht/");
        Task.addTask(rentalContractOld);

        Task rentalContractNew = new Task("Neuer Mietvertrag", "Prüfen Sie Ihren Mietvertrag auf Rechtskonformität und bindende Klauseln (Mindestmietdauer, angemessene Betriebskostenvorauszahlung, zu hohe Staffelmiete, sichtbare Mängel melden, Kleinreparaturklausel, WG-Miete, Maklerprovision, Vergleich Mietspiegel / Mietendeckel)", removalDate != null ? Calendar.getInstance().getTime() : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts.getValue()));
        rentalContractNew.addLink("Mietvertrag: Vorsicht Fallen", "https://ratgeber.immowelt.de/a/mietvertrag-vorsicht-fallen.html");
        rentalContractNew.addLink("Tipps zum Mietvertrag", "https://www.nach-dem-abitur.de/mietvertrag-tipps");
        Task.addTask(rentalContractNew);

        Task informOldAndNewNeighbours = new Task("Nachbarn (Alte und Neue) informieren", "Informieren Sie Ihre alten und neuen Nachbarn per Aushang über Ihren Umzug. Beachten Sie auch die Ruhezeiten. Üblicherweise von 13 Uhr bis 15 Uhr und 20 Uhr bis 7 Uhr. Nutzen Sie zudem die Gelegenheit, um Ihre Türschilder anzubringen, damit die Post und Speditionen Sie erreichen.", removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -7) : null, Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement));
        // TODO partner-link
        rentalContractNew.addLink("Türschilder aussuchen", "https://www.amazon.de/s?k=hausschilder+mit+namen&adgrpid=69976276766&gclid=Cj0KCQjwr-_tBRCMARIsAN413WTWhp80ZVdRs4LlF2dEewnOedLgufrRhydV9qZpyuDeoZXtbCbRPpYaAnsZEALw_wcB&hvadid=391551507346&hvdev=c&hvlocphy=9042976&hvnetw=g&hvpos=1t1&hvqmt=b&hvrand=1850260414947884646&hvtargid=kwd-299359190093&hydadcr=27958_1978105&tag=googhydr08-21&ref=pd_sl_35u1b84rff_b");
        Task.addTask(informOldAndNewNeighbours);

        Task informAuthorities = new Task("Ummeldung Behörden etc.", "Nach Ihrem Umzug (aber nicht davor!) müssen Sie sich in Ihrem Einwohnermeldeamt ummelden. Geben Sie auch Ihre neue Adresse an Ihre Vertragspartner weiter und verlassen Sie sich nicht auf den Nachsendeauftrag.", addMonthDaysToJavaUtilDate(removalDate, 0, 13), Priority.High, 0L,
                new TaskType(TaskTypeMain.Movement.getValue()));
        informAuthorities.addLink("Wohnsitz ummelden", "https://umziehen.de/an-ab-ummelden/wohnsitz-anmelden-192");
        informAuthorities.addLink("Auto ummelden / neue Plakette", "https://www.umzug.de/tipps/ummelden/auto-ummelden.html");
        Task.addTask(informAuthorities);

        Task postalAftermath = new Task("Nachsendeauftrag", "Richten Sie einen Nachsendeauftrag ein. Vorsicht, hier gibt es schwarze Schafe im Internet! Nutzen Sie nur die Webseite der Post und keine Abzocher wie nachsenden.info oder Ähnliches.", addMonthDaysToJavaUtilDate(removalDate, 0, 0), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.OldFlat.getValue()));
        postalAftermath.addLink("Jetzt Nachsendeauftrag einrichten", "https://shop.deutschepost.de/nachsendeservice-beauftragen");
        Task.addTask(postalAftermath);

        Task informContractors = new Task("Vertragspartner informieren", "Informieren Sie Banken, Versicherungen, Mobilfunkanbieter, Arbeitgeber und andere Vertragspartner über Ihre neue Adresse. Verlassen Sie sich nicht nur auf den Nachsendeauftrag", addMonthDaysToJavaUtilDate(removalDate, 0, -14), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Contracts));
        Task.addTask(informContractors);

        Task currentContract = new Task("Energieversorger", "Prüfen Sie, ob Sie Ihren alten Gas - bzw. Stromvertrag behalten (aber über Adresswechsel informieren) oder wechseln müssen / dürfen.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        currentContract.addLink("Anbieter vergleichen", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(currentContract);

        Task internetContract = new Task("Internetanschluss", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen. Andernfalls muss dieser zumindest informiert werden.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        internetContract.addLink("Anbieter vergleichen", "https://www.verivox.de"); // FIXME: partner-id
        internetContract.addLink("Anbieter am neuen Wohnort?", "https://www.check24.de/dsl/verfuegbarkeit/"); // FIXME: link kann ich versorgt werden (Gebiet)
        Task.addTask(internetContract);

        Task phoneContract = new Task("Festnetztelefon", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen und ob eine Mitnahme der alten Nummer möglich ist.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        phoneContract.addLink("Telefonanbieter vergleichen", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(phoneContract);

        Task tvContract = new Task("TV-Anschluss", "Prüfen Sie, ob Sie Ihren Anbieter wechseln müssen / dürfen. Andernfalls muss dieser zumindest informiert werden.", addMonthDaysToJavaUtilDate(removalDate, 0, -7), Priority.High, 0L,
                new TaskType(TaskTypeMain.Contracts));
        tvContract.addLink("TV-Anbieter vergleichen", "https://www.verivox.de"); // FIXME: partner-id
        Task.addTask(tvContract);

        Task fireAndBurglaryProtection = new Task("Brandschutz und Einbruchsschutz", "Rauchmelder sind in einigen Bundesländern Pflicht und können Leben retten. Vergessen Sie zudem nicht den Einbruchsschutz.", removalDate != null ? addMonthDaysToJavaUtilDate(removalDate, 0, -6) : null, Priority.Normal, 0L,
                new TaskType(TaskTypeMain.NewFlat));
        fireAndBurglaryProtection.addLink("Feuermelder", "https://www.amazon.de/Rauchmelder-Brandmelder/b?ie=UTF8&node=2077635031");
        fireAndBurglaryProtection.addLink("Einbruchsschutz", "https://www.amazon.de/Einbruchschutz-Sicherheitstechnik-Baumarkt/s?k=Einbruchschutz&rh=n%3A2077623031");
        Task.addTask(fireAndBurglaryProtection);

        Task requestSpecialLeave = new Task("Sonderurlaub beantragen", "In der Regel genehmigt Ihnen Ihr Arbeitgeber für den Umzug Sonderurlaub. Fragen kostet nichts!", Calendar.getInstance().getTime(), Priority.Normal, 0L,
                new TaskType(TaskTypeMain.Movement));
        requestSpecialLeave.addLink("Sonderurlaub", "https://www.movinga.de/hub/beratung/sonderurlaub-bei-umzug//b?ie=UTF8&node=2077635031");
        Task.addTask(requestSpecialLeave);

        Persistance.SaveTasks(mainActivity);
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
