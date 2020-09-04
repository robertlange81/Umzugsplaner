package com.planner.generic.checklist.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Log;

import com.planner.generic.checklist.Helpers.Comparators.ComparatorConfig;
import com.planner.generic.checklist.Helpers.Comparators.ComparatorSortable;
import com.planner.generic.checklist.Helpers.DBConverter.LinkMapConverter;
import com.planner.generic.checklist.Helpers.DBConverter.PriorityConverter;
import com.planner.generic.checklist.Helpers.DBConverter.TaskTypeConverter;
import com.planner.generic.checklist.Helpers.DBConverter.TimestampConverter;
import com.planner.generic.checklist.Helpers.DBConverter.UuidConverter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Task implements Serializable {

    public static final String TABLE_NAME = "Task";

    private static final List<Task> TASK_LIST = new ArrayList<Task>();
    public static final Map<UUID, Task> TASK_MAP = new HashMap<UUID, Task>();
    public static ReentrantLock lock = new ReentrantLock();

    /*
    - Recycler View direkt am ContentProvider registrieren
    - UpdaterThread loswerden
    - Bright vs Dark Theme
    - letzte Suche merken
    - Orientierung Tablet
    - Content Provider ???
    - Ortsangabe Task
    - neues Icon: Schloss mit Virus

    - in Liste bei markierter Aufgabe fixieren
    - Import
    - Ortsangabe auch bei Neustart auf Liste / Dialog
    - MyHammer Alternative
    - negative Beträge
    - Icon / Bild zu jedem Task (im Hintergrund)
    - Hintergrundbild (Grundriss / Baby)
    - Bilder speichern
    - Darcula wechseln

    - Babyliste
    - Geburstagsliste
    - Hochzeitsliste
    - Synchronisieren
    - Bild statt Link
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
    public com.planner.generic.checklist.Model.Priority priority; // 4

    @ColumnInfo(name = "date")
    @TypeConverters({TimestampConverter.class})
    public Date date; // 8

    @ColumnInfo(name = "createdAt")
    @TypeConverters({TimestampConverter.class})
    public Date createdAt; // 8

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

    @ColumnInfo(name = "locationZip")
    public String locationZip; // 256

    @ColumnInfo(name = "locationPlace")
    public String locationPlace; // 512

    @ColumnInfo(name = "locationStreet")
    public String locationStreet; // 1024

    @ColumnInfo(name = "locationStreetNumber")
    public String locationStreetNumber; // 2048

    /**
     * ID für eine Auflistung
     */
    public static final int ITEM_LIST_ID = 100;

    /**
     * ID für einen Datensatz
     */
    public static final int ITEM_ID = 101;

    private static ComparatorConfig comparatorConfig;

    public Task(String _name,
                String _description,
                Date _date,
                Priority _priority,
                long _costs,
                TaskType _type,
                Location location
    ) {
        this(_name, _description);
        this.date = _date;
        this.priority = _priority;
        this.costs = _costs;
        this.type = _type;

        if(location != null) {
            this.locationZip = location.getPostal();
            this.locationPlace = location.getPlace();
            this.locationStreet = location.getStreet();
            this.locationStreetNumber = location.getStreetNumber();
        }
    }

    public Task(String name, String description) {
        this(UUID.randomUUID(), name, description);
    }

    public Task(UUID uuid, String name, String description) {
        this.id = uuid;
        this.name = name;
        this.description = description;
        date = null;
        priority = com.planner.generic.checklist.Model.Priority.Normal;
        costs = 0L;
        type = new TaskType(TaskTypeMain.MEDICIN);
        links = new TreeMap<>();
        createdAt = new Date();
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
        createdAt = clone.createdAt;
        locationPlace = clone.locationPlace;
        locationStreet = clone.locationStreet;
        locationStreetNumber = clone.locationStreetNumber;
        locationZip = clone.locationZip;
    }

    public static synchronized List<Task> getTaskList() {
        return TASK_LIST;
    }

    public static synchronized List<Task> getTaskListClone() {
        lock.lock();
        List<Task> clone = new ArrayList<Task>();
        for(Task task: TASK_LIST) {
            clone.add(new Task(task));
        }
        lock.unlock();
        return clone;
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

    public static synchronized void addTask(Task task) {
        lock.lock();
        TASK_LIST.add(task);
        TASK_MAP.put(task.id, task);
        lock.unlock();
    }

    public static synchronized void removeTask(Task task) {
        TASK_LIST.remove(task);
        TASK_MAP.remove(task.id);
    }

    public static synchronized void clearAll() {
        lock.lock();
        TASK_MAP.clear();
        // TASK_MAP.remove(TASK_LIST);
        TASK_LIST.removeAll(TASK_LIST);
        lock.unlock();
    }

    public static synchronized long sumCosts(boolean onlyDone) {
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
                Log.e("ERROR", "Task.equals: " + ex.getMessage());
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


    public static boolean SortBy(ComparatorConfig.SortType sortType) {

        if(comparatorConfig == null)
            comparatorConfig = new ComparatorConfig();

        if(comparatorConfig.sortableMap != null) {
            ComparatorSortable comparatorSortable = comparatorConfig.sortableMap.get(sortType);
            if(comparatorSortable != null) {
                Task.lock.lock();
                Collections.sort(Task.getTaskList(), comparatorSortable);
                Task.lock.unlock();
                // JAVA 8
                //.thenComparing(new PriorityComparator())
                //.thenComparing(new NameComparator()));
                return true;
            }
        }

        return false;
    }

    public void destroy() {
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
