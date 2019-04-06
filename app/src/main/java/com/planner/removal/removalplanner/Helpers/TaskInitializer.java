package com.planner.removal.removalplanner.Helpers;

import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;

import java.util.Date;

public class TaskInitializer {

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {

        if(!isInitialized) {
            isInitialized = true;
            Task task1 = new Task("Aufgabe1 seh lang bla bla bla bla bla", "Beschreibung1");
            task1.Links.put("https://www.google.de", "https://www.google.de");
            task1.Links.put("https://www.uni-leipzig.de/", "https://www.uni-leipzig.de/");
            task1.Links.put("gmx.de", "gmx.de");
            task1.Priority = Priority.Normal;
            task1.Type = TaskType.KITCHEN;
            task1.Description = "Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1 Beschreibung1";

            Task task2 = new Task("Aufgabe2", "Beschreibung2");
            task2.Costs = 30000098739l;
            task2.Links.put("https://www.google.de", "https://www.google.de");
            task2.Links.put("https://www.uni-leipzig.de/", "https://www.uni-leipzig.de/");
            task2.Links.put("gmx.de", "gmx.de");
            task2.Links.put("gmx", "www.gmx.de");
            task2.Priority = Priority.Normal;
            task2.Date = defaultDate;

            Task task3 = new Task("Aufgabe3", "Beschreibung3");
            task3.Costs = 3000L;
            task3.Priority = Priority.High;
            Date zielTermin3 = new Date(defaultDate.getTime());
            zielTermin3.setYear(119);
            task3.Date = zielTermin3;

            Task task4 = new Task("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
            task4.Costs = 800000L;
            task4.Priority = Priority.Normal;
            Date zielTermin4 = new Date(119, 0, 1);
            zielTermin4.setHours(10);
            task4.Date = zielTermin4;
            task4.Is_Done = true;

            Task task5 = new Task("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
            task5.Costs = 600000L;
            task5.Priority = Priority.Normal;
            task5.Date = zielTermin4;
            task5.Is_Done = true;


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
}
