package com.planner.removal.removalplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AufgabenInitialiser {

    public static ArrayList<Aufgabe> GetStandardAufgaben (Date zielTermin) {

        Aufgabe aufgabe1 = new Aufgabe("Aufgabe1 seh lang bla bla bla bla bla", "Beschreibung1");
        aufgabe1.Link = "Link";
        aufgabe1.Prio = Prio.Normal;
        aufgabe1.Typ = Typ.Elektronik;

        Aufgabe aufgabe2 = new Aufgabe("Aufgabe2", "Beschreibung2");
        aufgabe2.Kosten = 30000098739l;
        aufgabe2.Prio = Prio.Normal;

        Aufgabe aufgabe3 = new Aufgabe("Aufgabe3", "Beschreibung3");
        aufgabe3.Kosten = 3000;
        aufgabe3.Prio = Prio.Hoch;
        Date zielTermin3 = new Date(zielTermin.getTime());
        zielTermin3.setHours(10);
        aufgabe3.Termin = zielTermin;


        Aufgabe[] aufgaben = {aufgabe1, aufgabe2, aufgabe3};

        return new ArrayList<Aufgabe>(Arrays.asList(aufgaben));
    }
}
