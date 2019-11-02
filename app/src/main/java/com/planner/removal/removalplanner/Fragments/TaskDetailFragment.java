package com.planner.removal.removalplanner.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
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
public class TaskDetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String TASK_ID = "task_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Task _task;
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
    EditText txtDeadline;
    TextView[] txtInputs;
    ImageView[] imgDeleteLinks;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;

    Button btnSave;
    boolean isNotifyEnabled = true;

    public static final int MAX_INPUT_FIELDS_FOR_LINKS = 5;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
        int x = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DEBUG", "onCreate of Fragment");
        instance = this;

        if (getArguments().containsKey(TASK_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String taskId = getArguments().getString(TASK_ID);
            _task = Task.TASK_MAP.get(taskId);

            if(_task == null) {
                createNewTask();
            }
        }
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of Fragment");
        super.onResume();
        instance = this;
        if (_task == null && getArguments().containsKey(TASK_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            _task = Task.TASK_MAP.get(getArguments().getString(TASK_ID));

            if(_task == null) {
                createNewTask();
            }
        }
    }

    private void createNewTask() {
        _task = new Task("", "");
        Task.addTask(_task);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isNotifyEnabled = false;
        Log.e("DEBUG", "onCreateView");

        final View rootView = inflater.inflate(R.layout.detail, container, false);

        if(TaskFormater.currentLocal == null) {
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

        btnDatePicker = (Button) rootView.findViewById(R.id.detail_btn_date);
        btnTimePicker = (Button) rootView.findViewById(R.id.detail_btn_time);
        btnSave = (Button) rootView.findViewById(R.id.detail_btn_ok);

        if(MainActivity.mTwoPane) {
            btnSave.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }

        txtDeadline =(EditText) rootView.findViewById(R.id.detail_deadline);
        //txtDeadline.setInputType(InputType.TYPE_NULL);
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

        if (_task != null) {
            ArrayAdapter _categoryAdapter = new ArrayAdapter<String>(
                    rootView.getContext(),
                    R.layout.simple_spinner_dropdown,
                    rootView.getContext().getResources().getStringArray(R.array.base_task_types)
            );
            spinnerDetailType.setAdapter(_categoryAdapter);
            setDetails(null, rootView);
        }

        _initListeners(rootView);
        final HashMap<TextView, String> linkMap = _initLinks();
        _initDeleteIcons(rootView);

        if(updaterThread == null)
            startTimerThread(rootView);

        isNotifyEnabled = true;

        return rootView;
    }

    private void _initListeners(final View rootView) {
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

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
                if(!hasFocus && txtName.getText() != null) {
                    String input = txtName.getText().toString();

                    if(!input.equals(_task.name)) {
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
                if(_task != null && !_task.type.equals(TaskType.values()[position])) {
                    _task.type = TaskType.values()[position];
                    if(isNotifyEnabled)
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
                Log.e("x","x");
                if(!hasFocus && txtDescription.getText() != null) {
                    String input = txtDescription.getText().toString();
                    if(!_task.description.equals(input))
                        _task.description = input;
                }
            }
        });

        txtCostsSig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("costs", "focus out");
                if(!hasFocus && txtCostsSig.getText() != null) {
                    String input = txtCostsSig.getText().toString();

                    if(input.equals("")) {
                        _task.costs = 0L;
                        if(isNotifyEnabled)
                            MainActivity.NotifyTaskChanged(_task, getActivity());
                        return;
                    }
                }
            }
        });

        CurrencyWatcher cw = new CurrencyWatcher(txtCostsSig, txtCostsFractions, _task,"#,###");
        txtCostsSig.addTextChangedListener(cw);

        txtCostsFractions.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("costs", "focus out");
                if(!hasFocus && txtCostsFractions.getText() != null) {
                    String input = txtCostsFractions.getText().toString();

                    if(input.equals("")) {
                        _task.costs = 0L;
                        if(isNotifyEnabled)
                            MainActivity.NotifyTaskChanged(_task, getActivity());
                        return;
                    }
                }
            }
        });

        txtCostsFractions.addTextChangedListener(cw);

        txtDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus &&  txtDeadline.getText() != null) {
                    String input = txtDeadline.getText().toString();

                    if(input.equals("")) {
                        _task.date = null;
                        if(isNotifyEnabled)
                            MainActivity.NotifyTaskChanged(_task, getActivity());
                        return;
                    }
                }
            }
        });
    }

    private void setDetails(final HashMap<TextView, String> linkMap, View rootView) {

        isNotifyEnabled = false;
        if(!txtName.getText().toString().equals(_task.name))
            txtName.setText(_task.name);

        if(_task.type != null) {
            spinnerDetailType.setSelection(_task.type.getValue());
        }

        if(!txtDescription.getText().toString().equals(_task.description))
            txtDescription.setText(_task.description);

        if(!checkIsDone.isChecked() && _task.is_Done || checkIsDone.isChecked() && !_task.is_Done) {
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

        if(linkMap != null)
            mapTrashCanToInput(linkMap);

        if(_task.costs != 0.00) {
            txtCostsSig.setText(TaskFormater.intSigToString(_task.costs));
            txtCostsFractions.setText(TaskFormater.intFractionsToString(_task.costs));
        }

        if(_task.date != null) {
            tempDate = new Date(_task.date.getTime());
            txtDeadline.setText(TaskFormater.formatDateToSring(_task.date));
        }

        isNotifyEnabled = true;
    }

    private void mapTrashCanToInput(HashMap<TextView, String> linkMap) {
        if(_task.links != null && _task.links.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : _task.links.entrySet())
            {
                if(i > MAX_INPUT_FIELDS_FOR_LINKS)
                    break;

                linkMap.put(txtInputs[i], entry.getKey());
                _formatLink(txtInputs[i], entry.getValue(), entry.getKey());
                i++;
            }

            if(i < MAX_INPUT_FIELDS_FOR_LINKS)
                _formatLink(txtInputs[i], "", "");
        }
    }

    private void _initDeleteIcons(final View rootView) {
        final HashMap<ImageView, List<TextView>> deleteMap = new HashMap<>();

        for (int i = 0; i < imgDeleteLinks.length; i++) {

            List<TextView> inputList = deleteMap.get(imgDeleteLinks[i]);
            if(inputList == null || inputList.size() == 0) {
                inputList = new ArrayList<>();
            }

            inputList.add(txtInputs[i]);
            deleteMap.put(imgDeleteLinks[i], inputList);

            imgDeleteLinks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                List<TextView> tlist = deleteMap.get(view);
                for (TextView t:tlist) {
                    if(t.getText() != null && !t.getText().toString().equals("")) {

                        String msg = (t.getTag() != null ? t.getTag().toString() : t.getText())
                                + " " + rootView.getResources().getString(R.string.removed);

                        Snackbar snack = Snackbar.make(
                                view,
                                msg,
                                Snackbar.LENGTH_LONG);

                        Task clone = new Task(_task);

                        if(Task.TASK_MAP.get(_task.id) != null) {
                            snack.setAction(
                                    R.string.undo,
                                    new Command(Command.CommandTyp.Undo, clone, getActivity())
                            );
                            Log.e("new command", "new command");
                            snack.show();
                        }

                        t.setText("");
                        if(t == txtDeadline) {
                            _task.date = null;
                            MainActivity.NotifyTaskChanged(_task, getActivity());
                        } else {
                            t.requestFocus();
                            t.clearFocus();
                        }
                    }
                }
                }
            });
        }
    }

    private HashMap<TextView, String> _initLinks() {
        Log.e("setDetails", "setDetails");
        final HashMap<TextView, String> linkMap = new HashMap<>();

        for (final TextView txtLink : txtInputs) {

            if(!txtLink.getTag().toString().equalsIgnoreCase("Link"))
                continue;

            txtLink.setMovementMethod(LinkMovementMethod.getInstance());
            if(txtLink != txtInputs[0])
                ((TableRow) txtLink.getParent()).setVisibility(View.GONE);

            txtLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        try {
                            String inputLink = txtLink.getText().toString();
                            inputLink = inputLink.replaceAll("\\s+","");
                            boolean showNewLine = false;
                            String key = linkMap.get(txtLink);
                            if(key == null) {
                                showNewLine = true;
                            } else {
                                Log.e("remove ", key);
                                _task.links.remove(key);
                                linkMap.remove(txtLink);
                            }

                            if(!inputLink.equals("")) {
                                String value = inputLink;
                                if(!URLUtil.isValidUrl(value)) {
                                    if(!value.contains("www")) {
                                        value = "www." + value;
                                    }

                                    if(!value.contains("https")) {
                                        value = "https://" + value;
                                    }

                                    if(URLUtil.isValidUrl(value)) {
                                        _task.addLink(inputLink, value, true);
                                        Log.e("put ", inputLink);
                                        linkMap.put(txtLink, inputLink);
                                    }
                                }

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
                            }
                        } catch (Exception x) {
                            Log.e("Exception", x.getMessage());
                        }
                    }
                }
            });
        }
        return linkMap;
    }

    private void _formatLink(TextView linkInput, String href, String displayLink) {
        String link = "";
        if(!href.isEmpty() && !displayLink.isEmpty())
            link += "<a href='" + href + "'>" + displayLink + "</a>  ";

        linkInput.setText(Html.fromHtml(link));
        linkInput.setAutoLinkMask(Linkify.WEB_URLS);
        linkInput.setLinksClickable(true);
        linkInput.setCursorVisible(true);
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
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                                tempDate.setHours(0);
                                tempDate.setMinutes(0);
                            }

                            tempDate.setYear(year - 1900);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
                            _task.date = tempDate;

                            if(isNotifyEnabled)
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
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                            }

                            tempDate.setHours(hourOfDay);
                            tempDate.setMinutes(minute);

                            txtDeadline.setText(TaskFormater.formatDateToSring(tempDate));
                            _task.date = tempDate;

                            if(isNotifyEnabled)
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
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }

    public static void notifyTaskChanged() {
        if(instance != null)
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
                        if(needsUpdate) {
                            needsUpdate = false;

                            handler.post(new Runnable(){
                                public void run() {
                                    ((Activity) rootView.getContext()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final HashMap<TextView, String> linkMap = _initLinks();

                                            if(_task == null) {
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
                    }
                    catch (InterruptedException e) {
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
        if(updaterThread != null)
            updaterThread.interrupt();
    }

    public void onDestroy() {
        MainActivity.NotifyTaskChanged(_task, getActivity());
        stopTimerThread();
        super.onDestroy();
        instance = null;
    }
}
