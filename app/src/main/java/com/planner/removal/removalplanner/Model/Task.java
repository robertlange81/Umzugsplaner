package com.planner.removal.removalplanner.Model;

import java.lang.reflect.Field;
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
import android.util.Log;

import com.planner.removal.removalplanner.Helpers.DBConverter.UuidConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.LinkMapConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.PriorityConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.TaskTypeConverter;
import com.planner.removal.removalplanner.Helpers.DBConverter.TimestampConverter;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Task implements Serializable {

    public static final List<Task> TASK_LIST = new ArrayList<Task>();
    public static final Map<UUID, Task> TASK_MAP = new HashMap<UUID, Task>();

    /*
    TODO / FIXME / Ideen:
    - alle menu icons nach oben
    - Loader + Hinweis
    - Synchronisieren
    - kurzes Tutorial am Anfang
    - Assistenz
    - Amazon / Google / Verivox
    - Hintergrundbild (Grundriss / Baby)
    - Ziel-date im Hintergrund
    - Orte hinzufügen -> Routenplaner !!!
    - Ausdrucken / PDF erstellen / per Mail senden
    - Import / Export (zuletzt) !!!
    - Content Provider ???
    - Bild statt Link
    - Einkaufsliste
     */

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @TypeConverters({UuidConverter.class})
    public UUID id;

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
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        date = null;
        priority = com.planner.removal.removalplanner.Model.Priority.Normal;
        costs = 0L;
        type = new TaskType(TaskTypeMain.Movement);
        links = new TreeMap<>();
    }

    public Task(String _name, String _description, Date _date, Priority _priority, long _costs, TaskType _type) {
        this(_name, _description);
        this.date = _date;
        this.priority = _priority;
        this.costs = _costs;
        this.type = _type;
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

    public void addLink(String key, String value, boolean force) {
        this.links.put(key, value);
    }

    public void addLink(String key, String value) {
        this.links.put(key, value);
    }

    public static void addTask(Task task) {
        TASK_LIST.add(task);
        TASK_MAP.put(task.id, task);
    }

    public static void removeTask(Task task) {
        TASK_LIST.remove(task);
        TASK_MAP.remove(task.id);
    }

    public static void clearAll() {
        TASK_MAP.clear();
        // TASK_MAP.remove(TASK_LIST);
        TASK_LIST.removeAll(TASK_LIST);
    }

    public static long sumCosts(boolean onlyDone) {
        long retval = 0;

        for (Task t: TASK_LIST) {
            if(onlyDone && !t.is_Done)
                continue;

            retval += t.costs;
        }

        return retval;
    }

    public boolean equals(Object obj) {
        if(obj==null) return false;
        if (!(obj instanceof Task))
            return false;
        if (obj == this)
            return true;

        Task other = (Task) obj;

        Field[] fields = other.getClass().getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                Object thisValue = field.get(this);
                Object otherValue = field.get(other);

                if(thisValue == null && otherValue == null)
                    continue;

                if(thisValue == null || otherValue == null)
                    return false;

                if(!thisValue.equals(otherValue))
                    return false;

            } catch (IllegalAccessException ex) {
                Log.e("DEBUG", "Task.equals: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        if(other.is_Done != this.is_Done)
            return false;

        return true;
    }

    public int hashCode(){
        int hash = 7;
        hash = 3 * hash + this.id.hashCode();
        /*
        hash = 3 * hash + (this.type == null ? 1 : this.type.hashCode());
        hash = 3 * hash + (this.costs == null ? 1 : this.costs.hashCode());
        hash = 3 * hash + (this.date == null ? 1 : this.date.hashCode());
        hash = 3 * hash + (this.description == null ? 1 : this.description.hashCode());
        hash = 3 * hash + (this.name == null ? 1 : this.name.hashCode());
        hash = 3 * hash + (this.priority == null ? 1 : this.priority.hashCode());
        */

        // TODO: hashCode for links necessary (currently final)?
        return hash;
    }
}
