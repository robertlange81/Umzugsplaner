package com.planner.virus.corona.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.planner.virus.corona.Activities.MainActivity;
import com.planner.virus.corona.Helpers.Command;
import com.planner.virus.corona.Helpers.CurrencyWatcher;
import com.planner.virus.corona.Helpers.TaskFormater;
import com.planner.virus.corona.Model.Priority;
import com.planner.virus.corona.Model.Task;
import com.planner.virus.corona.Model.TaskType;
import com.planner.virus.corona.Model.TaskTypeMain;
import com.planner.virus.corona.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a
 * {@link MainActivity} in two-pane mode (on tablets)
 * or a {@link com.planner.virus.corona.Activities.DetailActivity} on handsets.
 */
public class TaskDetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String TASK_ID = "task_id";

  /**
   * The dummy content this fragment is presenting.
   */
  private Task _task;
  private static CurrencyWatcher currencyWatcher;
  public static TaskDetailFragment instance;

  EditText txtName;
  EditText txtDescription;
  CheckBox checkIsDone;
  TextView lblIsDone;
  ImageView imgPrio;
  TextView lblPrio;
  EditText txtCostsSig, txtCostsFractions;
  Spinner spinnerDetailType;
  Button btnDatePicker;
  Button btnTimePicker;
  Button btnExportPicker;
  EditText txtDeadline;
  TextView[] txtInputs;
  ImageView[] imgDeleteLinks;
  TableRow firstLinkRow;

  private int mYear, mMonth, mDay, mHour, mMinute;
  Date tempDate;

  boolean isNotifyEnabled = false;

  public static final int MAX_INPUT_FIELDS_FOR_LINKS = 5;
  private static View.OnClickListener clickListener;
  View rootView;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TaskDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("DEBUG", "onCreate of TaskDetailFragment");
    Log.d("DEBUG", "onCreate of TaskDetailFragment END");
  }

  private void createNewTask(String taskId) {
    _task = new Task(UUID.fromString(taskId), "", "");
    Task.addTask(_task);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Log.d("DEBUG", "onCreateView TaskDetailFragment");
    setHasOptionsMenu(true);
    rootView = inflater.inflate(R.layout.detail, container, false);
    Log.d("DEBUG", "onCreateView TaskDetailFragment 1");
    Log.d("DEBUG", "onCreateView TaskDetailFragment END");

    txtName = rootView.findViewById(R.id.detail_name);
    checkIsDone = rootView.findViewById(R.id.detail_isDone);
    lblIsDone = rootView.findViewById(R.id.detail_isDone_label);
    imgPrio = rootView.findViewById(R.id.detail_prio);
    lblPrio = rootView.findViewById(R.id.detail_prio_label);
    txtDescription = rootView.findViewById(R.id.description);
    txtCostsSig = rootView.findViewById(R.id.detail_costs);
    txtCostsFractions = rootView.findViewById(R.id.detail_costs_decimal);
    spinnerDetailType = (Spinner) rootView.findViewById(R.id.type);
    firstLinkRow = (TableRow) rootView.findViewById(R.id.detail_third_line_links0);

    btnDatePicker = (Button) rootView.findViewById(R.id.detail_btn_date);
    btnTimePicker = (Button) rootView.findViewById(R.id.detail_btn_time);
    btnExportPicker = (Button) rootView.findViewById(R.id.detail_btn_date_export);

    txtDeadline = (EditText) rootView.findViewById(R.id.detail_deadline);
    txtDeadline.setOnClickListener(this);

    txtInputs = new TextView[9];
    imgDeleteLinks = new ImageView[9];
    txtInputs[5] = txtDescription;
    txtInputs[6] = txtCostsSig;
    txtInputs[7] = txtCostsFractions;
    txtInputs[8] = txtDeadline;

    imgDeleteLinks[0] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x0);
    imgDeleteLinks[1] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x1);
    imgDeleteLinks[2] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x2);
    imgDeleteLinks[3] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x3);
    imgDeleteLinks[4] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x4);
    imgDeleteLinks[5] = (ImageView) rootView.findViewById(R.id.detail_delete_info_icon);
    imgDeleteLinks[6] = (ImageView) rootView.findViewById(R.id.detail_delete_costs_icon);
    imgDeleteLinks[7] = (ImageView) rootView.findViewById(R.id.detail_delete_costs_icon);
    imgDeleteLinks[8] = (ImageView) rootView.findViewById(R.id.detail_delete_deadline_icon);

    return rootView;
  }

  @Override
  public void onResume() {
    Log.d("DEBUG", "onResume of TaskDetailFragment");
    instance = this;
    isNotifyEnabled = false;
    Log.d("isNotifyEnabled", "onResume false");
    super.onResume();

    if (getArguments().containsKey(TASK_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      String taskId = getArguments().getString(TASK_ID);

      if(_task != null && _task.id.toString().equals(taskId)) {
        isNotifyEnabled = true;
        currencyWatcher = new CurrencyWatcher(txtCostsSig, txtCostsFractions, _task, "#,###");
        txtCostsSig.addTextChangedListener(currencyWatcher);
        txtCostsFractions.addTextChangedListener(currencyWatcher);
        return;
      }

      if(taskId != null)
        _task = Task.TASK_MAP.get(UUID.fromString(taskId));

      if (_task == null) {
        createNewTask(taskId);
      }
      currencyWatcher = new CurrencyWatcher(txtCostsSig, txtCostsFractions, _task, "#,###");
    }

    if (TaskFormater.currentLocal == null) {
      TaskFormater.setCurrentLocale(rootView.getContext());
    }

    _initListeners(rootView);
    final HashMap<TextView, String> linkMap = _initLinks();

    if (_task != null) {
      ArrayAdapter _categoryAdapter = new ArrayAdapter<String>(
        rootView.getContext(),
        R.layout.simple_spinner_dropdown,
        rootView.getContext().getResources().getStringArray(R.array.base_task_types)
      );
      spinnerDetailType.setAdapter(_categoryAdapter);
      setDetails(linkMap, rootView);
    }

    _initDeleteIcons(rootView);

    if (updaterThread == null)
      startTimerThread(rootView);

    if (_task.name == null || _task.name.isEmpty()) {
      txtName.requestFocus();
    } else {
      txtName.clearFocus();
      txtName.setSelected(false);
    }

    isNotifyEnabled = true;
    Log.d("isNotifyEnabled", "onResume true");
    Log.d("DEBUG", "onResume of TaskDetailFragment END");
  }

  private void _initListeners(final View rootView) {
    Log.d("DEBUG", "_initListeners");
    btnDatePicker.setOnClickListener(this);
    btnTimePicker.setOnClickListener(this);
    btnExportPicker.setOnClickListener(this);

    checkIsDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String msg = rootView.getContext().getResources()
          .getString(checkIsDone.isChecked() ? R.string.done : R.string.todo);
        _task.is_Done = checkIsDone.isChecked();
        lblIsDone.setText(msg);
        MainActivity.NotifyTaskChanged(_task, getActivity());
        Snackbar.make(view, _task.name + " " + msg, Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });

    imgPrio.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (_task.priority == Priority.High) {
          imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
          _task.priority = Priority.Normal;
          lblPrio.setText(R.string.normalPrioText_short);
        } else {
          imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
          _task.priority = Priority.High;
          lblPrio.setText(R.string.highPrioText_short);
        }

        MainActivity.NotifyTaskChanged(_task, getActivity());
        String msg = rootView.getContext().getResources()
          .getString(_task.priority == Priority.Normal ? R.string.normalPrioText : R.string.highPrioText)
          + " " + _task.name;

        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snack.show();
      }
    });

    txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && txtName.getText() != null) {
          String input = txtName.getText().toString();

          if (!input.equals(_task.name)) {
            _task.name = input;
            generateMarketLinks(null);
            MainActivity.NotifyTaskChanged(_task, getActivity());
          }
        }
      }
    });

    txtName.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if(s.toString() != _task.name && _task.type.getValue() >= 4) { // user type
          generateMarketLinks(s.toString());
        }
      }
    });

    spinnerDetailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (_task != null && _task.type.getValue() != position) {

          _task.type = new TaskType(position);
          generateMarketLinks(null);

          if (isNotifyEnabled)
            MainActivity.NotifyTaskChanged(_task, getActivity());
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    txtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        Log.d("DEBUG", "txt changed");
        if (!hasFocus && txtDescription.getText() != null) {
          String input = txtDescription.getText().toString();
          if (!_task.description.equals(input))
            _task.description = input;
        }
      }
    });

    txtCostsFractions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && isNotifyEnabled) {
          MainActivity.NotifyTaskChanged(_task, getActivity());
        }
      }
    });

    txtCostsSig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        Log.d("costs", "focus out");
        if (!hasFocus && txtCostsSig.getText() != null) {
          String input = txtCostsSig.getText().toString();

          if (!hasFocus && isNotifyEnabled)
            MainActivity.NotifyTaskChanged(_task, getActivity());
        }
      }
    });

    txtDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && txtDeadline.getText() != null) {
          String input = txtDeadline.getText().toString();

          if (input.equals("")) {
            _task.date = null;
            if (isNotifyEnabled)
              MainActivity.NotifyTaskChanged(_task, getActivity());
          }
        }
      }
    });
    Log.d("DEBUG", "_initListeners END");
  }

  private void setDetails(final HashMap<TextView, String> linkMap, View rootView) {

    Log.d("DEBUG", "setDetails");
    isNotifyEnabled = false;
    Log.d("isNotifyEnabled", "setDetails false");
    if (!txtName.getText().toString().equals(_task.name))
      txtName.setText(_task.name);

    if (_task.type != null) {
      int counter = -1;
      for (TaskTypeMain taskTypeMain : TaskTypeMain.values()) {
        counter++;

        // extra case for zero
        if (counter == 0 && _task.type.getValue() != 0)
          continue;

        if ((taskTypeMain.getValue() & _task.type.getValue()) == taskTypeMain.getValue()) {
          spinnerDetailType.setSelection(counter);
          break;
        }
      }
    }

    if (!txtDescription.getText().toString().equals(_task.description))
      txtDescription.setText(_task.description);

    if (!checkIsDone.isChecked() && _task.is_Done || checkIsDone.isChecked() && !_task.is_Done) {
      checkIsDone.setChecked(_task.is_Done);
      String msg = rootView.getContext().getResources()
        .getString(checkIsDone.isChecked() ? R.string.done : R.string.todo);
      lblIsDone.setText(msg);
    }

    if (_task.priority == Priority.High) {
      imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
      lblPrio.setText(R.string.highPrioText_short);
    } else {
      imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
      lblPrio.setText(R.string.normalPrioText_short);
    }

    if (linkMap != null)
      setLinks(linkMap);

    txtCostsSig.removeTextChangedListener(currencyWatcher);
    txtCostsSig.setText(_task.costs != 0 ? TaskFormater.intSigToString(_task.costs) : "");
    txtCostsSig.addTextChangedListener(currencyWatcher);

    txtCostsFractions.removeTextChangedListener(currencyWatcher);
    txtCostsFractions.setText(_task.costs != 0 ? TaskFormater.intFractionsToString(_task.costs) : "");
    txtCostsFractions.addTextChangedListener(currencyWatcher);

    if (_task.date != null) {
      tempDate = new Date(_task.date.getTime());
      txtDeadline.setText(TaskFormater.formatDateToSring(_task.date));
    } else {
      txtDeadline.setText("");
    }
    Log.d("DEBUG", "setDetails - END");
    isNotifyEnabled = true;
    Log.d("isNotifyEnabled", "setDetails true");
  }

  private void setLinks(HashMap<TextView, String> linkMap) {
    Log.d("DEBUG", "setLinks");

    if (_task.links != null && _task.links.size() > 0) {
      int i = 0;
      for (Map.Entry<String, String> entry : _task.links.entrySet()) {
        if (i > MAX_INPUT_FIELDS_FOR_LINKS)
          break;

        linkMap.put(txtInputs[i], entry.getKey());
        _formatLink(txtInputs[i], entry.getValue(), entry.getKey());
        i++;
      }

      // disable next
      // if(i < MAX_INPUT_FIELDS_FOR_LINKS)
      //    _formatLink(txtInputs[i], "", "");
      firstLinkRow.setVisibility(View.VISIBLE);
    } else {
      firstLinkRow.setVisibility(View.GONE);
    }
    Log.d("DEBUG", "setLinks - END");
  }

  private void _initDeleteIcons(final View rootView) {
    Log.d("DEBUG", "_initDeleteIcons");
    final HashMap<ImageView, List<TextView>> deleteMap = new HashMap<>();

    clickListener = new View.OnClickListener() {
      @Override
      public void onClick(final View view) {
        List<TextView> tlist = deleteMap.get(view);
        for (TextView t : tlist) {
          if (t.getText() != null && !t.getText().toString().equals("")) {

            String msg = (t.getTag() != null ? t.getTag().toString() : t.getText())
              + " " + rootView.getResources().getString(R.string.removed);

            Snackbar snack = Snackbar.make(
              view,
              msg,
              Snackbar.LENGTH_LONG);

            Task clone = new Task(_task);

            if (t.getTag() != null && Task.TASK_MAP.get(_task.id) != null) {
              snack.setAction(
                R.string.undo,
                new Command(Command.CommandTyp.Undo, clone, getActivity())
              );
              Log.d("new command", "new command");
              snack.show();
            }

            t.setText("");
            if (t == txtDeadline) {
              _task.date = null;
              if (isNotifyEnabled)
                MainActivity.NotifyTaskChanged(_task, getActivity());
            } else {
              t.requestFocus();
              t.clearFocus();
            }
          }
        }
      }
    };

    for (int i = 0; i < imgDeleteLinks.length; i++) {

      List<TextView> inputList = deleteMap.get(imgDeleteLinks[i]);
      if (inputList == null || inputList.size() == 0) {
        inputList = new ArrayList<>();
      }

      inputList.add(txtInputs[i]);
      deleteMap.put(imgDeleteLinks[i], inputList);

      imgDeleteLinks[i].setOnClickListener(clickListener);
    }
    Log.d("DEBUG", "_initDeleteIcons - END");
  }

  private HashMap<TextView, String> _initLinks() {
    Log.d("DEBUG", "_initLinks");
    final HashMap<TextView, String> linkMap = new HashMap<>();

    txtInputs[0] = (TextView) rootView.findViewById(R.id.links_0);
    txtInputs[1] = (TextView) rootView.findViewById(R.id.links_1);
    txtInputs[2] = (TextView) rootView.findViewById(R.id.links_2);
    txtInputs[3] = (TextView) rootView.findViewById(R.id.links_3);
    txtInputs[4] = (TextView) rootView.findViewById(R.id.links_4);

    for (final TextView txtLink : txtInputs) {

      if (txtLink.getTag() == null || !txtLink.getTag().toString().equalsIgnoreCase("Link"))
        continue;

      txtLink.setMovementMethod(LinkMovementMethod.getInstance());
      if (txtLink != txtInputs[0])
        ((TableRow) txtLink.getParent()).setVisibility(View.GONE);

      /* make money links readonly */
      txtLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          if (!hasFocus) {
            try {
              String inputLink = txtLink.getText().toString();
              inputLink = inputLink.replaceAll("\\s+", "");
              String key = linkMap.get(txtLink);
              if (key != null) {
                Log.d("remove ", key);
                _task.links.remove(key);
                linkMap.remove(txtLink);
              }

              if (!inputLink.equals("")) {
                String value = inputLink;
                if (!URLUtil.isValidUrl(value)) {
                  if (!value.contains("www")) {
                    value = "www." + value;
                  }

                  if (!value.contains("https")) {
                    value = "https://" + value;
                  }

                  if (URLUtil.isValidUrl(value)) {
                    _task.addLink(inputLink, value, true);
                    Log.d("put ", inputLink);
                    linkMap.put(txtLink, inputLink);
                  }
                }
              }
            } catch (Exception x) {
              Log.e("Exception", x.getMessage());
            }
          }
        }
      });
    }

    Log.d("DEBUG", "_initLinks - END");
    return linkMap;
  }

  // generate links for Electronis and Furniture
  public void generateMarketLinks(String searchFor) {
    if(searchFor == null || searchFor.isEmpty()) {
      searchFor = _task.name;
    }

    if(_task.type.getValue() >= 4 && searchFor != null && !searchFor.isEmpty()) { // user type

      TreeMap<String,String> newLinks = new TreeMap<>();
      for(Map.Entry<String,String> entry : _task.links.entrySet()) {
        if(!entry.getValue().contains("amazon") && !entry.getValue().contains("ebay")) {
          newLinks.put(entry.getKey(), entry.getValue());
        }
      }

      _task.links = newLinks;
      _task.addLink(
        getString(R.string.lookFor) + " " + searchFor + " " + getString(R.string.on) + " " + getResources().getString(R.string.Amazon),
        getResources().getString(_task.type.equals(TaskTypeMain.Electronics) ? R.string.amazon_generic_link_electronics : R.string.amazon_generic_link_kitchen) + searchFor
      );

      _task.addLink(
              getString(R.string.lookFor) + " " + searchFor + " " + getString(R.string.on) + " " + getResources().getString(R.string.Ebay),
              getResources().getString(R.string.ebay_generic_link) + searchFor
      );

      setLinks(_initLinks());
    }
  }

  private void _formatLink(TextView linkInput, String href, String displayLink) {
    String link = "";
    if (!href.isEmpty() && !displayLink.isEmpty())
      link += "<a href=\"" + href + "\"> " + displayLink + "</a>";

    linkInput.setText(Html.fromHtml(link));
    ((TableRow) linkInput.getParent()).setVisibility(View.VISIBLE);
  }

  public void onClick(View v) {

    if (v == btnDatePicker || v == txtDeadline) {
      // Get Current date
      final Calendar c = Calendar.getInstance();
      mYear = tempDate != null ? tempDate.getYear() + 1900 : c.get(Calendar.YEAR);
      mMonth = tempDate != null ? tempDate.getMonth() : c.get(Calendar.MONTH);
      mDay = tempDate != null ? tempDate.getDate() : c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog;
      DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          if (tempDate == null) {
            tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
            tempDate.setHours(0);
            tempDate.setMinutes(0);
          }

          tempDate.setYear(year - 1900);
          tempDate.setMonth(monthOfYear);
          tempDate.setDate(dayOfMonth);
          txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
          _task.date = tempDate;

          if (isNotifyEnabled)
            MainActivity.NotifyTaskChanged(_task, getActivity());
        }
      };

      if(rootView.getContext().getResources().getInteger(R.integer.orientation) == 0) { // tablet
        datePickerDialog = new DatePickerDialog(this.getContext(), listener, mYear, mMonth, mDay);
      } else { // small device
        datePickerDialog = new DatePickerDialog(this.getContext(), AlertDialog.THEME_TRADITIONAL, listener, mYear, mMonth, mDay);
      }

      datePickerDialog.show();
    }

    if (v == btnTimePicker) {

      // Get Current Time
      final Calendar c = Calendar.getInstance();
      mHour = tempDate != null ? tempDate.getHours() : c.get(Calendar.HOUR_OF_DAY);
      mMinute = tempDate != null ? tempDate.getMinutes() : c.get(Calendar.MINUTE);

      // Launch Time Picker Dialog
      TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
        new TimePickerDialog.OnTimeSetListener() {

          @Override
          public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
            if (tempDate == null) {
              tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
            }

            tempDate.setHours(hourOfDay);
            tempDate.setMinutes(minute);

            txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
            _task.date = tempDate;

            if (isNotifyEnabled)
              MainActivity.NotifyTaskChanged(_task, getActivity());
          }
        }, mHour, mMinute, true);
      timePickerDialog.show();
    }

    if (v == btnExportPicker) {
      if (_task.date == null) {
        Snackbar.make(rootView, getResources().getString(R.string.placeholder_export_no_date), Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
        return;
      }

      Date exportDate = new Date(_task.date.getTime());
      if (exportDate.getHours() == 0) {
        exportDate.setHours(8);
      }
      Intent intent = new Intent(Intent.ACTION_EDIT);
      intent.setType("vnd.android.cursor.item/event");
      intent.putExtra("beginTime", _task.date.getTime());
      intent.putExtra(CalendarContract.Events.ALL_DAY, false);
      intent.putExtra("endTime", exportDate);
      intent.putExtra(CalendarContract.Events.DESCRIPTION, _task.description);
      intent.putExtra(CalendarContract.Events.DISPLAY_COLOR, Color.MAGENTA);
      intent.putExtra(CalendarContract.Events.TITLE, _task.name);
      startActivity(intent);
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
  }

  public static void notifyTaskChanged() {
    if (instance != null)
      instance.needsUpdate = true;
  }
  private boolean needsUpdate = false;
  private Thread updaterThread;

  private Thread startTimerThread(final View rootView) {
    stopTimerThread();
    final Handler handler = new Handler();
    Runnable updater = new Runnable() {
      public void run() {
        while (!updaterThread.isInterrupted()) {
            if (needsUpdate) {
              needsUpdate = false;

              handler.postDelayed(new Runnable() {
                public void run() {
                  ((Activity) rootView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      final HashMap<TextView, String> linkMap = _initLinks();

                      if (_task == null) {
                        String taskId = getArguments().getString(TASK_ID);
                        _task = Task.TASK_MAP.get(taskId);
                      }

                      if (_task != null) {
                        setDetails(linkMap, rootView);
                      }
                    }
                  });
                }
              }, 250);
            }
        }
      }
    };

    updaterThread = new Thread(updater);
    updaterThread.start();

    return updaterThread;
  }

  private void stopTimerThread() {
    if (updaterThread != null)
      updaterThread.interrupt();
  }

  @Override
  public void onPause() {
    Log.d("DEBUG", "TaskDetailFragment onPause");

    if (isNotifyEnabled && instance != null && instance.getView() != null) {
      View f = instance.getView().findFocus();
      if(f != null)
        f.clearFocus();
    }

    txtCostsSig.removeTextChangedListener(currencyWatcher);
    txtCostsFractions.removeTextChangedListener(currencyWatcher);
    currencyWatcher.destroy();
    super.onPause();
    stopTimerThread();
  }

  public void onDestroy() {
    Log.d("DEBUG", "TaskDetailFragment onDestroy");
    if (isNotifyEnabled && instance != null && instance.getView() != null) {
      View f = instance.getView().findFocus();
      if(f != null)
        f.clearFocus();
    }
    txtCostsSig.removeTextChangedListener(currencyWatcher);
    txtCostsFractions.removeTextChangedListener(currencyWatcher);
    currencyWatcher.destroy();
    stopTimerThread();
    super.onDestroy();
    instance = null;
  }

  class TextInputFocusChangeListener implements View.OnFocusChangeListener {

    String fieldName;
    public TextInputFocusChangeListener(String field) {
      fieldName = field;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      Field field = null;
      String oldValue = null;
      String input = null;

      try {
        field = _task.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        oldValue = (String) field.get(_task);
        Editable et = ((EditText)v).getText();

        if(et != null) {
          input = et.toString();
        }

        if (input != null && !input.equals(oldValue)) {
          field.set(_task, input);
          MainActivity.NotifyTaskChanged(_task, getActivity());
        }

      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
