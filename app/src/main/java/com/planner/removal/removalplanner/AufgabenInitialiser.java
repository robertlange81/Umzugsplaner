package com.planner.removal.removalplanner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AufgabenInitialiser {

    public static List<Aufgabe> GetStandardAufgaben (Date zielTermin) {

        Aufgabe aufgabe1 = new Aufgabe("Aufgabe1 seh lang bla bla bla bla bla", "Beschreibung1");
        aufgabe1.Kosten = 5000;
        aufgabe1.Termin = zielTermin;
        aufgabe1.Link = "Link";
        aufgabe1.Prio = Prio.Hoch;
        aufgabe1.Typ = Typ.Elektronik;

        Aufgabe aufgabe2 = new Aufgabe("Aufgabe2", "Beschreibung2");
        aufgabe2.Kosten = 3000;
        aufgabe1.Prio = Prio.Hoch;
        Aufgabe[] aufgaben = {aufgabe1, aufgabe2};

        return Arrays.asList(aufgaben);
    }
}
