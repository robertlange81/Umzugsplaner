package com.planner.removal.removalplanner;

import android.view.View;
import android.widget.ArrayAdapter;

public class Kommando implements View.OnClickListener{

    public enum KommandoTyp {
        Add, Remove
    }

    KommandoTyp _kommandoTyp;
    ArrayAdapter<Aufgabe> _adapter;
    Aufgabe _aufgabe;

    public Kommando(KommandoTyp kommandoTyp, ArrayAdapter<Aufgabe> adapter, Aufgabe aufgabe) {
        _kommandoTyp = kommandoTyp;
        _adapter = adapter;
        _aufgabe = aufgabe;
    }

    @Override
    public void onClick(View v) {
        if(_kommandoTyp == KommandoTyp.Add) {
            _adapter.add(_aufgabe);
        } else {
            _adapter.remove(_aufgabe);
        }
        _adapter.notifyDataSetChanged();
    }
}
