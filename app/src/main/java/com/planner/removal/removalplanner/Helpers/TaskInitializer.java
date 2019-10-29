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
            isInitialized = true;
            Task task1 = new Task("Aufgabe1 seh lang bla bla bla bla bla", "Beschreibung1");
            task1.links.put("https://www.google.de", "https://www.google.de");
            task1.links.put("https://www.uni-leipzig.de/", "https://www.uni-leipzig.de/");
            task1.links.put("gmx.de", "gmx.de");
            task1.priority = Priority.Normal;
            task1.type = TaskType.KITCHEN;
            task1.description = "Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1";

            Task task2 = new Task("Aufgabe2", "Beschreibung2");
            task2.costs = 30000098739l;
            task2.links.put("https://www.google.de", "https://www.google.de");
            task2.links.put("https://www.uni-leipzig.de/", "https://www.uni-leipzig.de/");
            task2.links.put("gmx.de", "gmx.de");
            task2.links.put("Sonderkündigungsrecht", "https://www.hausgold.de/sonderkuendigungsrecht/");
            task2.priority = Priority.Normal;
            task2.date = defaultDate;

            Task task3 = new Task("Aufgabe3", "Beschreibung3");
            task3.costs = 3000L;
            task3.priority = Priority.High;
            Date zielTermin3 = new Date(defaultDate.getTime());
            zielTermin3.setYear(119);
            task3.date = zielTermin3;

            Task task4 = new Task("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
            task4.costs = 800000L;
            task4.priority = Priority.Normal;
            Date zielTermin4 = new Date(119, 0, 1);
            zielTermin4.setHours(10);
            task4.date = zielTermin4;
            task4.is_Done = true;

            Task task5 = new Task("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
            task5.costs = 600000L;
            task5.priority = Priority.Normal;
            task5.date = zielTermin4;
            task5.is_Done = true;


            Task.addTask(task1);
            Task.addTask(task2);
            Task.addTask(task3);
            Task.addTask(task4);
            Task.addTask(task5);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
        }
    }

    public static void InitTasks(Date removalDate) {
        // FIXME init new task serial ; Mietkautionskonto
        Task rentalContractOld = new Task("Alten Mietvertrag kündigen", "Kündigen Sie Ihr altes Mietverhältnis fristgerecht. Möglicherweise können Sie die Kündigungsfrist durch einen Nachmieter verkürzen.", Calendar.getInstance().getTime(), Priority.Normal, 0L, TaskType.ORGANISATION);
        rentalContractOld.links.put("3-Monats-Frist?", "https://ratgeber.immowelt.de/a/wohnung-kuendigen-problemlos-raus-aus-dem-mietvertrag.html");
        rentalContractOld.links.put("Sonderkündigungsrecht", "https://www.hausgold.de/sonderkuendigungsrecht/");
    }

    public static Date addMonthDaysToJavaUtilDate(Date date, int months, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
