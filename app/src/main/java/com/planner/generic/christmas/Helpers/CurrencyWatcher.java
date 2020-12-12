package com.planner.generic.christmas.Helpers;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.planner.generic.christmas.Model.Task;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CurrencyWatcher implements android.text.TextWatcher {

  private final DecimalFormat df;
  private final EditText sigNumbers, fractionNumbers;
  private Task _task;

  public CurrencyWatcher(EditText sigNumbers, EditText fractionNumbers, Task task, String pattern) {
    df = new DecimalFormat(pattern);
    df.setDecimalSeparatorAlwaysShown(true);
    this.sigNumbers = sigNumbers;
    this.fractionNumbers = fractionNumbers;
    this._task = task;
  }

  @Override
  public void afterTextChanged(Editable s) {

    if(_task == null)
      return;

    if (s == sigNumbers.getEditableText()) {
      _task.costs = _task.costs % 100;
      sigNumbersChanged(sigNumbers);
    } else if (s == fractionNumbers.getEditableText()) {
      _task.costs = (_task.costs / 100) * 100;
      fracNumbersChanged(fractionNumbers);
    }
  }

  private void sigNumbersChanged(EditText current) {

    if(_task == null)
      return;

    sigNumbers.removeTextChangedListener(this);

    if (current.getEditableText() != null && !current.getEditableText().toString().isEmpty()) {
      try {
        Log.d("sigNumbers", _task.id.toString());
        String oldSig = current.getText().toString();
        String newStringCurrent = oldSig
                .replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "")
                .replace(TaskFormater.getNumberFormat().getCurrency().getSymbol(), "")
                .replaceAll("[^0-9.,-]", "");

        newStringCurrent = newStringCurrent.trim().equals("-") ? "-0" : newStringCurrent;
        Long newNumber = df.parse(newStringCurrent).longValue();

        if (newNumber < 0) {
          _task.costs = newNumber * 100 - _task.costs;
        } else {
          _task.costs = newNumber * 100 + _task.costs;
        }

        String currText = TaskFormater.intSigToString(_task.costs);

        if (currText != null && currText.length() > 0) {
          int cp = current.getSelectionStart();
          sigNumbers.setText(currText);

          current.setSelection(
                  Math.max(
                          0,
                          Math.min(currText.length(), cp + (currText.length() - oldSig.length()))
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

    if (current.getEditableText() != null) {
      try {
        String newStringCurrent = current.getText().toString()
                .replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "")
                .replace(TaskFormater.getNumberFormat().getCurrency().getSymbol(), "")
                .replaceAll("[^\\d]", "");

        newStringCurrent = newStringCurrent.equals("-") ? "-0" : newStringCurrent;
        Long newNumber = newStringCurrent.isEmpty() ? 0L : df.parse(newStringCurrent).longValue();

        _task.costs += newNumber;

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

  public void setTask(Task t) {
    _task = t;
  }

  public void destroy() {
    this._task = null;
  }
}
