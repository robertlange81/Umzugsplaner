package com.planner.removal.removalplanner.Model;

import java.util.Date;

public class Aufgabe {
    public Aufgabe(String name, String beschreibung) {
        Name = name;
        Beschreibung = beschreibung;
        Termin = new Date(Long.MAX_VALUE);
        Prio = com.planner.removal.removalplanner.Model.Prio.Normal;
        Kosten = 0;
    }
    public String Name;
    public String Beschreibung;
    public com.planner.removal.removalplanner.Model.Prio Prio;
    public Date Termin;
    public boolean istErledigt;
    public com.planner.removal.removalplanner.Model.Typ Typ;
    public long Kosten; // in Cent
    public String Link;
}
