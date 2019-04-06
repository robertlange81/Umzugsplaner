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
    - Ziel-Date im Hintergrund
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

    public String Name; // 1
    public String Description; // 2
    public com.planner.removal.removalplanner.Model.Priority Priority; // 4
    public Date Date; // 8
    public boolean Is_Done; // 16
    public TaskType Type; // 32
    public Long Costs; // 64 in Cent
    public TreeMap<String,String> Links; // 128

    public Task(String name, String description) {
        id = new Integer(maxId++).toString();
        this.Name = name;
        this.Description = description;
        Date = null;
        Priority = com.planner.removal.removalplanner.Model.Priority.Normal;
        Costs = 0L;
        Type = TaskType.KITCHEN;
        Links = new TreeMap<>();
    }

    public Task(Task clone) {
        id = clone.id;
        this.Name = clone.Name;
        this.Description = clone.Description;
        Priority = clone.Priority;
        Date = clone.Date;
        Is_Done = clone.Is_Done;
        Type = clone.Type;
        Costs = clone.Costs;
        Links = (TreeMap) clone.Links.clone();
    }

    public void ImportTask(Task clone) {
        this.Name = clone.Name;
        this.Description = clone.Description;
        Priority = clone.Priority;
        Date = clone.Date;
        Is_Done = clone.Is_Done;
        Type = clone.Type;
        Costs = clone.Costs;
        Links = clone.Links;
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
