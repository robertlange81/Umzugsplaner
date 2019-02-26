package com.planner.removal.removalplanner.Fragments;

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
import com.planner.removal.removalplanner.Helper.Command;
import com.planner.removal.removalplanner.Helper.Formater;
import com.planner.removal.removalplanner.Model.Prio;
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
    private Task task;

    EditText txtName;
    EditText txtDescription;
    CheckBox checkIsDone;
    TextView lblIsDone;
    ImageView imgPrio;
    TextView lblPrio;
    EditText txtCosts;
    Spinner spinnerDetailType;
    Button btnDatePicker;
    Button btnTimePicker;
    EditText txtDeadline;
    TextView[] txtInputs;
    ImageView[] imgDeleteLinks;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;

    public static final int MAX_INPUT_FIELDS_FOR_LINKS = 5;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(TASK_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            if (getArguments().containsKey(TASK_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                task = Task.TASK_MAP.get(getArguments().getString(TASK_ID));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.detail, container, false);

        if(Formater.currentLocal == null) {
            Formater.setCurrentLocale(rootView.getContext());
        }

        txtName = rootView.findViewById(R.id.detail_name);
        checkIsDone = rootView.findViewById(R.id.detail_isDone);
        lblIsDone = rootView.findViewById(R.id.detail_isDone_label);
        imgPrio = rootView.findViewById(R.id.detail_prio);
        lblPrio = rootView.findViewById(R.id.detail_prio_label);
        txtDescription = rootView.findViewById(R.id.description);
        txtCosts = rootView.findViewById(R.id.detail_costs);
        spinnerDetailType = (Spinner) rootView.findViewById(R.id.type);

        btnDatePicker =(Button) rootView.findViewById(R.id.detail_btn_date);
        btnTimePicker =(Button) rootView.findViewById(R.id.detail_btn_time);
        txtDeadline =(EditText) rootView.findViewById(R.id.detail_deadline);

        txtInputs = new TextView[8];
        imgDeleteLinks = new ImageView[8];
        txtInputs[0] = (TextView) rootView.findViewById(R.id.links_0);
        txtInputs[1] = (TextView) rootView.findViewById(R.id.links_1);
        txtInputs[2] = (TextView) rootView.findViewById(R.id.links_2);
        txtInputs[3] = (TextView) rootView.findViewById(R.id.links_3);
        txtInputs[4] = (TextView) rootView.findViewById(R.id.links_4);
        txtInputs[5] = txtDescription;
        txtInputs[6] = txtCosts;
        txtInputs[7] = txtDeadline;

        imgDeleteLinks[0] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x0);
        imgDeleteLinks[1] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x1);
        imgDeleteLinks[2] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x2);
        imgDeleteLinks[3] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x3);
        imgDeleteLinks[4] = (ImageView) rootView.findViewById(R.id.detail_delete_links_icon_x4);
        imgDeleteLinks[5] = (ImageView) rootView.findViewById(R.id.detail_delete_info_icon);
        imgDeleteLinks[6] = (ImageView) rootView.findViewById(R.id.detail_delete_costs_icon);
        imgDeleteLinks[7] = (ImageView) rootView.findViewById(R.id.detail_delete_deadline_icon);

        _initListeners(rootView);
        final HashMap<TextView, String> linkMap = _initLinks();
        _initDeleteIcons(rootView);

        if (task != null) {
            setDetails(linkMap, rootView);

            final TaskType[] categories = TaskType.values();
            final List<String> catsString = new ArrayList<>();
            for (TaskType cat : categories) {
                catsString.add(cat.toString());
            }

            ArrayAdapter _categoryAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.simple_spinner_dropdown, catsString);
            spinnerDetailType.setAdapter(_categoryAdapter);
            if(task.type != null)
                spinnerDetailType.setSelection(task.type.getValue());
        }

        startTimerThread(rootView);
        return rootView;
    }

    private void _initListeners(final View rootView) {
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        checkIsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = rootView.getContext().getResources()
                        .getString(checkIsDone.isChecked() ? R.string.done : R.string.todo);
                task.isDone = checkIsDone.isChecked();
                lblIsDone.setText(msg);
                MainActivity.notifyTaskChanged();
                Snackbar.make(view, task.name + " " + msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imgPrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (task.prio == Prio.High) {
                    imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
                    task.prio = Prio.Normal;
                    lblPrio.setText(R.string.normalPrioText_short);
                } else {
                    imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
                    task.prio = Prio.High;
                    lblPrio.setText(R.string.highPrioText_short);
                }

                MainActivity.notifyTaskChanged();
                String msg = rootView.getContext().getResources()
                        .getString(task.prio == Prio.Normal ? R.string.normalPrioText : R.string.highPrioText)
                        + " " + task.name;

                Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
                snack.show();
            }
        });

        txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && txtName.getText() != null) {
                    String input = txtName.getText().toString();

                    if(!input.equals(task.name)) {
                        task.name = input;
                        MainActivity.notifyTaskChanged();
                        return;
                    }
                }
            }
        });

        spinnerDetailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.type = TaskType.values()[position];
                MainActivity.notifyTaskChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && txtDescription.getText() != null) {
                    String input = txtDescription.getText().toString();
                    if(!task.description.equals(input))
                        task.description = input;
                }
            }
        });

        txtCosts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && txtCosts.getText() != null) {
                    String input = txtCosts.getText().toString();

                    if(input.equals("")) {
                        task.costs = 0;
                        MainActivity.notifyTaskChanged();
                        return;
                    }
                }
            }
        });

        txtDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus &&  txtDeadline.getText() != null) {
                    String input = txtDeadline.getText().toString();

                    if(input.equals("")) {
                        task.date = null;
                        MainActivity.notifyTaskChanged();
                        return;
                    }
                }
            }
        });
    }

    private void setDetails(final HashMap<TextView, String> linkMap, View rootView) {
        txtName.setText(task.name);
        txtDescription.setText(task.description);
        checkIsDone.setChecked(task.isDone);
        String msg = rootView.getContext().getResources()
                .getString(checkIsDone.isChecked() ? R.string.done : R.string.todo);
        lblIsDone.setText(msg);

        if (task.prio == Prio.High) {
            imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
            lblPrio.setText(R.string.highPrioText_short);
        } else {
            imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
            lblPrio.setText(R.string.normalPrioText_short);
        }

        if(task.links != null && task.links.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : task.links.entrySet())
            {
                if(i > MAX_INPUT_FIELDS_FOR_LINKS)
                    break;

                linkMap.put(txtInputs[i], entry.getKey());
                _formatLink(txtInputs[i], entry.getValue(), entry.getKey());
                i++;
            }

            if(i < MAX_INPUT_FIELDS_FOR_LINKS)
                _formatLink(txtInputs[i], "_", "_");
        }

        if(task.costs > 0.00)
            txtCosts.setText(Formater.intCentToString(task.costs));

        if(task.date != null)
            txtDeadline.setText(Formater.formatDateToSring(task.date));
    }

    private void _initDeleteIcons(final View rootView) {
        final HashMap<ImageView, TextView> deleteMap = new HashMap<>();

        for (int i = 0; i < imgDeleteLinks.length; i++) {
            deleteMap.put(imgDeleteLinks[i], txtInputs[i]);

            imgDeleteLinks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    TextView t = deleteMap.get(view);
                    if(t.getText() != null && t.getText().toString() != "") {

                        String msg = (t.getTag() != null ? t.getTag().toString() : t.getText())
                                + " " + rootView.getResources().getString(R.string.removed);

                        Snackbar snack = Snackbar.make(
                                view,
                                msg,
                                Snackbar.LENGTH_LONG);

                        Task clone = new Task(task);

                        snack.setAction(
                                R.string.undo,
                                new Command(Command.CommandTyp.Undo, clone)
                        );
                        Log.e("new command", "new command");
                        snack.show();

                        t.setText("");
                        t.requestFocus();
                        t.clearFocus();
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
                                task.links.remove(key);
                                linkMap.remove(txtLink);
                            }

                            if(!inputLink.equals("")) {
                                task.links.put(inputLink, inputLink);
                                Log.e("put ", inputLink);
                                linkMap.put(txtLink, inputLink);

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
        Log.e("_formatLink, alt:  ", linkInput.getText().toString());
        Log.e("_formatLink, neu:  ", href + ":" + displayLink);

        String link = "";
        if(!href.isEmpty() && !displayLink.isEmpty())
            link += "<a href='" + href + "'>" + displayLink + "</a>  ";

        linkInput.setText(Html.fromHtml(link));
        linkInput.setAutoLinkMask(Linkify.WEB_URLS);
        linkInput.setLinksClickable(true);
        linkInput.setCursorVisible(true);
        ((TableRow) linkInput.getParent()).setVisibility(View.VISIBLE);

        Log.e("_formatLink, neu:  ", linkInput.getText().toString());
    }

    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

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

                            tempDate.setYear(year);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDeadline.setText(Formater.formatDateToSring(tempDate));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

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

                            txtDeadline.setText(Formater.formatDateToSring(tempDate));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }

    public static void notifyTaskChanged() {
        needsUpdate = true;
    }

    public static boolean needsUpdate = false;
    public static Thread updaterThread;
    private Thread startTimerThread(final View rootView) {
        stopTimerThread();
        final Handler handler = new Handler();
        Runnable updater = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        if(needsUpdate) {
                            needsUpdate = false;
                            handler.post(new Runnable(){
                                public void run() {
                                    task = Task.TASK_MAP.get(getArguments().getString(TASK_ID));
                                    final HashMap<TextView, String> linkMap = _initLinks();
                                    setDetails(linkMap, rootView);
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
        Thread thread = new Thread(updater);
        thread.start();

        return thread;
    }

    private void stopTimerThread() {
        if(updaterThread != null)
            updaterThread.interrupt();
    }

    public void onDestroy() {
        super.onDestroy();
        stopTimerThread();
    }
}
