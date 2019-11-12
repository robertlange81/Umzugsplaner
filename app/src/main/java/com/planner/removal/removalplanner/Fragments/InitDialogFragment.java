package com.planner.removal.removalplanner.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.planner.removal.removalplanner.Helpers.TaskFormater;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.R;

import java.util.Calendar;
import java.util.Date;

public class InitDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    // https://guides.codepath.com/android/using-dialogfragment
    // https://developer.android.com/guide/topics/ui/controls/pickers#java
    AlertDialog alert;
    Button btnDatePicker;
    Button btnTimePicker;
    CheckBox checkBoxDeleteOld;
    EditText txtDeadline;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;

    // Use this instance of the interface to deliver action events
    private InitDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] msg  = getArguments().getStringArray("message");

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View view = inflater.inflate(R.layout.dialog_init, null);

        btnDatePicker =(Button) view.findViewById(R.id.init_btn_date);
        btnTimePicker =(Button) view.findViewById(R.id.init_btn_time);
        txtDeadline = (EditText) view.findViewById(R.id.detail_deadline);
        checkBoxDeleteOld = (CheckBox) view.findViewById(R.id.init_checkBox_delete_old);

        if(Task.TASK_LIST != null && Task.TASK_LIST.size() == 0) {
            checkBoxDeleteOld.setVisibility(View.GONE);
        } else {
            checkBoxDeleteOld.setVisibility(View.VISIBLE);
        }

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        builder.setView(view)
                .setTitle(getResources().getString(R.string.placeholder_init))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) mListener);

        alert = builder.create();

        alert.show();
        return alert;
        // FIXME
        // https://www.amazon.de/dp/B079Z3DVC2/ref=cm_sw_em_r_mt_dp_U_jlntCbVXDEPCV
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }

    public void setmListener(InitDialogListener mListener) {
        this.mListener = mListener;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InitDialogListener extends DialogInterface.OnClickListener{

    }

    // Override the Fragment.onAttach() method to instantiate the InitDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the InitDialogListener so we can send events to the host
            setmListener((InitDialogListener) context);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Main Activity must implement InitDialogListener");
        }
    }

    public void onClick(View v) {

        if (v == btnDatePicker) {
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
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                                tempDate.setHours(0);
                                tempDate.setMinutes(0);
                            }

                            tempDate.setYear(year - 1900);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
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

                            txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    public Date getRemovalDate() {
        return tempDate;
    }

    public boolean getDeleteOld() {
        return checkBoxDeleteOld.isChecked();
    }
}
