package com.planner.removal.removalplanner;

import java.util.Date;

public class Aufgabe {
    public Aufgabe(String name, String beschreibung) {
        Name = name;
        Beschreibung = beschreibung;
    }
    public String Name;
    public String Beschreibung;
    public Prio Prio;
    public Date Termin;
    public boolean istErledigt;
    public Typ Typ;
    public int Kosten; // in Cent
    public String Link;
}
