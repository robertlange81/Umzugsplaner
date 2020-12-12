package com.planner.generic.christmas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.planner.generic.christmas.R;

public class DialogFragmentCosts extends DialogFragment {

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
                .setTitle(getResources().getString(R.string.placeholder_costs) + " - " + getResources().getString(R.string.placeholder_overview))
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
    }
}
