package com.planner.removal.removalplanner.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Helpers.Command;
import com.planner.removal.removalplanner.Helpers.CurrencyWatcher;
import com.planner.removal.removalplanner.Helpers.TaskFormater;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;
import com.planner.removal.removalplanner.Model.TaskTypeMain;
import com.planner.removal.removalplanner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a
 * {@link MainActivity} in two-pane mode (on tablets)
 * or a {@link com.planner.removal.removalplanner.Activities.DetailActivity} on handsets.
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
  private CurrencyWatcher currencyWatcher;
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
    Log.e("DEBUG", "onCreate of TaskDetailFragment");
    instance = this;
    Log.e("DEBUG", "onCreate of TaskDetailFragment END");
  }

  private void createNewTask() {
    _task = new Task("", "");
    Task.addTask(_task);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Log.e("DEBUG", "onCreateView TaskDetailFragment");
    setHasOptionsMenu(true);
    rootView = inflater.inflate(R.layout.detail, container, false);
    Log.e("DEBUG", "onCreateView TaskDetailFragment 1");

    Log.e("DEBUG", "onCreateView TaskDetailFragment END");
    return rootView;
  }

  @Override
  public void onResume() {
    Log.e("DEBUG", "onResume of TaskDetailFragment");

    super.onResume();
    instance = this;

    if (getArguments().containsKey(TASK_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      String taskId = getArguments().getString(TASK_ID);

      if(_task != null &&_task.equals(Task.TASK_MAP.get(taskId)))
        return;

      _task = Task.TASK_MAP.get(taskId);

      if (_task == null) {
        createNewTask();
      }
    }

    if (TaskFormater.currentLocal == null) {
      TaskFormater.setCurrentLocale(rootView.getContext());
    }

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
    txtInputs[0] = (TextView) rootView.findViewById(R.id.links_0);
    txtInputs[1] = (TextView) rootView.findViewById(R.id.links_1);
    txtInputs[2] = (TextView) rootView.findViewById(R.id.links_2);
    txtInputs[3] = (TextView) rootView.findViewById(R.id.links_3);
    txtInputs[4] = (TextView) rootView.findViewById(R.id.links_4);
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

    if (_task == null && getArguments().containsKey(TASK_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      _task = Task.TASK_MAP.get(getArguments().getString(TASK_ID));

      if (_task == null) {
        createNewTask();
      }
    }

    if (updaterThread == null)
      startTimerThread(rootView);

    if (_task.name == null || _task.name.isEmpty()) {
      txtName.requestFocus();
    } else {
      txtName.clearFocus();
      txtName.setSelected(false);
    }

    isNotifyEnabled = true;
    Log.e("DEBUG", "onResume of TaskDetailFragment END");
  }

  private void _initListeners(final View rootView) {
    Log.e("DEBUG", "_initListeners");
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
            MainActivity.NotifyTaskChanged(_task, getActivity());
            return;
          }
        }
      }
    });

    spinnerDetailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (_task != null && _task.type.getValue() != position) {

          _task.type = new TaskType(position);

          if (isNotifyEnabled && MainActivity.mTwoPane)
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
        Log.e("DEBUG", "txt changed");
        if (!hasFocus && txtDescription.getText() != null) {
          String input = txtDescription.getText().toString();
          if (!_task.description.equals(input))
            _task.description = input;
        }
      }
    });

    txtCostsSig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        Log.d("costs", "focus out");
        if (!hasFocus && txtCostsSig.getText() != null) {
          String input = txtCostsSig.getText().toString();

          if (input.equals("")) {
            _task.costs = 0L;
            if (isNotifyEnabled && MainActivity.mTwoPane)
              MainActivity.NotifyTaskChanged(_task, getActivity());
            return;
          }
        }
      }
    });

    currencyWatcher = new CurrencyWatcher(txtCostsSig, txtCostsFractions, _task, "#,###");
    txtCostsSig.addTextChangedListener(currencyWatcher);
    txtCostsFractions.addTextChangedListener(currencyWatcher);

    txtCostsSig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && isNotifyEnabled && MainActivity.mTwoPane) {
          MainActivity.NotifyTaskChanged(_task, getActivity());
        }
      }
    });

    txtCostsFractions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && isNotifyEnabled && MainActivity.mTwoPane) {
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
            if (isNotifyEnabled && MainActivity.mTwoPane)
              MainActivity.NotifyTaskChanged(_task, getActivity());
            return;
          }
        }
      }
    });
    Log.e("DEBUG", "_initListeners END");
  }

  private void setDetails(final HashMap<TextView, String> linkMap, View rootView) {

    Log.e("DEBUG", "setDetails");
    isNotifyEnabled = false;
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

    if (_task.costs != 0.00) {
      txtCostsSig.removeTextChangedListener(currencyWatcher);
      txtCostsSig.setText(TaskFormater.intSigToString(_task.costs));
      txtCostsSig.addTextChangedListener(currencyWatcher);

      txtCostsFractions.removeTextChangedListener(currencyWatcher);
      txtCostsFractions.setText(TaskFormater.intFractionsToString(_task.costs));
      txtCostsFractions.addTextChangedListener(currencyWatcher);
    }

    if (_task.date != null) {
      tempDate = new Date(_task.date.getTime());
      txtDeadline.setText(TaskFormater.formatDateToSring(_task.date));
    }
    Log.e("DEBUG", "setDetails - END");
  }

  private void setLinks(HashMap<TextView, String> linkMap) {
    Log.e("DEBUG", "setLinks");
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
    Log.e("DEBUG", "setLinks - END");
  }

  private void _initDeleteIcons(final View rootView) {
    Log.e("DEBUG", "_initDeleteIcons");
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
              Log.e("new command", "new command");
              snack.show();
            }

            t.setText("");
            if (t == txtDeadline) {
              _task.date = null;
              if (isNotifyEnabled && MainActivity.mTwoPane)
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
    Log.e("DEBUG", "_initDeleteIcons - END");
  }

  private HashMap<TextView, String> _initLinks() {
    Log.e("DEBUG", "_initLinks");
    final HashMap<TextView, String> linkMap = new HashMap<>();

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
              boolean showNewLine = false;
              String key = linkMap.get(txtLink);
              if (key == null) {
                showNewLine = true;
              } else {
                Log.e("remove ", key);
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
                    Log.e("put ", inputLink);
                    linkMap.put(txtLink, inputLink);
                  }
                }

                                /*
                                if(showNewLine) {
                                    for (TextView t : txtInputs) {
                                        if(!t.getTag().toString().equalsIgnoreCase("Link"))
                                            continue;

                                        if(((TableRow) t.getParent()).getVisibility() != View.VISIBLE) {
                                            ((TableRow) t.getParent()).setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                                */
              }
            } catch (Exception x) {
              Log.e("Exception", x.getMessage());
            }
          }
        }
      });
    }

    Log.e("DEBUG", "_initLinks - END");
    return linkMap;
  }

  private void _formatLink(TextView linkInput, String href, String displayLink) {
    String link = "";
    if (!href.isEmpty() && !displayLink.isEmpty())
      link += "<a href='" + href + "'>" + displayLink + "</a>  ";

    linkInput.setText(Html.fromHtml(link));
    linkInput.setAutoLinkMask(Linkify.WEB_URLS);
    linkInput.setLinksClickable(true);
    ((TableRow) linkInput.getParent()).setVisibility(View.VISIBLE);
  }

  public void onClick(View v) {

    if (v == btnDatePicker || v == txtDeadline) {
      // Get Current date
      final Calendar c = Calendar.getInstance();
      mYear = tempDate != null ? tempDate.getYear() + 1900 : c.get(Calendar.YEAR);
      mMonth = tempDate != null ? tempDate.getMonth() : c.get(Calendar.MONTH);
      mDay = tempDate != null ? tempDate.getDate() : c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
        new DatePickerDialog.OnDateSetListener() {

          @Override
          public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
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

            if (isNotifyEnabled && MainActivity.mTwoPane)
              MainActivity.NotifyTaskChanged(_task, getActivity());
                            /*
                            String msg = getContext().getResources()
                                    .getString(R.string.date_changed);

                            Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
                            snack.show();
                            */
          }
        }, mYear, mMonth, mDay);
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

            if (isNotifyEnabled && MainActivity.mTwoPane)
              MainActivity.NotifyTaskChanged(_task, getActivity());
                            /*
                            String msg = getContext().getResources()
                                    .getString(R.string.time_changed);

                            Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
                            snack.show();
                            */
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

      Intent intent = new Intent(Intent.ACTION_EDIT);
      intent.setType("vnd.android.cursor.item/event");
      intent.putExtra("beginTime", _task.date.getTime());
      intent.putExtra(CalendarContract.Events.ALL_DAY, false);
      intent.putExtra("endTime", _task.date.getTime() + 60 * 60 * 1000);
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
          try {
            if (needsUpdate) {
              needsUpdate = false;

              handler.post(new Runnable() {
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
              });
            }
            Thread.sleep(250);
          } catch (InterruptedException e) {
            e.printStackTrace();
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
    Log.e("DEBUG", "TaskDetailFragment onPause");
    if (isNotifyEnabled) {
      MainActivity.NotifyTaskChanged(_task, getActivity());
    }
    stopTimerThread();
    super.onPause();
  }

  public void onDestroy() {
    MainActivity.NotifyTaskChanged(_task, getActivity());
    stopTimerThread();
    super.onDestroy();
    instance = null;
  }
}
