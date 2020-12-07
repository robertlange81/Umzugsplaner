package com.planner.removal.removalplanner.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.planner.removal.removalplanner.Helpers.TaskFormater;
import com.planner.removal.removalplanner.Model.Location;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Date;

public class IntroFragmentInput extends Fragment  implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    Button btnDatePicker;
    Button btnTimePicker;
    // Button btnLocationPicker;
    EditText txtDeadline, txtPostal, txtStreet, txtPlace, txtHouseNumber;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate and set the layout for the dialog
        View view = inflater.inflate(R.layout.fragment_intro_input, null);

        View delete = view.findViewById(R.id.init_delete_location_icon_street);
        if(delete != null)
            delete.setVisibility(View.GONE);
        delete = view.findViewById(R.id.init_delete_location_icon_street_number);
        if(delete != null)
            delete.setVisibility(View.GONE);
        delete = view.findViewById(R.id.init_delete_location_icon_postal);
        if(delete != null)
            delete.setVisibility(View.GONE);
        delete = view.findViewById(R.id.init_delete_location_icon_PostalAddress);
        if(delete != null)
            delete.setVisibility(View.GONE);

        btnDatePicker =(Button) view.findViewById(R.id.init_btn_date);
        btnTimePicker =(Button) view.findViewById(R.id.init_btn_time);
        txtDeadline = (EditText) view.findViewById(R.id.init_deadline);
        txtPostal = (EditText) view.findViewById(R.id.init_postal);
        txtPlace = (EditText) view.findViewById(R.id.init_place);
        txtStreet = (EditText) view.findViewById(R.id.init_street);
        txtHouseNumber = (EditText) view.findViewById(R.id.init_house_number);
        //btnLocationPicker =(Button) view.findViewById(R.id.init_btn_location);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        txtDeadline.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void onClick(View v) {

        if (v == btnDatePicker || v == txtDeadline) {
            // Get Current date
            final Calendar c = Calendar.getInstance();
            mYear = tempDate != null ? tempDate.getYear() + 1900 : c.get(Calendar.YEAR);
            mMonth = tempDate != null ? tempDate.getMonth() : c.get(Calendar.MONTH);
            mDay = tempDate != null ? tempDate.getDate() : c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if(tempDate == null) {
                                try {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                                tempDate.setHours(0);
                                tempDate.setMinutes(0);
                                } catch (Exception x) {
                                    Log.d("Error", "onDateSet: " + x.getMessage());
                                }
                            }

                            tempDate.setYear(year - 1900);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDeadline.setText(TaskFormater.formatDateToString(tempDate));
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = tempDate != null ? tempDate.getHours() : c.get(Calendar.HOUR_OF_DAY);
            mMinute = tempDate != null ? tempDate.getMinutes() : c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                            }

                            tempDate.setHours(hourOfDay);
                            tempDate.setMinutes(minute);

                            txtDeadline.setText(TaskFormater.formatDateToString(tempDate));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        /*
        if (v == btnLocationPicker) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            getActivity().startActivity(intent);
        }
        */
    }

    public Date getRemovalDate() {
        return tempDate;
    }

    public Location getRemovalLocation() {

        if(this.txtPlace == null || this.txtPostal == null || this.txtStreet == null || this.txtHouseNumber == null)
            return null;

        // at one field must be set ; TODO: change location later
        if(this.txtPlace.getText().toString().isEmpty() && this.txtPostal.getText().toString().isEmpty()
            && this.txtStreet.getText().toString().isEmpty() && this.txtHouseNumber.getText().toString().isEmpty()) {
            return null;
        }

        Location l = new Location(
                this.txtPostal.getText().toString(),
                this.txtPlace.getText().toString(),
                this.txtStreet.getText().toString(),
                this.txtHouseNumber.getText().toString()
        );

        return l;
    }
}