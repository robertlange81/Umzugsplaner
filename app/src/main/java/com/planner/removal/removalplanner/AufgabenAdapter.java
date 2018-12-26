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
import java.util.Formatter;
import java.util.List;

public class AufgabenAdapter extends ArrayAdapter<Aufgabe> {
    private final Context context;
    private final List<Aufgabe> values;

    public AufgabenAdapter(Context context, List<Aufgabe> values) {
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

        CheckBox checkBox = rowView.findViewById(R.id.checkBox);
        checkBox.setEnabled(values.get(position).istErledigt);
        final String aufgabenName = values.get(position).Name;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, aufgabenName + "erledigt", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(values.get(position).Termin != null) {
            TextView termin = (TextView) rowView.findViewById(R.id.termin);
            termin.setText(Helper.formatDateTo(values.get(position).Termin));
        }

        TextView kosten = (TextView) rowView.findViewById(R.id.kosten);
        kosten.setText(Helper.intCentToString(values.get(position).Kosten));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        // change the icon for Windows and iPhone
        //String s = values.get(position).getName();
        /*
        if (s.startsWith("iPhone")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }
}