package com.planner.removal.removalplanner.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorConfig;
import com.planner.removal.removalplanner.R;
// TODO : Remove, if not needed
public class DetailDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] charSequence = new CharSequence[] {"As Guest","I have account here"};

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setMessage(R.string.placeholder_edit_task)
                .setPositiveButton(R.string.placeholder_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.placeholder_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setSingleChoiceItems(charSequence, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int sortId) {
                        if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                            MainActivity.SortBy(ComparatorConfig.SortType.values()[sortId]);
                        }
                    }
                });

        // https://www.amazon.de/dp/B079Z3DVC2/ref=cm_sw_em_r_mt_dp_U_jlntCbVXDEPCV
        AlertDialog ad = builder.create();
        return ad;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DetailDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DetailDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the DetailDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DetailDialogListener so we can send events to the host
            mListener = (DetailDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Main Activity must implement DetailDialogListener");
        }
    }
}
