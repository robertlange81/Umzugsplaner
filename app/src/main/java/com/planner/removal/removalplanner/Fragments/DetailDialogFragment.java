package com.planner.removal.removalplanner.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TabStopSpan;
import android.view.Gravity;
import android.widget.TextView;
import com.planner.removal.removalplanner.R;

public class DetailDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String msg  = getArguments().getString("message");

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        TextView msgTxtView = new TextView(getActivity());

        //SpannableStringBuilder span = new SpannableStringBuilder(msg);
        //span.setSpan(new TabStopSpan.Standard(300), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        //msgTxtView.setText( span, TextView.BufferType.SPANNABLE);
        msgTxtView.setText(msg);
        msgTxtView.setTextSize(22);
        msgTxtView.setPadding( 10, 100, 100, 10 );
        msgTxtView.setGravity(Gravity.END | Gravity.CENTER);
        msgTxtView.setBackgroundColor(0xFF770033);
        msgTxtView.setHeight(600);

        builder.setView(msgTxtView);

        builder.setView(msgTxtView)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
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
