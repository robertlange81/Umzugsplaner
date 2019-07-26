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

        if (s == sigNumbers.getEditableText()) {
            _task.costs = _task.costs % 100;
            sigNumbersChanged(sigNumbers);
        } else if (s == fractionNumbers.getEditableText()) {
            _task.costs = (_task.costs / 100) * 100;
            fracNumbersChanged(fractionNumbers);
        }

    }

    private void sigNumbersChanged(EditText current) {

        sigNumbers.removeTextChangedListener(this);

        if (current.getEditableText() != null && !current.getEditableText().toString().isEmpty()) {
            try {
                String oldSig = current.getText().toString();
                String newStringCurrent = oldSig
                        .replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "")
                        .replaceAll("[^0-9.,-]", "");

                newStringCurrent = newStringCurrent.equals("-") ? "-0" : newStringCurrent;
                Long newNumber = df.parse(newStringCurrent).longValue();

                _task.costs += newNumber * 100;

                MainActivity.NotifyTaskChanged(_task, null);

                String currText = TaskFormater.intDecimalsToString(_task.costs);

                if(currText != null && currText.length() > 0) {
                    int cp = current.getSelectionStart();
                    String newSig = currText.substring(0, currText.length() - 3);
                    sigNumbers.setText(newSig); // exclude fractions

                    current.setSelection(
                        Math.max(
                          0,
                          Math.min(newSig.length(), cp + (newSig.length() - oldSig.length()))
                        )
                    );
                }

            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        sigNumbers.addTextChangedListener(this);
    }

    private void fracNumbersChanged(EditText current) {

        fractionNumbers.removeTextChangedListener(this);

        if (current.getEditableText() != null && !current.getEditableText().toString().isEmpty()) {
            try {
                String newStringCurrent = current.getText().toString()
                        .replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");

                newStringCurrent = newStringCurrent.equals("-") ? "-0" : newStringCurrent;
                Long newNumber = df.parse(newStringCurrent).longValue();

                _task.costs += newNumber;

                MainActivity.NotifyTaskChanged(_task, null);

            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        fractionNumbers.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
