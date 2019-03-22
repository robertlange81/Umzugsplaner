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
    - Bild statt Link
     */

    public String name; // 1
    public String description; // 2
    public com.planner.removal.removalplanner.Model.Prio prio; // 4
    public Date date; // 8
    public boolean isDone; // 16
    public TaskType type; // 32
    public long costs; // 64 in Cent
    public TreeMap<String,String> links; // 128

    public Task(String name, String description) {
        id = new Integer(maxId++).toString();
        this.name = name;
        this.description = description;
        date = new Date(Long.MAX_VALUE);
        prio = com.planner.removal.removalplanner.Model.Prio.Normal;
        costs = 0;
        type = TaskType.KITCHEN;
        links = new TreeMap<>();
    }

    public Task(Task clone) {
        id = clone.id;
        this.name = clone.name;
        this.description = clone.description;
        prio = clone.prio;
        date = clone.date;
        isDone = clone.isDone;
        type = clone.type;
        costs = clone.costs;
        links = (TreeMap) clone.links.clone();
    }

    public void ImportTask(Task clone) {
        this.name = clone.name;
        this.description = clone.description;
        prio = clone.prio;
        date = clone.date;
        isDone = clone.isDone;
        type = clone.type;
        costs = clone.costs;
        links = clone.links;
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
