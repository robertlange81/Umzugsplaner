package com.planner.removal.removalplanner.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Task;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CurrencyWatcher implements android.text.TextWatcher {

    private final DecimalFormat df;
    private final DecimalFormat dfnd;
    private final EditText et;
    private final Task _task;
    private boolean hasFractionalPart;
    private int trailingZeroCount;

    public CurrencyWatcher(EditText editText, Task task, String pattern) {
        df = new DecimalFormat(pattern);
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###.00");
        this.et = editText;
        hasFractionalPart = false;
        this._task = task;
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("costs", "afterTextChanged");
        et.removeTextChangedListener(this);

        if (s != null && !s.toString().isEmpty()) {
            try {
                int inilen, endlen;
                inilen = et.getText().length();
                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$", "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    StringBuilder trailingZeros = new StringBuilder();
                    while (trailingZeroCount-- > 0)
                        trailingZeros.append('0');
                    et.setText(df.format(n) + trailingZeros.toString());
                } else {
                    et.setText(dfnd.format(n));
                }
                Double d = n.doubleValue() * 100;
                _task.costs = d.longValue();

                et.setText(et.getText().toString());
                //et.setText(Formater.intCentToString(_task.costs));

                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel < et.getText().length()) {
                    et.setSelection(sel);
                } else if (trailingZeroCount > -1) {
                    et.setSelection(et.getText().length() - 3);
                } else {
                    et.setSelection(et.getText().length());
                }
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }

            MainActivity.notifyTaskChanged();
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        if (index > -1) {
            for (index++; index < s.length(); index++) {
                if (s.charAt(index) == '0')
                    trailingZeroCount++;
                else {
                    trailingZeroCount = 0;
                }
            }
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}
