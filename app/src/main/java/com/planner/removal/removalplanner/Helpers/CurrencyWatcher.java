package com.planner.removal.removalplanner.Helpers;

import android.text.Editable;
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
        Log.d("Costs", "afterTextChanged");
        et.removeTextChangedListener(this);

        if (s != null && !s.toString().isEmpty()) {
            try {
                int inilen, endlen;
                inilen = et.getText().toString().replaceAll("[^0-9.,]+","").length();
                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$", "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();

                Double d = n.doubleValue() * 100;
                Long l = d.longValue();
                if(!l.equals(_task.Costs)) {
                    _task.Costs = d.longValue();
                    MainActivity.notifyTaskChanged();
                    String currText = Formater.intCentToString(_task.Costs);
                    et.setText(currText);

                    endlen = et.getText().toString().replaceAll("[^0-9.,]+","").length();
                    if(endlen - inilen != 3) {
                        int sel = (cp + (endlen - inilen));
                        if (sel > 0 && sel < et.getText().length()) {
                            et.setSelection(sel);
                        }
                    } else {
                        if(currText.substring(0, 1).matches("\\d")) {
                            et.setSelection(cp);
                        } else {
                            et.setSelection(cp + 1);
                        }
                    }
                }
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
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
