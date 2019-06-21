package com.planner.removal.removalplanner.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.planner.removal.removalplanner.R;

public class DetailDialogFragment extends DialogFragment {

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
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_costs, null))
                .setTitle("Kosten")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
        TextView txtPaidUndone = alert.findViewById(R.id.paid_undone);
        if(txtPaidUndone != null)
            txtPaidUndone.setText(msg[0]);

        TextView txtPaidDone = alert.findViewById(R.id.paid_done);
        if(txtPaidDone != null)
            txtPaidDone.setText(msg[1]);

        TextView txtPaidSum = alert.findViewById(R.id.paid_sum);
        if(txtPaidSum != null)
            txtPaidSum.setText(msg[2]);

        return alert;
        // https://www.amazon.de/dp/B079Z3DVC2/ref=cm_sw_em_r_mt_dp_U_jlntCbVXDEPCV
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
