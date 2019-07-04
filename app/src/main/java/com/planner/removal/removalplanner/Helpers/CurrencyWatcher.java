package com.planner.removal.removalplanner.Helpers;

import android.text.Editable;
import android.widget.EditText;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Task;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CurrencyWatcher implements android.text.TextWatcher {

    private final DecimalFormat df;
    private final EditText sigNumbers, fractionNumbers;
    private final Task _task;
    private boolean hasFractionalPart;
    private int trailingZeroCount;

    public CurrencyWatcher(EditText sigNumbers, EditText fractionNumbers, Task task, String pattern) {
        df = new DecimalFormat(pattern);
        df.setDecimalSeparatorAlwaysShown(true);
        this.sigNumbers = sigNumbers;
        this.fractionNumbers = fractionNumbers;
        hasFractionalPart = false;
        this._task = task;
    }

    @Override
    public void afterTextChanged(Editable s) {

        sigNumbers.removeTextChangedListener(this);
        fractionNumbers.removeTextChangedListener(this);

        EditText current = null;

        if (s == sigNumbers.getEditableText()) {
            current = sigNumbers;
            _task.costs = _task.costs % 100;
        } else if (s == fractionNumbers.getEditableText()) {
            current = fractionNumbers;
            _task.costs = (_task.costs / 100) * 100;
        }

        if (s != null && !s.toString().isEmpty()) {
            try {
                String newStringCurrent = current.getText().toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "")
                        .replace("$", "")
                        .replace("€", "");
                Long newNumber = df.parse(newStringCurrent).longValue();

                _task.costs += current == sigNumbers ? (newNumber * 100) : newNumber;

                MainActivity.NotifyTaskChanged(_task, null);

                // String currText = TaskFormater.intDecimalsToString(_task.costs);

                /*
                if(currText != null && currText.length() > 0) {
                    int cp = current.getSelectionStart();
                    sigNumbers.setText(currText.substring(0, currText.length() - 3));
                    fractionNumbers.setText(currText.substring(currText.length() - 2));

                    if(currText.substring(0, 1).matches("\\d")) {
                        current.setSelection(cp);
                    } else {
                        current.setSelection(cp + 1);
                    }
                }
                */
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        sigNumbers.addTextChangedListener(this);
        fractionNumbers.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
