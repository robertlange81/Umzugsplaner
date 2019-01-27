package com.planner.removal.removalplanner.Model;

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
        aufgabe2.Termin = zielTermin;

        Aufgabe aufgabe3 = new Aufgabe("Aufgabe3", "Beschreibung3");
        aufgabe3.Kosten = 3000;
        aufgabe3.Prio = Prio.Hoch;
        Date zielTermin3 = new Date(zielTermin.getTime());
        zielTermin3.setYear(119);
        aufgabe3.Termin = zielTermin3;

        Aufgabe aufgabe4 = new Aufgabe("Aufgabe4", "Beschreibung4 sehr lang, bla bmla ");
        aufgabe4.Kosten = 800000;
        aufgabe4.Prio = Prio.Normal;
        Date zielTermin4 = new Date(119, 0, 1);
        zielTermin4.setHours(10);
        aufgabe4.Termin = zielTermin4;
        aufgabe4.istErledigt = true;

        Aufgabe[] aufgaben = {aufgabe1, aufgabe2, aufgabe3, aufgabe4};

        return new ArrayList<Aufgabe>(Arrays.asList(aufgaben));
    }
}
