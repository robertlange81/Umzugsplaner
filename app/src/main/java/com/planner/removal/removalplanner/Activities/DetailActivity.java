package com.planner.removal.removalplanner.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.planner.removal.removalplanner.Adapter.AufgabenAdapter;
import com.planner.removal.removalplanner.Helfer;
import com.planner.removal.removalplanner.Model.Aufgabe;
import com.planner.removal.removalplanner.Model.Prio;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public static DetailActivity instance;
    public static final String ARG_AUFGABE_ID = "aufgabe_id";

    Button btnDatePicker, btnTimePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        _initializeElements();

        instance = this;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    /**
     * On click listener that
     * finish the activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        this.finish();
        return true;
    }


    private void _initializeElements()
    {
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        /*
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        final ImageView imgPrio = (ImageView) findViewById(R.id.icon_fav_haupt);
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

                Snackbar snack = Snackbar.make(view, DetailActivity.this.getResources().getString(act.Prio == Prio.Normal ? R.string.normalePrioText : R.string.hohePrioText) + " " + act.Name, Snackbar.LENGTH_LONG);
                snack.show();
            }
        });
        */
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                            }

                            tempDate.setYear(year);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDate.setText(Helfer.formatDateToSring(tempDate));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                            }

                            tempDate.setHours(hourOfDay);
                            tempDate.setMinutes(minute);

                            txtDate.setText(Helfer.formatDateToSring(tempDate));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
