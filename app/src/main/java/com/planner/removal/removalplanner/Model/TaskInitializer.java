package com.planner.removal.removalplanner.Model;

import java.util.Date;

public class TaskInitializer {

    public static boolean isInitialized;
    public static void CreateDefaultTasks(Date defaultDate) {

        if(!isInitialized) {
            isInitialized = true;
            Task task1 = new Task("Aufgabe1 seh lang bla bla bla bla bla", "Beschreibung1");
            task1.link = "link";
            task1.prio = Prio.Normal;
            task1.type = Type.Elektronik;

            Task task2 = new Task("Aufgabe2", "Beschreibung2");
            task2.costs = 30000098739l;
            task2.prio = Prio.Normal;
            task2.date = defaultDate;

            Task task3 = new Task("Aufgabe3", "Beschreibung3");
            task3.costs = 3000;
            task3.prio = Prio.High;
            Date zielTermin3 = new Date(defaultDate.getTime());
            zielTermin3.setYear(119);
            task3.date = zielTermin3;

            Task task4 = new Task("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
            task4.costs = 800000;
            task4.prio = Prio.Normal;
            Date zielTermin4 = new Date(119, 0, 1);
            zielTermin4.setHours(10);
            task4.date = zielTermin4;
            task4.isDone = true;

            Task.addTask(task1);
            Task.addTask(task2);
            Task.addTask(task3);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
            Task.addTask(task4);
        }
    }
}
