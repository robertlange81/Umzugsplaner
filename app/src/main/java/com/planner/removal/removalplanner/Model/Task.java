package com.planner.removal.removalplanner.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Task {
    public static final List<Task> TASK_LIST = new ArrayList<Task>();
    public static final Map<String, Task> TASK_MAP = new HashMap<String, Task>();
    public static int maxId = 0;
    public String id;

    /*
    Ideen:
    - Hintergrundbild (Grundriss / Baby)
    - Ziel-date im Hintergrund
    - mit Google-Kalender synchronisieren
    - Orte hinzufügen -> Routenplaner
    - Kostenübersicht
    - Ausdrucken / PDF erstellen / per Mail senden
    - Import / Export
    - Standard neu importieren
    - Amazon / Google
    - Content Provider
    - Sortieren
     */

    public String name;
    public String description;
    public com.planner.removal.removalplanner.Model.Prio prio;
    public Date date;
    public boolean isDone;
    public TaskType type;
    public long costs; // in Cent
    public TreeMap<String,String> links;

    public Task(String name, String description) {
        id = new Integer(maxId++).toString();
        this.name = name;
        this.description = description;
        date = new Date(Long.MAX_VALUE);
        prio = com.planner.removal.removalplanner.Model.Prio.Normal;
        costs = 0;
        type = TaskType.Furniture;
        links = new TreeMap<>();
    }

    public Task(Task clone) {
        id = clone.id;
        this.name = clone.name;
        this.description = clone.description;
        date = clone.date;
        prio = clone.prio;
        costs = clone.costs;
        type = clone.type;
        links = (TreeMap) clone.links.clone();
        isDone = clone.isDone;
    }

    public void ImportTask(Task clone) {
        this.name = clone.name;
        this.description = clone.description;
        date = clone.date;
        prio = clone.prio;
        costs = clone.costs;
        type = clone.type;
        links = clone.links;
        isDone = clone.isDone;
    }

    public static void addTask(Task task) {
        TASK_LIST.add(task);
        TASK_MAP.put(task.id, task);
    }
    public static void removeTask(Task task) {
        TASK_LIST.remove(task);
        TASK_MAP.remove(task.id);
    }
}
