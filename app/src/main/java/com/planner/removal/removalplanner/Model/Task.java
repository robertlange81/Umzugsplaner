package com.planner.removal.removalplanner.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.planner.removal.removalplanner.Helpers.DBConverter.LinkMapConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.PriorityConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.TaskTypeConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.TimestampConverter;

import java.io.Serializable;

@Entity
public class Task implements Serializable {

    public static final List<Task> TASK_LIST = new ArrayList<Task>();
    public static final Map<String, Task> TASK_MAP = new HashMap<String, Task>();
    public static int maxId = 0;

    /*
    Ideen:
    - Assistenz
    - Kosten auch negativ
    - Kostenübersicht - gesamt, erledigt, offen
    - Hintergrundbild (Grundriss / Baby)
    - Ziel-date im Hintergrund
    - mit Google-Kalender synchronisieren
    - Orte hinzufügen -> Routenplaner
    - Ausdrucken / PDF erstellen / per Mail senden
    - Import / Export
    - Standard neu importieren
    - Amazon / Google
    - Content Provider
    - Sortieren
    - Bild statt Link
    - Einkaufsliste
    - nach oben scrollen nach Sortieren
     */

    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name; // 1

    @ColumnInfo(name = "description")
    public String description; // 2

    @ColumnInfo(name = "priority")
    @TypeConverters({PriorityConverter.class})
    public com.planner.removal.removalplanner.Model.Priority priority; // 4

    @ColumnInfo(name = "date")
    @TypeConverters({TimestampConverter.class})
    public Date date; // 8

    @ColumnInfo(name = "is_Done")
    public boolean is_Done; // 16

    @ColumnInfo(name = "type")
    @TypeConverters({TaskTypeConverter.class})
    public TaskType type; // 32

    @ColumnInfo(name = "costs")
    public Long costs; // 64 in Cent

    @ColumnInfo(name = "links")
    @TypeConverters({LinkMapConverter.class})
    public TreeMap<String,String> links; // 128

    public Task(String name, String description) {
        id = new Integer(maxId++).toString();
        this.name = name;
        this.description = description;
        date = null;
        priority = com.planner.removal.removalplanner.Model.Priority.Normal;
        costs = 0L;
        type = TaskType.KITCHEN;
        links = new TreeMap<>();
    }

    public Task(Task clone) {
        id = clone.id;
        this.name = clone.name;
        this.description = clone.description;
        priority = clone.priority;
        date = clone.date;
        is_Done = clone.is_Done;
        type = clone.type;
        costs = clone.costs;
        links = (TreeMap) clone.links.clone();
    }

    public void ImportTask(Task clone) {
        this.name = clone.name;
        this.description = clone.description;
        priority = clone.priority;
        date = clone.date;
        is_Done = clone.is_Done;
        type = clone.type;
        costs = clone.costs;
        links = clone.links;
    }

    public static void addTask(Task task) {
        TASK_LIST.add(task);
        TASK_MAP.put(task.id.toString(), task);
    }
    public static void removeTask(Task task) {
        TASK_LIST.remove(task);
        TASK_MAP.remove(task.id);
    }

    public static int sumCosts() {
        int retval = 0;

        for (Task t: TASK_LIST) {
            retval += t.costs;
        }

        return retval;
    }
}
