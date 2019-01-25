package com.planner.removal.removalplanner;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AufgabenAdapter extends ArrayAdapter<Aufgabe> {
    private final Context context;
    private final List<Aufgabe> values;

    public AufgabenAdapter(Context context, ArrayList<Aufgabe> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView name = rowView.findViewById(R.id.name);
        name.setText(values.get(position).Name);

        final CheckBox checkBox = rowView.findViewById(R.id.checkBox);
        final Aufgabe aufgabe = values.get(position);
        checkBox.setChecked(aufgabe.istErledigt);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = AufgabenAdapter.this.context.getResources().getString(checkBox.isChecked() ? R.string.erledigt : R.string.offen);
                aufgabe.istErledigt = checkBox.isChecked();
                Snackbar.make(view, aufgabe.Name + " " + msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(values.get(position).Termin != null) {
            TextView termin = (TextView) rowView.findViewById(R.id.termin);
            String terminTxt = Helper.formatDateTo(values.get(position).Termin);
            termin.setText(terminTxt);
        }

        TextView kosten = (TextView) rowView.findViewById(R.id.kosten);
        kosten.setText(Helper.intCentToString(values.get(position).Kosten));

        TextView typ = (TextView) rowView.findViewById(R.id.typ);
        if(values.get(position).Typ != null)
            typ.setText(values.get(position).Typ.toString());

        final ImageView imgPrio = (ImageView) rowView.findViewById(R.id.icon);

        final Prio prio = values.get(position).Prio;

        if (prio == Prio.Hoch) {
            imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
        }

        imgPrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aufgabe act = values.get(position);
                if (act.Prio == Prio.Hoch) {
                    imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
                    act.Prio = Prio.Normal;
                } else {
                    imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
                    act.Prio = Prio.Hoch;
                }

                Snackbar snack = Snackbar.make(view, AufgabenAdapter.this.context.getResources().getString(act.Prio == Prio.Normal ? R.string.normalePrioText : R.string.hohePrioText) + " " + act.Name, Snackbar.LENGTH_LONG);
                snack.show();
            }
        });

        final ImageView imgLoeschen = (ImageView) rowView.findViewById(R.id.loeschen);
        imgLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ((View) view.getParent().getParent()).animate().setDuration(300).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Aufgabe toRemove = getItem(position);
                                AufgabenAdapter.this.remove(toRemove);
                                AufgabenAdapter.this.notifyDataSetChanged();
                                ((View) view.getParent().getParent()).setAlpha(1);

                                Snackbar snack = Snackbar.make(view, aufgabe.Name + " " + AufgabenAdapter.this.context.getResources().getString(R.string.geloescht), Snackbar.LENGTH_LONG);
                                snack.setAction(
                                        R.string.undo,
                                        new Kommando(Kommando.KommandoTyp.Add, AufgabenAdapter.this, toRemove)
                                );
                                snack.show();
                            }
                        });
            }
        });

        return rowView;
    }
}